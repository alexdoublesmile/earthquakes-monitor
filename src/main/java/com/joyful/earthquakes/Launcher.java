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

import static com.joyful.earthquakes.util.ConnectionConstants.*;
import static com.joyful.earthquakes.util.ParserConstants.*;

public class Launcher {
    public static void main(String[] args) throws IOException {
        final Document doc = Jsoup.connect(NO_SCRIPT_URL)
                .userAgent(BROWSER)
                .timeout(TIMEOUT)
                .referrer(REQUESTER)
                .get();

        final Element feed = doc.selectFirst(FEED_TAG);
        final Elements entries = feed.select(ENTRY_TAG);

        final EarthEventMapper earthEventMapper = new EarthEventMapper();

        List<EarthEvent> earthEventList = new ArrayList<>();
        for (Element entry : entries) {
            entry.getAllElements().forEach(e -> {
                if (e.selectFirst(TITLE_TAG) != null
                        && e.selectFirst(TIME_TAG) != null) {
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
