package com.example.takehome.model.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CountryApiResponse {
    private String name;
    private List<String> countries;
    private List<String> otherCountries;
}
