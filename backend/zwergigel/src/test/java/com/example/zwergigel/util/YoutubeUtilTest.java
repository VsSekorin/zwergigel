package com.example.zwergigel.util;

import com.example.zwergigel.dto.VideoData;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class YoutubeUtilTest {

    @Test
    void getData() {
        final VideoData expected = new VideoData()
            .withName("M117: Breaking responsibility down is the responsibility of a manager/architect")
            .withDuration(281L)
            .withThumbnailUrl("https://i.ytimg.com/vi/kPTKT3jOG7c/maxresdefault.jpg");
        assertEquals(expected, YoutubeUtil.getData("https://youtu.be/kPTKT3jOG7c"));
    }
}