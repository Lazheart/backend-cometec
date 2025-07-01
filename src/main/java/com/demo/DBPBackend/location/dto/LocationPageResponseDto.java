package com.demo.DBPBackend.location.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Data
public class LocationPageResponseDto {
    
    private List<GoogleMapsLocationDto> locations;
    private int currentPage;
    private int totalPages;
    private long totalElements;
    private boolean hasNext;
    private boolean hasPrevious;
    
    public LocationPageResponseDto(List<GoogleMapsLocationDto> locations, int currentPage, 
                                 int totalPages, long totalElements, boolean hasNext, boolean hasPrevious) {
        this.locations = locations;
        this.currentPage = currentPage;
        this.totalPages = totalPages;
        this.totalElements = totalElements;
        this.hasNext = hasNext;
        this.hasPrevious = hasPrevious;
    }
} 