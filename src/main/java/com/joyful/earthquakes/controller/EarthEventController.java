package com.joyful.earthquakes.controller;

import com.joyful.earthquakes.model.dto.EarthEventReadDto;
import com.joyful.earthquakes.service.EarthEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Log4j2
@RestController
@RequestMapping("/api/v1/earth-events")
@RequiredArgsConstructor
public class EarthEventController {
    private EarthEventService earthEventService;

    @GetMapping
    public List<EarthEventReadDto> findByLocationName(@RequestParam String location) {
        return earthEventService.findByLocationName(location);
    }
}
