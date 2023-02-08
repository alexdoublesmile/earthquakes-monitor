package com.joyful.earthquakes.service;

import com.joyful.earthquakes.mapper.USGSEventMapper;
import com.joyful.earthquakes.model.entity.EarthEvent;
import com.joyful.earthquakes.repository.EarthEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.*;

import static com.joyful.earthquakes.util.ConnectionConstants.*;
import static com.joyful.earthquakes.util.ConnectionConstants.REQUESTER;
import static com.joyful.earthquakes.util.ParserConstants.ENTRY_TAG;
import static com.joyful.earthquakes.util.ParserConstants.FEED_TAG;
import static java.util.Collections.singletonList;
import static java.util.Comparator.comparing;

@Log4j2
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class USGSParserService {
    private final EarthEventRepository earthEventRepository;
    private final USGSEventMapper usgsEventMapper;

    @Transactional
    public void parse() {
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

        final HashMap<String, List<EarthEvent>> locationNewEvents = new HashMap<>();
        for (Element entry : entries) {
            final EarthEvent event = usgsEventMapper.mapToEarthEvent(entry);

            locationNewEvents.merge(event.getLocation(), new ArrayList<>(singletonList(event)), (list1, list2) -> {
                list1.addAll(list2);
                list1.sort(comparing(EarthEvent::getTime));
                return list1;
            });
        }

        for (Map.Entry<String, List<EarthEvent>> next : locationNewEvents.entrySet()) {
            final List<EarthEvent> events = next.getValue();
            for (int i = 1; i < events.size(); i++) {
                final EarthEvent currentEvent = events.get(i);
                final long previousEventTime = events.get(i - 1).getTime().toEpochSecond();
                currentEvent.setEventTimeDiff(currentEvent.getTime().toEpochSecond() - previousEventTime);
            }
        }
        locationNewEvents.values().stream()
                .flatMap(List::stream)
                .forEach(event -> {
                    if (earthEventRepository.notExists(event.getTime())) {
                        earthEventRepository.save(event);
                    }
                });
    }
}
