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

//        final ZonedDateTime lastEventTime = sortedEvents.get(0).getTime();
//        final ZonedDateTime lastHourTime = lastEventTime.minusHours(1);
//        final ZonedDateTime prelastHourTime = lastEventTime.minusHours(2);
//        final ZonedDateTime lastDayTime = lastEventTime.minusDays(1);
//        final ZonedDateTime prelastDayTime = lastEventTime.minusDays(2);
//
//        final List<EarthEvent> prelastHourEvents = sortedEvents.stream()
//                .filter(event -> event.getTime().isAfter(prelastHourTime))
//                .filter(event -> event.getTime().isBefore(lastHourTime))
//                .collect(toList());
//
//        final List<ZonedDateTime> prelastDayEventTimeList = sortedEvents.stream()
//                .filter(event -> event.getTime().isAfter(prelastDayTime))
//                .filter(event -> event.getTime().isBefore(lastDayTime))
//                .map(EarthEvent::getTime)
//                .collect(toList());
//
//        final List<EarthEvent> lastHourEvents = sortedEvents.stream()
//                .filter(event -> event.getTime().isAfter(lastHourTime))
//                .collect(toList());
//
//        final List<ZonedDateTime> lastDayEventTimeList = sortedEvents.stream()
//                .filter(event -> event.getTime().isAfter(lastDayTime))
//                .map(EarthEvent::getTime)
//                .collect(toList());

//        ZonedDateTime prelastTime = null;
//        long preavgDiffMillis;
//        long presumMillis = 0L;
//        for (ZonedDateTime currentTime : prelastDayEventTimeList) {
//            if (prelastTime != null) {
//                presumMillis += currentTime.toEpochSecond() - prelastTime.toEpochSecond();
//            }
//            prelastTime = currentTime;
//        }
//        preavgDiffMillis = presumMillis / prelastDayEventTimeList.size();
//
//        ZonedDateTime predayLastTime = null;
//        long preavgDayDiffMillis;
//        long presumDayMillis = 0L;
//        for (ZonedDateTime dayCurrentTime : prelastDayEventTimeList) {
//            if (predayLastTime != null) {
//                presumDayMillis += dayCurrentTime.toEpochSecond() - predayLastTime.toEpochSecond();
//            }
//            predayLastTime = dayCurrentTime;
//        }
//        preavgDayDiffMillis = presumDayMillis / prelastDayEventTimeList.size();
//
//        ZonedDateTime lastTime = null;
//        long avgDiffMillis;
//        long sumMillis = 0L;
//        for (ZonedDateTime currentTime : lastDayEventTimeList) {
//            if (lastTime != null) {
//                sumMillis += currentTime.toEpochSecond() - lastTime.toEpochSecond();
//            }
//            lastTime = currentTime;
//        }
//        avgDiffMillis = sumMillis / lastDayEventTimeList.size();
//
//        ZonedDateTime dayLastTime = null;
//        long avgDayDiffMillis;
//        long sumDayMillis = 0L;
//        for (ZonedDateTime dayCurrentTime : lastDayEventTimeList) {
//            if (dayLastTime != null) {
//                sumDayMillis += dayCurrentTime.toEpochSecond() - dayLastTime.toEpochSecond();
//            }
//            dayLastTime = dayCurrentTime;
//        }
//        avgDayDiffMillis = sumDayMillis / lastDayEventTimeList.size();
//
//        long hourFactor = preavgDiffMillis - avgDiffMillis;
//        long dayFactor = preavgDayDiffMillis - avgDayDiffMillis;

//        long hourFactor = eventList.stream()
//                .filter(event -> event.getTime().isAfter())
//        long dayFactor = preavgDayDiffMillis - avgDayDiffMillis;


        return sortedEvents.stream()
                .map(earthEvent -> EarthEventReadDto.builder()
                        .magnitude(String.valueOf(earthEvent.getMagnitude()))
                        .fullLocation(earthEvent.getRegion() + earthEvent.getLocation())
                        .time(earthEvent.getTime().toString())
                        .hourFrequency(String.valueOf(getFrequency(earthEvent, sortedEvents, HOURS)))
                        .hourFactor(String.valueOf(getFactor(earthEvent, sortedEvents, HOURS)))
                        .dayFrequency(String.valueOf(getFrequency(earthEvent, sortedEvents, DAYS)))
                        .dayFactor(String.valueOf(getFactor(earthEvent, sortedEvents, DAYS)))
                        .build())
                .collect(toList());
    }

    private long getFactor(EarthEvent mainEvent, List<EarthEvent> eventList, TemporalUnit timeUnit) {
        final long oldFrequency = getFrequency(mainEvent, eventList, timeUnit, 2);
        final long currentFrequency = getFrequency(mainEvent, eventList, timeUnit, 1);
        return oldFrequency - currentFrequency;
    }

    private long getFrequency(EarthEvent mainEvent, List<EarthEvent> eventList, TemporalUnit timeUnit, int unitsNumber) {
        List<EarthEvent> lastEvents = eventList.stream()
                .filter(event -> event.getTime().isAfter(mainEvent.getTime().minus(unitsNumber, timeUnit)))
                .collect(toList());

        final long diffSum = lastEvents.stream()
                .mapToLong(EarthEvent::getEventTimeDiff)
                .sum();

        return diffSum / lastEvents.size();
    }

    private long getFrequency(EarthEvent mainEvent, List<EarthEvent> eventList, TemporalUnit timeUnit) {
        return getFrequency(mainEvent, eventList, timeUnit, 1);
    }
}
