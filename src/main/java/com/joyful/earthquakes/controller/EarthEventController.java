package com.joyful.earthquakes.controller;

import com.joyful.earthquakes.service.EarthEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
@RequiredArgsConstructor
public class EarthEventController {
    private EarthEventService usgsEventService;
}
