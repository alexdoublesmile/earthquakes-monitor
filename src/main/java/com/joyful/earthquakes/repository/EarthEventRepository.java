package com.joyful.earthquakes.repository;

import com.joyful.earthquakes.model.USGSEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.ZonedDateTime;

public interface EarthEventRepository extends JpaRepository<USGSEvent, Long> {

    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN 'false' ELSE 'true' END FROM USGSEvent u WHERE u.time = ?1")
    Boolean notExists(ZonedDateTime time);
}
