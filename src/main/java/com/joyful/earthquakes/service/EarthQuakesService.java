package com.joyful.earthquakes.service;

import com.joyful.earthquakes.mapper.EarthEventMapper;
import com.joyful.earthquakes.model.EarthEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static com.joyful.earthquakes.util.ConnectionConstants.*;
import static com.joyful.earthquakes.util.ConnectionConstants.REQUESTER;
import static com.joyful.earthquakes.util.ParserConstants.*;
import static com.joyful.earthquakes.util.ParserConstants.TIME_TAG;
import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;

@Log4j2
@Service
@RequiredArgsConstructor
public class EarthQuakesService {

    private final EarthEventMapper earthEventMapper;

    public void parseUSGS() {
        final Document doc;
        try {
            doc = Jsoup.connect(NO_SCRIPT_URL)
                    .userAgent(BROWSER)
                    .timeout(TIMEOUT)
                    .referrer(REQUESTER)
                    .get();
        } catch (IOException e) {
            log.error("Can't parse USGS");
            throw new RuntimeException(e);
        }

        final Element feed = doc.selectFirst(FEED_TAG);
        final Elements entries = feed.select(ENTRY_TAG);


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
                .sorted(comparing(EarthEvent::getTime))
                .collect(toList());

        sortedEvents.forEach(System.out::println);
    }
}
