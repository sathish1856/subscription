package com.interview.subscription.repository;

import com.interview.subscription.model.Subscription;
import com.interview.subscription.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    @Query("SELECT s FROM Subscription s WHERE s.hotel.hotelid = :hotelid AND s.status = :status")
    Optional<Subscription> findByHotelIdAndStatus(@Param("hotelid") Long hotelid, @Param("status") Status status);

    @Query("SELECT s FROM Subscription s WHERE s.status = :status")
    List<Subscription> findByStatus(@Param("status") Status status);

    @Query("SELECT s FROM Subscription s WHERE MONTH(s.startDate) = MONTH(:startDateMonth) AND YEAR(s.startDate) = YEAR(:startDateMonth)")
    List<Subscription> findByStartDateMonth(@Param("startDateMonth") LocalDate startDateMonth);
}
