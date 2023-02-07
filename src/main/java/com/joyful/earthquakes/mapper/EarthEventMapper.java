package com.joyful.earthquakes.mapper;

import com.joyful.earthquakes.model.EarthEvent;
import org.jsoup.nodes.Element;

import java.time.ZonedDateTime;

public class EarthEventMapper {
    public EarthEvent mapToEarthEvent(Element htmlInfo) {

        final String title = htmlInfo.selectFirst("title").text();
        final String updated = htmlInfo.selectFirst("updated").text();

        final int firstCharNum = 2;
        final int firstSeparatorNum = title.indexOf('-');
        final int thirdSeparatorNum = title.indexOf(',');

        final String magnitude = title.substring(firstCharNum, firstSeparatorNum - 1);
        final String place = title.substring(firstSeparatorNum + 2, thirdSeparatorNum);
        final String country = title.substring(thirdSeparatorNum + 2);

        final EarthEvent earthEvent = EarthEvent.builder()
                .time(ZonedDateTime.parse(updated))
                .place(place)
                .magnitude(Float.parseFloat(magnitude))
                .country(country)
                .build();
        return earthEvent;
    }
}
