package com.example.takehome.service;

import com.example.takehome.config.GraphqlConfig;
import com.example.takehome.dao.CountryDao;
import com.example.takehome.model.Country;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Nurlan
 * I decided to store Map of countries in the in-memory bean instead of any database just for the test purpose
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class DataCollectionService {
    public final GraphqlConfig graphqlConfig;
    public final ObjectMapper objectMapper;
    public final CountryDao countryDao;

    @PostConstruct
    public void initCountries() {
        this.countryDao.setCountries(collectCountries());
    }

    public Map<String, Country> collectCountries() {
        String query = "{\"query\":\"{\\n  countries{\\n    name, code, continent{name}\\n  }\\n}\",\"variables\":{}}";
        var headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> request =
                new HttpEntity<>(query, headers);

        String res =
                restTemplate.postForObject(graphqlConfig.getCountryGraphqlUrl(), request, String.class);
        JsonNode json = null;
        try {
            json = objectMapper.readTree(res);
        } catch (JsonProcessingException e) {
            log.warn("Countries weren't initialized");
            return Collections.emptyMap();
        }
        var listOfCountries = objectMapper.convertValue(json.at("/data/countries"), new TypeReference<List<Country>>() {
        });
        return listOfCountries.stream().collect(Collectors.toMap(Country::getCode, s -> s));
    }
}
