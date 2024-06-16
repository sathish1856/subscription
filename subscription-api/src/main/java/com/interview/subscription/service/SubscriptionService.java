package com.interview.subscription.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.interview.subscription.dto.SubscriptionDTO;
import com.interview.subscription.model.Hotel;
import com.interview.subscription.model.Status;
import com.interview.subscription.model.Subscription;
import com.interview.subscription.model.SubscriptionAudit;
import com.interview.subscription.model.Term;
import com.interview.subscription.repository.HotelRepository;
import com.interview.subscription.repository.SubscriptionAuditRepository;
import com.interview.subscription.repository.SubscriptionRepository;

@Service
public class SubscriptionService {

	@Autowired
	private SubscriptionRepository subscriptionRepository;

	@Autowired
	private HotelRepository hotelRepository;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private SubscriptionAuditRepository subscriptionAuditRepository;

	public void saveAuditRecord(Subscription subscription, String fieldChanged, String oldValue, String newValue) {
		SubscriptionAudit audit = new SubscriptionAudit();
		audit.setSubscription(subscription);
		audit.setChangeDate(LocalDateTime.now());
		audit.setFieldChanged(fieldChanged);
		audit.setOldValue(oldValue);
		audit.setNewValue(newValue);
		subscriptionAuditRepository.save(audit);
	}

	public SubscriptionDTO createSubscription(SubscriptionDTO subscriptionDTO) {
		Hotel hotel = validateHotel(subscriptionDTO.getHotelid());
		ensureNoActiveSubscription(subscriptionDTO.getHotelid());
		Subscription subscription = prepareSubscription(subscriptionDTO, hotel);
		Subscription savedSubscription = subscriptionRepository.save(subscription);
		saveAuditRecord(savedSubscription, "status", null, Status.ACTIVE.name());
		return convertToDTO(savedSubscription);
	}

	public SubscriptionDTO convertToDTO(Subscription subscription) {
		SubscriptionDTO subscriptionDTO = new SubscriptionDTO();
		BeanUtils.copyProperties(subscription, subscriptionDTO);
		// subscriptionDTO.setEndDate(subscription.getEndDate().toString());
		subscriptionDTO.setStartDate(subscription.getStartDate().toString());
		subscriptionDTO.setHotelid(subscription.getHotel().getHotelid());
		subscriptionDTO.setTerm(subscription.getTerm().name());
		subscriptionDTO.setNextPayment(subscription.getNextPayment().toString());
		subscriptionDTO.setStatus(subscription.getStatus().name());
		subscriptionDTO.setHotel(subscription.getHotel());
		return subscriptionDTO;
	}

	public SubscriptionDTO cancelSubscription(Long id) {
		Subscription subscription = subscriptionRepository.findById(id)
				.orElseThrow(() -> new NoSuchElementException("Subscription not found"));
		String oldStatus = subscription.getStatus().name();
		subscription.setStatus(Status.CANCELED);
		subscription.setEndDate(LocalDate.now());
		Subscription canceledSubscription = subscriptionRepository.save(subscription);
		saveAuditRecord(canceledSubscription, "status", oldStatus, Status.CANCELED.name());
		return convertToDTO(canceledSubscription);
	}

	public SubscriptionDTO restartSubscription(Long id) {
		Subscription subscription = subscriptionRepository.findById(id)
				.orElseThrow(() -> new NoSuchElementException("Subscription not found"));
		String oldStatus = subscription.getStatus().name();
		subscription.setStatus(Status.ACTIVE);
		subscription.setEndDate(null);
		subscription.setNextPayment(calculateNextPaymentDate(subscription));
		Subscription restartedSubscription = subscriptionRepository.save(subscription);
		saveAuditRecord(restartedSubscription, "status", oldStatus, Status.ACTIVE.name());
		return convertToDTO(restartedSubscription);
	}

	public SubscriptionDTO updateSubscription(Long id, SubscriptionDTO subscriptionDTO) {
		Hotel hotel = validateHotel(subscriptionDTO.getHotelid());
		Subscription subscription = subscriptionRepository.findById(id)
				.orElseThrow(() -> new NoSuchElementException("Subscription not found"));
		updateSubscriptionDetails(subscription, subscriptionDTO, hotel);
		Subscription updatedSubscription = subscriptionRepository.save(subscription);
		saveAuditRecord(updatedSubscription, "status", subscription.getStatus().name(),
				subscription.getStatus().name());
		return convertToDTO(updatedSubscription);
	}

	private Hotel validateHotel(Long hotelId) {
		return hotelRepository.findById(hotelId).orElseThrow(() -> new IllegalArgumentException("Invalid hotel ID"));
	}

	private void ensureNoActiveSubscription(Long hotelId) {
		subscriptionRepository.findByHotelIdAndStatus(hotelId, Status.ACTIVE).ifPresent(subscription -> {
			throw new IllegalStateException("Hotel already has an active subscription");
		});
	}

	private Subscription prepareSubscription(SubscriptionDTO subscriptionDTO, Hotel hotel) {
		Subscription subscription = new Subscription();
		subscription = subscriptionRepository.findByHotel(hotel);
		if(subscription == null) {
			subscription = objectMapper.convertValue(subscriptionDTO, Subscription.class);			
		}
		subscription.setHotel(hotel);
		subscription.setStatus(Status.ACTIVE);
		subscription.setNextPayment(calculateNextPaymentDate(subscription));
		return subscription;
	}

	private LocalDate calculateNextPaymentDate(Subscription subscription) {
		int termInMonths = subscription.getTerm() == Term.MONTHLY ? 1 : 12;
		return subscription.getStartDate().plusMonths(termInMonths);
	}

	private void updateSubscriptionDetails(Subscription subscription, SubscriptionDTO subscriptionDTO, Hotel hotel) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		subscription.setStartDate(LocalDate.parse(subscriptionDTO.getStartDate(), formatter));
		subscription.setNextPayment(LocalDate.parse(subscriptionDTO.getNextPayment(), formatter));
		subscription.setEndDate(LocalDate.parse(subscriptionDTO.getNextPayment(), formatter));
		subscription.setTerm(Term.valueOf(subscriptionDTO.getTerm()));
		subscription.setStatus(Status.valueOf(subscriptionDTO.getStatus()));
		subscription.setHotel(hotel);
	}

	public List<SubscriptionDTO> filterSubscriptionsByStatus(String status) {
		List<Subscription> subscriptions = new ArrayList<>();
		if (status.equalsIgnoreCase("ALL")) {
			subscriptions = subscriptionRepository.findAll();
		} else {
			Status statusEnum = Status.valueOf(status.toUpperCase());
			subscriptions = subscriptionRepository.findByStatus(statusEnum);
		}
		return subscriptions.stream().map(subscription -> convertToDTO(subscription)).collect(Collectors.toList());
	}
	
    public void checkAndUpdateExpiredSubscriptions() {
        List<Subscription> activeSubscriptions = subscriptionRepository.findByStatus(Status.ACTIVE);
        LocalDate today = LocalDate.now();

        List<Subscription> expiredSubscriptions = activeSubscriptions.stream()
                .filter(subscription -> subscription.getEndDate() != null && subscription.getEndDate().isBefore(today))
                .peek(subscription -> subscription.setStatus(Status.EXPIRED))
                .collect(Collectors.toList());
        subscriptionRepository.saveAll(expiredSubscriptions);
    }
}
