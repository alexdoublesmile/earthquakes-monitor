package com.joyful.earthquakes.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.ZonedDateTime;

import static javax.persistence.GenerationType.IDENTITY;

@Data
@Builder
@Entity
@Table(name = "usgs_event")
@NoArgsConstructor
@AllArgsConstructor
public class USGSEvent {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(name = "event_time")
    private ZonedDateTime time;

    private Double magnitude;

    private String region;
    private String location;
}
