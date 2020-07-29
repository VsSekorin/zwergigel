package com.example.zwergigel.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.Duration;

@Entity
@Getter @Setter
public class Video {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String url;
    private Duration duration;
    private Long paid;

    @ManyToOne @JoinColumn
    private User user;

    public Video() {
    }

    public Video(String url, Duration duration, Long paid, User user) {
        this.url = url;
        this.duration = duration;
        this.paid = paid;
        this.user = user;
    }

    public Video addPaid(final Long add) {
        this.paid = this.paid + add;
        return this;
    }
}
