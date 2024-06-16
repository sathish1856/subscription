package com.interview.subscription.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.interview.subscription.dto.ErrorResponse;
import com.interview.subscription.dto.SubscriptionDTO;
import com.interview.subscription.model.Hotel;
import com.interview.subscription.model.Status;
import com.interview.subscription.model.Subscription;
import com.interview.subscription.model.Term;
import com.interview.subscription.repository.HotelRepository;
import com.interview.subscription.repository.SubscriptionRepository;
import com.interview.subscription.service.SubscriptionService;

@RestController
@RequestMapping("/api/subscriptions")
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

//	@PutMapping("/{id}")
//    public ResponseEntity<SubscriptionDTO> updateSubscription(@PathVariable("id") Long id, @RequestBody SubscriptionDTO subscriptionDTO) {
//        Optional<Hotel> hotelOptional = hotelRepository.findById(subscriptionDTO.getHotelid());
//        if (!hotelOptional.isPresent()) {
//            return ResponseEntity.badRequest().build();
//        }
//
//        Optional<Subscription> subscriptionOptional = subscriptionRepository.findById(id);
//        if (!subscriptionOptional.isPresent()) {
//            return ResponseEntity.notFound().build();
//        }
//
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
//        Subscription subscription = subscriptionOptional.get();
//        subscription.setStartDate(LocalDate.parse(subscriptionDTO.getStartDate(), formatter));
//        subscription.setNextPayment(LocalDate.parse(subscriptionDTO.getNextPayment(), formatter));
//        subscription.setEndDate(LocalDate.parse(subscriptionDTO.getNextPayment(), formatter));
//        subscription.setTerm(Term.valueOf(subscriptionDTO.getTerm()));
//        subscription.setStatus(Status.valueOf(subscriptionDTO.getStatus()));
//        subscription.setHotel(hotelOptional.get());
//
//        String oldStatus = subscription.getStatus().name();
//        Subscription updatedSubscription = subscriptionRepository.save(subscription);
//        subscriptionService.saveAuditRecord(updatedSubscription, "status", oldStatus, subscription.getStatus().name());
//        return ResponseEntity.ok(convertToDTO(updatedSubscription));
//    }
//
//    @PostMapping("/cancel/{id}")
//    public ResponseEntity<SubscriptionDTO> cancelSubscription(@PathVariable("id") Long id) {
//        Optional<Subscription> subscriptionOptional = subscriptionRepository.findById(id);
//        if (!subscriptionOptional.isPresent()) {
//            return ResponseEntity.notFound().build();
//        }
//        Subscription subscription = subscriptionOptional.get();
//        String oldStatus = subscription.getStatus().name();
//        subscription.setStatus(Status.CANCELED);
//        subscription.setEndDate(java.time.LocalDate.now());
//        Subscription canceledSubscription = subscriptionRepository.save(subscription);
//        subscriptionService.saveAuditRecord(canceledSubscription, "status", oldStatus, Status.CANCELED.name());
//        return ResponseEntity.ok(convertToDTO(canceledSubscription));
//    }
//
//    @PostMapping("/restart/{id}")
//    public ResponseEntity<SubscriptionDTO> restartSubscription(@PathVariable("id") Long id) {
//        Optional<Subscription> subscriptionOptional = subscriptionRepository.findById(id);
//        if (!subscriptionOptional.isPresent()) {
//            return ResponseEntity.notFound().build();
//        }
//        Subscription subscription = subscriptionOptional.get();
//        String oldStatus = subscription.getStatus().name();
//        subscription.setStatus(Status.ACTIVE);
//        subscription.setEndDate(null);
//        subscription.setNextPayment(subscription.getTerm() == Term.MONTHLY ?
//                subscription.getNextPayment().plusMonths(1) :
//                subscription.getNextPayment().plusYears(1));
//        Subscription restartedSubscription = subscriptionRepository.save(subscription);
//        subscriptionService.saveAuditRecord(restartedSubscription, "status", oldStatus, Status.ACTIVE.name());
//        return ResponseEntity.ok(convertToDTO(restartedSubscription));
//    }



}
