package com.joyful.earthquakes.model;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Value
@Builder
public class EarthEvent {
    Float magnitude;
    String country;
    String place;
    ZonedDateTime time;
}
