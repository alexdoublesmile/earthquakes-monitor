package com.joyful.earthquakes.model;

import lombok.Builder;
import lombok.Value;

import java.time.ZonedDateTime;

@Value
@Builder
public class EarthEvent {
    Integer magnitude;
    String country;
    String place;
    ZonedDateTime time;
}
