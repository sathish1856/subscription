package com.interview.subscription.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.interview.subscription.dto.SubscriptionDTO;
import com.interview.subscription.model.Hotel;
import com.interview.subscription.model.Status;
import com.interview.subscription.model.Subscription;
import com.interview.subscription.model.Term;
import com.interview.subscription.repository.HotelRepository;
import com.interview.subscription.repository.SubscriptionRepository;

@RestController
@RequestMapping("/api/subscriptions")
public class SubscriptionController {

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @GetMapping
    public List<SubscriptionDTO> getAllSubscriptions() {
        List<Subscription> subscriptions = subscriptionRepository.findAll();
        return subscriptions.stream()
                .map(subscription -> convertToDTO(subscription))
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubscriptionDTO> getSubscriptionById(@PathVariable("id") Long id) {
        Optional<Subscription> subscriptionOptional = subscriptionRepository.findById(id);
        return subscriptionOptional.map(subscription -> ResponseEntity.ok(convertToDTO(subscription)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<SubscriptionDTO> createSubscription(@RequestBody SubscriptionDTO subscriptionDTO) {
        Optional<Hotel> hotelOptional = hotelRepository.findById(subscriptionDTO.getHotelid());
        if (!hotelOptional.isPresent()) {
            return ResponseEntity.badRequest().build();
        }

        Optional<Subscription> activeSubscription = subscriptionRepository
                .findByHotelIdAndStatus(subscriptionDTO.getHotelid(), Status.ACTIVE);
        if (activeSubscription.isPresent() && !activeSubscription.isEmpty()) {
            throw new IllegalStateException("Hotel already has an active subscription");
        }

        Subscription subscription = objectMapper.convertValue(subscriptionDTO, Subscription.class);
        subscription.setHotel(hotelOptional.get());
        subscription.setStatus(Status.ACTIVE);
        subscription.setNextPayment(
                subscription.getStartDate().plusMonths(subscription.getTerm() == Term.MONTHLY ? 1 : 12));
        Subscription savedSubscription = subscriptionRepository.save(subscription);
        return ResponseEntity.ok(convertToDTO(savedSubscription));
    }

	@PutMapping("/{id}")
    public ResponseEntity<SubscriptionDTO> updateSubscription(@PathVariable("id") Long id, @RequestBody SubscriptionDTO subscriptionDTO) {
        Optional<Hotel> hotelOptional = hotelRepository.findById(subscriptionDTO.getHotelid());
        if (!hotelOptional.isPresent()) {
            return ResponseEntity.badRequest().build();
        }

        Optional<Subscription> subscriptionOptional = subscriptionRepository.findById(id);
        if (!subscriptionOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        Subscription subscription = subscriptionOptional.get();
        subscription.setStartDate(LocalDate.parse(subscriptionDTO.getStartDate(), formatter));
        subscription.setNextPayment(LocalDate.parse(subscriptionDTO.getNextPayment(), formatter));
        subscription.setEndDate(LocalDate.parse(subscriptionDTO.getNextPayment(), formatter));
        subscription.setTerm(Term.valueOf(subscriptionDTO.getTerm()));
        subscription.setStatus(Status.valueOf(subscriptionDTO.getStatus()));
        subscription.setHotel(hotelOptional.get());

        Subscription updatedSubscription = subscriptionRepository.save(subscription);
        return ResponseEntity.ok(convertToDTO(updatedSubscription));
    }

    @PostMapping("/cancel/{id}")
    public ResponseEntity<SubscriptionDTO> cancelSubscription(@PathVariable("id") Long id) {
        Optional<Subscription> subscriptionOptional = subscriptionRepository.findById(id);
        if (!subscriptionOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        Subscription subscription = subscriptionOptional.get();
        subscription.setStatus(Status.CANCELED);
        subscription.setEndDate(java.time.LocalDate.now());
        Subscription canceledSubscription = subscriptionRepository.save(subscription);
        return ResponseEntity.ok(convertToDTO(canceledSubscription));
    }

    @PostMapping("/restart/{id}")
    public ResponseEntity<SubscriptionDTO> restartSubscription(@PathVariable("id") Long id) {
        Optional<Subscription> subscriptionOptional = subscriptionRepository.findById(id);
        if (!subscriptionOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        Subscription subscription = subscriptionOptional.get();
        subscription.setStatus(Status.ACTIVE);
        subscription.setEndDate(null);
        subscription.setNextPayment(subscription.getTerm() == Term.MONTHLY ?
                subscription.getNextPayment().plusMonths(1) :
                subscription.getNextPayment().plusYears(1));
        Subscription restartedSubscription = subscriptionRepository.save(subscription);
        return ResponseEntity.ok(convertToDTO(restartedSubscription));
    }

    @GetMapping("/status/{status}")
    public List<SubscriptionDTO> getSubscriptionsByStatus(@PathVariable("status") String status) {
        List<Subscription> subscriptions = subscriptionRepository.findByStatus(Status.valueOf(status));
        return subscriptions.stream()
                .map(subscription -> convertToDTO(subscription))
                .collect(Collectors.toList());
    }

    private SubscriptionDTO convertToDTO(Subscription subscription) {
    	SubscriptionDTO subscriptionDTO = new SubscriptionDTO();
        BeanUtils.copyProperties(subscription, subscriptionDTO);
       // subscriptionDTO.setEndDate(subscription.getEndDate().toString());
        subscriptionDTO.setStartDate(subscription.getStartDate().toString());
        subscriptionDTO.setHotelid(subscription.getHotel().getHotelid());
        subscriptionDTO.setTerm(subscription.getTerm().name());
        subscriptionDTO.setNextPayment(subscription.getNextPayment().toString());
        subscriptionDTO.setStatus(subscription.getStatus().name());
        return subscriptionDTO;
    }

    @SuppressWarnings("unused")
	private Subscription convertToEntity(SubscriptionDTO subscriptionDTO) {
        Subscription subscription = new Subscription();
        BeanUtils.copyProperties(subscriptionDTO, subscription);
        return subscription;
    }
}
