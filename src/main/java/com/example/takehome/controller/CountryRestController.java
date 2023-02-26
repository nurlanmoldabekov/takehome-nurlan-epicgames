package com.example.takehome.controller;

import com.example.takehome.model.dto.CountryApiResponse;
import com.example.takehome.service.CountryService;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(CountryRestController.RESOURCE_PATH)
public class CountryRestController {
    public static final String RESOURCE_KEY = "/country";
    public static final String RESOURCE_PATH = ApiConstants.API_V1 + RESOURCE_KEY;

    private final CountryService service;

    @GetMapping("/continent")
    public ResponseEntity<CountryApiResponse> getContinent(@RequestParam @NotEmpty List<String> countries) {
        return ResponseEntity.ok(service.getSameContinentCountries(countries));
    }
}
