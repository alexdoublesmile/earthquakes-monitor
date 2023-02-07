package com.joyful.earthquakes.controller;

import com.joyful.earthquakes.model.dto.EarthEventReadDto;
import com.joyful.earthquakes.service.EarthEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Log4j2
@RestController
@RequiredArgsConstructor
public class EarthEventController {
    private EarthEventService earthEventService;

    public List<EarthEventReadDto> findByLocationName(String location) {
        return earthEventService.findByLocationName(location);
    }
}
