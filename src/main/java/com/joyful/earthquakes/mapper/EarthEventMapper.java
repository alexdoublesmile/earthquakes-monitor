package com.joyful.earthquakes.mapper;

import com.joyful.earthquakes.model.EarthEvent;
import com.joyful.earthquakes.model.LocationType;
import com.joyful.earthquakes.util.ParserHelper;
import org.jsoup.nodes.Element;

import java.util.Map;

import static java.lang.Float.parseFloat;
import static java.time.ZonedDateTime.parse;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.joining;

public class EarthEventMapper {
    public EarthEvent mapToEarthEvent(Element htmlInfo) {

        final String title = htmlInfo.selectFirst("title").text();
        final String updated = htmlInfo.selectFirst("updated").text();

        final String[] splitTitle = title.split(" ");

        String magnitude = splitTitle[1];
        String fullLocation = stream(splitTitle).skip(3).collect(joining(" "));

        final Map<LocationType, String> locationMap = ParserHelper.parseLocation(fullLocation);

        return EarthEvent.builder()
                .magnitude(parseFloat(magnitude))
                .region(locationMap.get(LocationType.REGION))
                .location(locationMap.get(LocationType.LOCATION))
                .time(parse(updated))
                .build();
    }
}
