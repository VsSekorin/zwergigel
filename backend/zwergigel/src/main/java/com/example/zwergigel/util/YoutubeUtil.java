package com.example.zwergigel.util;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.jsoup.Jsoup;

import java.net.URL;
import java.time.Duration;

@UtilityClass
public class YoutubeUtil {

    @SneakyThrows
    public Duration getDuration(final String url) {
        return Jsoup.parse(new URL(url), 10000).body()
            .getElementsByAttribute("itemprop").stream()
            .filter(a -> "duration".equals(a.attr("itemprop")))
            .map(a -> a.attr("content"))
            .findFirst()
            .map(Duration::parse)
            .orElse(Duration.ZERO);
    }
}
