package com.example.zwergigel.dto;

import lombok.Data;

@Data
public class VideoDto {

    private final Long id;
    private final String url;
    private final Long duration;
    private final Long paid;
    private final String name;
    private final String thumbnailUrl;
    private final Double coefficient;

    public VideoDto(Long id, String url, Long duration, Long paid, String name, String thumbnailUrl) {
        this.id = id;
        this.url = url;
        this.duration = duration;
        this.paid = paid;
        this.name = name;
        this.thumbnailUrl = thumbnailUrl;
        this.coefficient = paid / (duration / 60d);
    }
}
