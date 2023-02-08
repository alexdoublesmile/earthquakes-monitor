package com.joyful.earthquakes.mapper;

import com.joyful.earthquakes.model.dto.EarthEventReadDto;
import com.joyful.earthquakes.model.entity.EarthEvent;
import org.springframework.stereotype.Component;

import java.time.temporal.TemporalUnit;
import java.util.List;

import static java.time.temporal.ChronoUnit.DAYS;
import static java.time.temporal.ChronoUnit.HOURS;
import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;

@Component
public class EarthEventMapper {

    public List<EarthEventReadDto> toDtoList(List<EarthEvent> eventList) {
        final List<EarthEvent> sortedEvents = eventList.stream()
                .sorted(comparing(EarthEvent::getTime).reversed())
                .collect(toList());

        return sortedEvents.stream()
                .map(earthEvent -> EarthEventReadDto.builder()
                        .magnitude(String.valueOf(earthEvent.getMagnitude()))
                        .fullLocation(earthEvent.getRegion() + earthEvent.getLocation())
                        .time(earthEvent.getTime().toString())
                        .hourFrequency(getFrequency(earthEvent, sortedEvents, HOURS) + "min")
                        .hourFactor(getFactor(earthEvent, sortedEvents, HOURS) + "min")
                        .dayFrequency(getFrequency(earthEvent, sortedEvents, DAYS) + "min")
                        .dayFactor(getFactor(earthEvent, sortedEvents, DAYS) + "min")
                        .build())
                .collect(toList());
    }

    private double getFactor(EarthEvent mainEvent, List<EarthEvent> eventList, TemporalUnit timeUnit) {
        final double oldFrequency = getFrequency(mainEvent, eventList, timeUnit, 2);
        final double currentFrequency = getFrequency(mainEvent, eventList, timeUnit, 1);
        return oldFrequency - currentFrequency;
    }

    private double getFrequency(EarthEvent mainEvent, List<EarthEvent> eventList, TemporalUnit timeUnit, int unitsNumber) {
        List<EarthEvent> lastEvents = eventList.stream()
                .filter(event -> event.getTime().isAfter(mainEvent.getTime().minus(unitsNumber, timeUnit)))
                .filter(event -> event.getTimeDiffSec() != null)
                .collect(toList());

        final double diffSum = lastEvents.stream()
                .mapToLong(EarthEvent::getTimeDiffSec)
                .sum();

        return diffSum / lastEvents.size() / 60;
    }

    private double getFrequency(EarthEvent mainEvent, List<EarthEvent> eventList, TemporalUnit timeUnit) {
        return getFrequency(mainEvent, eventList, timeUnit, 1);
    }
}
