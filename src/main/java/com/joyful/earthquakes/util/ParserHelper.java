package com.joyful.earthquakes.util;

import com.joyful.earthquakes.model.LocationType;

import java.util.HashMap;
import java.util.Map;

import static com.joyful.earthquakes.model.LocationType.LOCATION;
import static com.joyful.earthquakes.model.LocationType.REGION;
import static java.lang.Character.isLetter;
import static java.lang.Character.isLowerCase;
import static java.util.Arrays.stream;

public final class ParserHelper {

    public static Map<LocationType, String> parseLocation(String fullLocation) {
        final HashMap<LocationType, String> resultMap = new HashMap<>();

        int separatorNum = fullLocation.indexOf(',');
        final String[] splitLocation = fullLocation.split(" ");

        String region =  separatorNum == -1
                ? splitLocation[0]
                : fullLocation.substring(0, separatorNum);

        String locationSeparator = separatorNum == -1 ? " " : "," + " ";

        String location = fullLocation.substring(region.length() + locationSeparator.length());

        if (hasNotCapitalizedChar(location)) {
            region = region + " " + location;
            location = region.split(" ")[0];
        }

        resultMap.put(LOCATION, location);
        resultMap.put(REGION, region);
        return resultMap;
    }

    private static boolean hasNotCapitalizedChar(String region) {
        return stream(region.split(" "))
                .anyMatch(word -> isLetter(word.charAt(0)) && isLowerCase(word.charAt(0)));

    }
}
