package com.demo.DBPBackend.conf;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:application.properties")
public class GoogleMapsConfig {

    @Value("${google.maps.api.key:}")
    private String apiKey;

    @Value("${google.maps.enabled:false}")
    private boolean enabled;

    @Value("${google.maps.default.zoom:12}")
    private int defaultZoom;

    @Value("${google.maps.default.lat:40.4168}")
    private double defaultLat;

    @Value("${google.maps.default.lng:-3.7038}")
    private double defaultLng;

    public String getApiKey() {
        return apiKey;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public int getDefaultZoom() {
        return defaultZoom;
    }

    public double getDefaultLat() {
        return defaultLat;
    }

    public double getDefaultLng() {
        return defaultLng;
    }
} 