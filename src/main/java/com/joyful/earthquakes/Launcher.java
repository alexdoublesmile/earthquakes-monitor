package com.joyful.earthquakes;

import com.joyful.earthquakes.mapper.EarthEventMapper;
import com.joyful.earthquakes.model.EarthEvent;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Launcher {
    public static void main(String[] args) throws IOException {
        String noscriptUrl = "https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/2.5_day.atom";

        final Document doc = Jsoup.connect(noscriptUrl)
                .userAgent("Mozilla")
                .timeout(5000)
                .referrer("https://google.com")
                .get();

        final Element feed = doc.selectFirst("feed");
        final Elements entries = feed.select("entry");

        final EarthEventMapper earthEventMapper = new EarthEventMapper();

        List<EarthEvent> earthEventList = new ArrayList<>();
        for (Element entry : entries) {
            entry.getAllElements().forEach(e -> {
                if (e.selectFirst("title") != null && e.selectFirst("updated") != null) {
                    earthEventList.add(earthEventMapper.mapToEarthEvent(entry));
                }
            });
        }

        final List<EarthEvent> sortedEvents = earthEventList.stream()
                .sorted(Comparator.comparingDouble(EarthEvent::getMagnitude))
                .collect(Collectors.toList());

        sortedEvents.forEach(System.out::println);
    }
}
