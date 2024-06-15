package com.interview.subscription.service;

import java.time.LocalDate;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.interview.subscription.model.Hotel;
import com.interview.subscription.model.Status;
import com.interview.subscription.model.Subscription;
import com.interview.subscription.model.Term;
import com.interview.subscription.repository.HotelRepository;
import com.interview.subscription.repository.SubscriptionRepository;

@Service
public class SubscriptionService {

	@Autowired
	private SubscriptionRepository subscriptionRepository;
    @Autowired
    private HotelRepository hotelRepository;

    @PostMapping
    public ResponseEntity<Subscription> createSubscription(@RequestBody Subscription subscription) {
        Long hotelid = 1L;
        Optional<Hotel> hotelOptional = hotelRepository.findById(hotelid);
        if (!hotelOptional.isPresent()) {
            return ResponseEntity.badRequest().build();
        }

        Optional<Subscription> activeSubscription = subscriptionRepository
                .findByHotelIdAndStatus(hotelid, Status.ACTIVE);
        if (activeSubscription.isPresent()) {
            throw new IllegalStateException("Hotel already has an active subscription");
        }

        subscription.setHotel(hotelOptional.get());
        subscription.setStatus(Status.ACTIVE);
        subscription.setNextPayment(
                subscription.getStartDate().plusMonths(subscription.getTerm() == Term.MONTHLY ? 1 : 12));
        Subscription savedSubscription = subscriptionRepository.save(subscription);
        return ResponseEntity.ok(savedSubscription);
    }


	public Subscription cancelSubscription(Long subscriptionID) {
		Optional<Subscription> subscriptionOpt = subscriptionRepository.findById(subscriptionID);
		if (subscriptionOpt.isPresent()) {
			Subscription subscription = subscriptionOpt.get();
			subscription.setStatus(Status.CANCELED);
			subscription.setEndDate(LocalDate.now());
			return subscriptionRepository.save(subscription);
		} else {
			throw new IllegalStateException("Subscription not found");
		}
	}

	public List<Subscription> getAllSubscriptions() {
		return subscriptionRepository.findAll();
	}

	public List<Subscription> filterSubscriptions(Status status, LocalDate startDateMonth) {
		if (status != null && startDateMonth != null) {
			return subscriptionRepository.findByStartDateMonth(startDateMonth).stream()
					.filter(subscription -> subscription.getStatus() == status).toList();
		} else if (status != null) {
			return subscriptionRepository.findByStatus(status);
		} else if (startDateMonth != null) {
			return subscriptionRepository.findByStartDateMonth(startDateMonth);
		} else {
			return getAllSubscriptions();
		}
	}

	public Subscription restartSubscription(Long subscriptionID) {
		Optional<Subscription> subscriptionOpt = subscriptionRepository.findById(subscriptionID);
		if (subscriptionOpt.isPresent()) {
			Subscription subscription = subscriptionOpt.get();
			subscription.setStatus(Status.ACTIVE);
			subscription.setNextPayment(LocalDate.now().plusMonths(subscription.getTerm() == Term.MONTHLY ? 1 : 12));
			subscription.setEndDate(null);
			return subscriptionRepository.save(subscription);
		} else {
			throw new IllegalStateException("Subscription not found");
		}
	}

	public Subscription getSubscription(Long subscriptionID) {
		return subscriptionRepository.findById(subscriptionID)
				.orElseThrow(() -> new IllegalStateException("Subscription not found"));
	}

}
