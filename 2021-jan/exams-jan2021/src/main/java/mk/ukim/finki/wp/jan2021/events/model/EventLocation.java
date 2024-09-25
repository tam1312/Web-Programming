package mk.ukim.finki.wp.jan2021.events.model;

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class EventLocation {

    public EventLocation() {
    }

    public EventLocation(String name) {
        this.name = name;
    }

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
