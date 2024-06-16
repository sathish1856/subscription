package com.interview.subscription.controller;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.interview.subscription.dto.ErrorResponse;
import com.interview.subscription.dto.SubscriptionDTO;
import com.interview.subscription.model.Subscription;
import com.interview.subscription.repository.SubscriptionRepository;
import com.interview.subscription.service.SubscriptionService;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/v1/subscriptions")
public class SubscriptionController {

    @Autowired
    private SubscriptionRepository subscriptionRepository;
    
    @Autowired
    SubscriptionService subscriptionService;

    @GetMapping
    public List<SubscriptionDTO> getAllSubscriptions() {
        List<Subscription> subscriptions = subscriptionRepository.findAll();
        return subscriptions.stream()
                .map(subscription -> subscriptionService.convertToDTO(subscription))
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubscriptionDTO> getSubscriptionById(@PathVariable("id") Long id) {
        Optional<Subscription> subscriptionOptional = subscriptionRepository.findById(id);
        return subscriptionOptional.map(subscription -> ResponseEntity.ok(subscriptionService.convertToDTO(subscription)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> createSubscription(@RequestBody SubscriptionDTO subscriptionDTO) {
    	try {
            SubscriptionDTO createdSubscription = subscriptionService.createSubscription(subscriptionDTO);
            return ResponseEntity.ok(createdSubscription);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse(e.getMessage()));
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<?> updateSubscription(@PathVariable("id") Long id, @RequestBody SubscriptionDTO subscriptionDTO) {
        try {
            SubscriptionDTO updatedSubscription = subscriptionService.updateSubscription(id, subscriptionDTO);
            return ResponseEntity.ok(updatedSubscription);
        } catch (IllegalArgumentException e) {
        	return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        } catch (NoSuchElementException e) {
        	return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse(e.getMessage()));
        }
    }

    @PostMapping("/cancel/{id}")
    public ResponseEntity<?> cancelSubscription(@PathVariable("id") Long id) {
        try {
            SubscriptionDTO canceledSubscription = subscriptionService.cancelSubscription(id);
            return ResponseEntity.ok(canceledSubscription);
        } catch (IllegalArgumentException e) {
        	return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }

    @PostMapping("/restart/{id}")
    public ResponseEntity<?> restartSubscription(@PathVariable("id") Long id) {
        try {
            SubscriptionDTO restartedSubscription = subscriptionService.restartSubscription(id);
            return ResponseEntity.ok(restartedSubscription);
        } catch (IllegalArgumentException e) {
        	return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }

    @GetMapping("/filter/{status}")
    public ResponseEntity<List<SubscriptionDTO>> filterSubscriptionsByStatus(@PathVariable("status") String status) {
        List<SubscriptionDTO> subscriptions = subscriptionService.filterSubscriptionsByStatus(status);
        return ResponseEntity.ok(subscriptions);
    }


}
