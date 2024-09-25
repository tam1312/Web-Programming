package mk.ukim.finki.wp.jan2021.events.repository;

import mk.ukim.finki.wp.jan2021.events.model.Event;
import mk.ukim.finki.wp.jan2021.events.model.EventLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventLocationRepository extends JpaRepository<EventLocation,Long> {
}
