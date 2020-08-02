package com.example.zwergigel.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public final class VideoData {

    private String name;
    private Long duration;
    private String thumbnailUrl;

    public VideoData() {
    }

    @Override
    public String toString() {
        return "VideoData{" +
            "name='" + name + '\'' +
            ", duration=" + duration +
            ", thumbnailUrl='" + thumbnailUrl + '\'' +
            '}';
    }
}
