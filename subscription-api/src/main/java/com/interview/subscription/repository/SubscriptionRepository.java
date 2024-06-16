package com.interview.subscription.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.interview.subscription.model.Hotel;
import com.interview.subscription.model.Status;
import com.interview.subscription.model.Subscription;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    @Query("SELECT s FROM Subscription s WHERE s.hotel.hotelid = :hotelid AND s.status = :status")
    Optional<Subscription> findByHotelIdAndStatus(@Param("hotelid") Long hotelid, @Param("status") Status status);

	List<Subscription> findByStatus(Status status);

	Subscription findByHotel(Hotel hotel);
}
