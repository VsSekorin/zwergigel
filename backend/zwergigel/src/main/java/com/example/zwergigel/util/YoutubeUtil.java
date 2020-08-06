package com.example.zwergigel.util;

import com.example.zwergigel.dto.VideoData;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

import java.net.URL;
import java.time.Duration;

@UtilityClass
public class YoutubeUtil {

    @SneakyThrows
    public VideoData getData(final String url) {
        final Elements elements = Jsoup.parse(new URL(url), 10000).body()
            .getElementsByAttribute("itemprop");
        return new VideoData()
            .withName(getName(elements))
            .withDuration(getDuration(elements))
            .withThumbnailUrl(getThumbnailUrl(elements));
    }

    private String getName(final Elements elements) {
        return elements.stream()
            .filter(elem -> "name".equals(elem.attr("itemprop")))
            .findFirst()
            .map(elem -> elem.attr("content"))
            .orElse("");
    }

    private Long getDuration(final Elements elements) {
        return elements.stream()
            .filter(elem -> "duration".equals(elem.attr("itemprop")))
            .findFirst()
            .map(elem -> Duration.parse(elem.attr("content")).getSeconds())
            .orElse(0L);
    }

    private String getThumbnailUrl(final Elements elements) {
        return elements.stream()
            .filter(elem -> "thumbnailUrl".equals(elem.attr("itemprop")))
            .findFirst()
            .map(elem -> elem.attr("href"))
            .orElse("");
    }
}
