package mk.ukim.finki.wp.jan2021.events.model;

import javax.persistence.*;

@Entity
public class Event {

    public Event() {
    }

    public Event(String name, String description, Double price, EventType type, EventLocation location) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.type = type;
        this.location = location;
        this.likes = 0;
    }

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private String description;

    private Double price;

    @Enumerated(EnumType.STRING)
    private EventType type;

    @ManyToOne
    private EventLocation location;

    private Integer likes = 0;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public EventType getType() {
        return type;
    }

    public void setType(EventType type) {
        this.type = type;
    }

    public EventLocation getLocation() {
        return location;
    }

    public void setLocation(EventLocation location) {
        this.location = location;
    }

    public Integer getLikes() {
        return likes;
    }

    public void setLikes(Integer likes) {
        this.likes = likes;
    }
}
