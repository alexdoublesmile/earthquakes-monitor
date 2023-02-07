package com.joyful.earthquakes.scheduler;

import com.joyful.earthquakes.service.EarthEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class USGSParserScheduler {

    private final EarthEventService earthQuakesService;

    @Scheduled(cron = " */60 * * * * *")
    public void parseUSGS() {
        earthQuakesService.parseUSGS();
    }
}
