package com.example.zwergigel.util;

import com.example.zwergigel.dto.VideoData;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.jsoup.Jsoup;

import java.net.URL;
import java.time.Duration;

@UtilityClass
public class YoutubeUtil {

    @SneakyThrows
    public Long getDuration(final String url) {
        return Jsoup.parse(new URL(url), 10000).body()
            .getElementsByAttribute("itemprop").stream()
            .filter(a -> "duration".equals(a.attr("itemprop")))
            .map(a -> a.attr("content"))
            .findFirst()
            .map(Duration::parse)
            .map(Duration::getSeconds)
            .orElse(0L);
    }

    @SneakyThrows
    public VideoData getData(final String url) {
        final VideoData data = new VideoData();
        Jsoup.parse(new URL(url), 10000).body()
            .getElementsByAttribute("itemprop").forEach(elem -> {
                if ("name".equals(elem.attr("itemprop"))) {
                    data.setName(elem.attr("content"));
                }
                if ("duration".equals(elem.attr("itemprop"))) {
                    data.setDuration(Duration.parse(elem.attr("content")).getSeconds());
                }
                if ("thumbnailUrl".equals(elem.attr("itemprop"))) {
                    data.setThumbnailUrl(elem.attr("href"));
                }
            });
        return data;
    }
}
