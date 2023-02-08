package com.joyful.earthquakes.service;

import com.joyful.earthquakes.mapper.EarthEventMapper;
import com.joyful.earthquakes.model.dto.EarthEventReadDto;
import com.joyful.earthquakes.model.entity.EarthEvent;
import com.joyful.earthquakes.repository.EarthEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@Log4j2
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EarthEventService {
    private final EarthEventMapper earthEventMapper;
    private final EarthEventRepository earthEventRepository;

    public List<EarthEventReadDto> findByLocationName(String location) {
        return earthEventMapper.toDtoList(earthEventRepository.findAllByLocation(location));
    }

    @Transactional
    public void saveAll(List<EarthEvent> events) {
        final ZonedDateTime maxTime = earthEventRepository.findMaxTime()
                .toLocalDateTime().atZone(ZoneId.systemDefault());
        events.forEach(event -> {
            if (maxTime.isBefore(event.getTime())) {
                earthEventRepository.save(event);
            }
        });
    }

    public Optional<Timestamp> findLastTimeByLocation(String location) {
        return Optional.ofNullable(earthEventRepository.findMaxTimeByLocation(location));
//        final List<EarthEvent> allByLocation = earthEventRepository.findAllByLocation(location);
//
//        return allByLocation.stream()
//                .filter(event -> event.getTime().isBefore(time))
//                .map(EarthEvent::getTime)
//                .max(ZonedDateTime::compareTo);
    }
}
