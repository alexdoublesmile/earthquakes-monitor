package com.joyful.earthquakes.model;

import lombok.Builder;
import lombok.Data;
import lombok.Value;

import java.time.ZonedDateTime;

@Data
@Builder
public class EarthEvent {
    private Float magnitude;
    private String region;
    private String location;
    private ZonedDateTime time;
}
