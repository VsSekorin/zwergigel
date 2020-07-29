package com.example.zwergigel.dto;

import lombok.Data;

import java.time.Duration;

@Data
public class VideoDto {

    private final Long id;
    private final String url;
    private final Long duration;
    private final Long paid;
    private final Double coefficient;

    public VideoDto(Long id, String url, Duration duration, Long paid) {
        this(id, url, duration.getSeconds(), paid);
    }

    public VideoDto(Long id, String url, Long duration, Long paid) {
        this.id = id;
        this.url = url;
        this.duration = duration;
        this.paid = paid;
        this.coefficient = paid / (duration / 60d);
    }
}
