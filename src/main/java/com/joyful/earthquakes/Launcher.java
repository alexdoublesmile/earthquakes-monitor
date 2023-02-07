package com.joyful.earthquakes;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class Launcher {
    public static void main(String[] args) throws IOException {
        String noscriptUrl = "https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/2.5_day.atom";

        final Document doc = Jsoup.connect(noscriptUrl)
                .userAgent("Mozilla")
                .timeout(5000)
                .referrer("https://google.com")
                .get();

    }
}
