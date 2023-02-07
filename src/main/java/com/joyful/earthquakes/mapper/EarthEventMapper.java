package com.joyful.earthquakes.mapper;

import com.joyful.earthquakes.model.dto.EarthEventReadDto;
import com.joyful.earthquakes.model.entity.EarthEvent;

import java.time.ZonedDateTime;
import java.util.List;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;

public class EarthEventMapper {

    public List<EarthEventReadDto> toDtoList(List<EarthEvent> eventList) {
        final List<EarthEvent> sortedEvents = eventList.stream()
                .sorted(comparing(EarthEvent::getTime).reversed())
                .collect(toList());

        final ZonedDateTime lastEventTime = sortedEvents.get(0).getTime();
        final ZonedDateTime lastHourTime = lastEventTime.minusHours(1);
        final ZonedDateTime prelastHourTime = lastEventTime.minusHours(2);
        final ZonedDateTime lastDayTime = lastEventTime.minusDays(1);
        final ZonedDateTime prelastDayTime = lastEventTime.minusDays(2);

        final List<EarthEvent> prelastHourEvents = sortedEvents.stream()
                .filter(event -> event.getTime().isAfter(prelastHourTime))
                .filter(event -> event.getTime().isBefore(lastHourTime))
                .collect(toList());

        final List<ZonedDateTime> prelastDayEventTimeList = sortedEvents.stream()
                .filter(event -> event.getTime().isAfter(prelastDayTime))
                .filter(event -> event.getTime().isBefore(lastDayTime))
                .map(EarthEvent::getTime)
                .collect(toList());

        final List<EarthEvent> lastHourEvents = sortedEvents.stream()
                .filter(event -> event.getTime().isAfter(lastHourTime))
                .collect(toList());

        final List<ZonedDateTime> lastDayEventTimeList = sortedEvents.stream()
                .filter(event -> event.getTime().isAfter(lastDayTime))
                .map(EarthEvent::getTime)
                .collect(toList());

        ZonedDateTime prelastTime = null;
        long preavgDiffMillis;
        long presumMillis = 0L;
        for (ZonedDateTime currentTime : prelastDayEventTimeList) {
            if (prelastTime != null) {
                presumMillis += currentTime.toEpochSecond() - prelastTime.toEpochSecond();
            }
            prelastTime = currentTime;
        }
        preavgDiffMillis = presumMillis / prelastDayEventTimeList.size();

        ZonedDateTime predayLastTime = null;
        long preavgDayDiffMillis;
        long presumDayMillis = 0L;
        for (ZonedDateTime dayCurrentTime : prelastDayEventTimeList) {
            if (predayLastTime != null) {
                presumDayMillis += dayCurrentTime.toEpochSecond() - predayLastTime.toEpochSecond();
            }
            predayLastTime = dayCurrentTime;
        }
        preavgDayDiffMillis = presumDayMillis / prelastDayEventTimeList.size();

        ZonedDateTime lastTime = null;
        long avgDiffMillis;
        long sumMillis = 0L;
        for (ZonedDateTime currentTime : lastDayEventTimeList) {
            if (lastTime != null) {
                sumMillis += currentTime.toEpochSecond() - lastTime.toEpochSecond();
            }
            lastTime = currentTime;
        }
        avgDiffMillis = sumMillis / lastDayEventTimeList.size();

        ZonedDateTime dayLastTime = null;
        long avgDayDiffMillis;
        long sumDayMillis = 0L;
        for (ZonedDateTime dayCurrentTime : lastDayEventTimeList) {
            if (dayLastTime != null) {
                sumDayMillis += dayCurrentTime.toEpochSecond() - dayLastTime.toEpochSecond();
            }
            dayLastTime = dayCurrentTime;
        }
        avgDayDiffMillis = sumDayMillis / lastDayEventTimeList.size();

        long hourFactor = preavgDiffMillis - avgDiffMillis;
        long dayFactor = preavgDayDiffMillis - avgDayDiffMillis;

        return sortedEvents.stream()
                .map(earthEvent -> EarthEventReadDto.builder()
                        .magnitude(String.valueOf(earthEvent.getMagnitude()))
                        .fullLocation(earthEvent.getRegion() + earthEvent.getLocation())
                        .time(earthEvent.getTime().toString())
                        .hourFrequency(String.valueOf(avgDiffMillis))
                        .hourFactor(String.valueOf(hourFactor))
                        .dayFrequency(String.valueOf(avgDayDiffMillis))
                        .dayFactor(String.valueOf(dayFactor))
                        .build())
                .collect(toList());
    }
}
