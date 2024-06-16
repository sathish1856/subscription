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
import org.springframework.web.bind.annotation.RestController;

import com.interview.subscription.dto.ErrorResponse;
import com.interview.subscription.dto.SubscriptionDTO;
import com.interview.subscription.model.Subscription;
import com.interview.subscription.repository.SubscriptionRepository;
import com.interview.subscription.service.SubscriptionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/v1/subscriptions")
public class SubscriptionController {

	@Autowired
	private SubscriptionRepository subscriptionRepository;

	@Autowired
	SubscriptionService subscriptionService;

	@Operation(summary = "Get List of all Subscriptions")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Listed all the Subscriptions", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = Subscription.class)) }),
			@ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
			@ApiResponse(responseCode = "404", description = "Subscriptions not found", content = @Content) })
	@GetMapping
	public List<SubscriptionDTO> getAllSubscriptions() {
		List<Subscription> subscriptions = subscriptionRepository.findAll();
		return subscriptions.stream().map(subscription -> subscriptionService.convertToDTO(subscription))
				.collect(Collectors.toList());
	}

	@Operation(summary = "Get subscription based on ID")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Get a Subscription", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = Subscription.class)) }),
			@ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
			@ApiResponse(responseCode = "404", description = "Subscription not found", content = @Content)})
	@GetMapping("/{id}")
	public ResponseEntity<SubscriptionDTO> getSubscriptionById(@PathVariable("id") Long id) {
		Optional<Subscription> subscriptionOptional = subscriptionRepository.findById(id);
		return subscriptionOptional
				.map(subscription -> ResponseEntity.ok(subscriptionService.convertToDTO(subscription)))
				.orElseGet(() -> ResponseEntity.notFound().build());
	}

	@Operation(summary = "Get List of all Subscriptions")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Listed all the Subscriptions", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = Subscription.class)) }),
			@ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
			@ApiResponse(responseCode = "404", description = "Subscriptions not found", content = @Content),
			@ApiResponse(responseCode = "409", description = "Active Subscription avaliable for hotel", content = @Content)})
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

	@Operation(summary = "Updates the given subscription")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Returns the updated subscription", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = Subscription.class)) }),
			@ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
			@ApiResponse(responseCode = "404", description = "Subscriptions not found", content = @Content) })
	@PutMapping("/{id}")
	public ResponseEntity<?> updateSubscription(@PathVariable("id") Long id,
			@RequestBody SubscriptionDTO subscriptionDTO) {
		try {
			SubscriptionDTO updatedSubscription = subscriptionService.updateSubscription(id, subscriptionDTO);
			return ResponseEntity.ok(updatedSubscription);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
		} catch (NoSuchElementException e) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse(e.getMessage()));
		}
	}

	@Operation(summary = "Cancesl the subscription for given subscriptionID")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Returns cancelled subscription", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = Subscription.class)) }),
			@ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
			@ApiResponse(responseCode = "404", description = "Subscriptions not found", content = @Content) })
	@PostMapping("/cancel/{id}")
	public ResponseEntity<?> cancelSubscription(@PathVariable("id") Long id) {
		try {
			SubscriptionDTO canceledSubscription = subscriptionService.cancelSubscription(id);
			return ResponseEntity.ok(canceledSubscription);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
		}
	}

	@Operation(summary = "Restart the existing subscription")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Returns restarted subscription", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = Subscription.class)) }),
			@ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
			@ApiResponse(responseCode = "404", description = "Subscriptions not found", content = @Content) })
	@PostMapping("/restart/{id}")
	public ResponseEntity<?> restartSubscription(@PathVariable("id") Long id) {
		try {
			SubscriptionDTO restartedSubscription = subscriptionService.restartSubscription(id);
			return ResponseEntity.ok(restartedSubscription);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
		}
	}

	@Operation(summary = "Get List of all Subscriptions for given filter")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Listed all the Subscriptions for given filter", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = Subscription.class)) }),
			@ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
			@ApiResponse(responseCode = "404", description = "Subscriptions not found", content = @Content) })
	@GetMapping("/filter/{status}")
	public ResponseEntity<List<SubscriptionDTO>> filterSubscriptionsByStatus(@PathVariable("status") String status) {
		List<SubscriptionDTO> subscriptions = subscriptionService.filterSubscriptionsByStatus(status);
		return ResponseEntity.ok(subscriptions);
	}

}
