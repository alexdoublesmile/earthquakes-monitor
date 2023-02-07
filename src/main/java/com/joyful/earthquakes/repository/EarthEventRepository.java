package com.joyful.earthquakes.repository;

import com.joyful.earthquakes.model.entity.EarthEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.ZonedDateTime;
import java.util.List;

public interface EarthEventRepository extends JpaRepository<EarthEvent, Long> {

    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN 'false' ELSE 'true' END FROM EarthEvent u WHERE u.time = ?1")
    Boolean notExists(ZonedDateTime time);

    List<EarthEvent> findAllByLocation(String location);
}
