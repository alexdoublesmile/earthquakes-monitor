package com.joyful.earthquakes.service;

import com.joyful.earthquakes.mapper.EarthEventMapper;
import com.joyful.earthquakes.model.dto.EarthEventReadDto;
import com.joyful.earthquakes.model.entity.EarthEvent;
import com.joyful.earthquakes.repository.EarthEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
        events.forEach(event -> {
            if (earthEventRepository.notExists(event.getTime())) {
                earthEventRepository.save(event);
            }
        });
    }
}
