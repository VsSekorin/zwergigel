package com.example.zwergigel.entity;

import com.example.zwergigel.dto.VideoData;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
public class Video {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String url;
    private Long duration;
    private Long paid;
    private String name;
    private String thumbnailUrl;

    @ManyToOne @JoinColumn
    private User user;

    public Video() {
    }

    public Video(String url, Long paid, VideoData data, User user) {
        this(url, data.getDuration(), paid, data.getName(), data.getThumbnailUrl(), user);
    }

    public Video(String url, Long duration, Long paid, String name, String thumbnailUrl, User user) {
        this.url = url;
        this.duration = duration;
        this.paid = paid;
        this.name = name;
        this.thumbnailUrl = thumbnailUrl;
        this.user = user;
    }

    public Video addPaid(final Long add) {
        this.paid = this.paid + add;
        return this;
    }
}
