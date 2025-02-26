package io.kk.bandhub.venueservice.repository.booking;

import io.kk.bandhub.venueservice.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaBookingRepository extends JpaRepository<Booking, Integer>, BookingRepository, JpaSpecificationExecutor<Booking> {

}
