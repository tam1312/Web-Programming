package mk.ukim.finki.wp.jan2021.events.repository;

import mk.ukim.finki.wp.jan2021.events.model.Event;
import mk.ukim.finki.wp.jan2021.events.model.EventType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findAllByPriceLessThan(Double price);
    List<Event> findAllByType(EventType type);
    List<Event> findAllByPriceLessThanAndType(Double price, EventType type);
}
