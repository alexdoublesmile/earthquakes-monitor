package com.joyful.earthquakes.service;

import com.joyful.earthquakes.mapper.EarthEventMapper;
import com.joyful.earthquakes.mapper.USGSEventMapper;
import com.joyful.earthquakes.model.dto.EarthEventReadDto;
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
import java.util.List;
import java.util.stream.Collectors;

import static com.joyful.earthquakes.util.ConnectionConstants.*;
import static com.joyful.earthquakes.util.ParserConstants.ENTRY_TAG;
import static com.joyful.earthquakes.util.ParserConstants.FEED_TAG;
import static java.util.stream.Collectors.toList;

@Log4j2
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EarthEventService {

    private final USGSEventMapper usgsEventMapper;
    private final EarthEventMapper earthEventMapper;
    private final EarthEventRepository earthEventRepository;

    @Transactional
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

        for (Element entry : entries) {
            final EarthEvent event = usgsEventMapper.mapToEarthEvent(entry);
            if (earthEventRepository.notExists(event.getTime())) {
                earthEventRepository.save(event);
            }
        }
    }

    public List<EarthEventReadDto> findByLocationName(String location) {
        return earthEventMapper.toDtoList(earthEventRepository.findAllByLocation(location));
    }
}
