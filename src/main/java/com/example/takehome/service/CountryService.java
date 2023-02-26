package com.example.takehome.service;

import com.example.takehome.dao.CountryDao;
import com.example.takehome.exception.BadRequestException;
import com.example.takehome.exception.NotFoundException;
import com.example.takehome.model.Country;
import com.example.takehome.model.dto.CountryApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class CountryService {
    private final CountryDao dao;

    public CountryApiResponse getSameContinentCountries(List<String> countryCodes) {

        var continent = validateAndGetContinent(countryCodes);
        var otherCountries = dao.getCountries().values().stream().filter(s -> s.getContinent().getName().equals(continent))
                .map(Country::getCode).filter(code -> !countryCodes.contains(code)).toList();
        return CountryApiResponse.builder().countries(countryCodes).name(continent).otherCountries(otherCountries).build();
    }

    /**
     * Validating incoming list. It should:
     * - have at least one code of a country
     * - have at least valid existing code
     * - provided codes should be from the same continent
     *
     * @param countryCodes List<String>
     * @return String
     */
    private String validateAndGetContinent(List<String> countryCodes) {
        if (countryCodes == null || countryCodes.isEmpty()) {
            throw new BadRequestException("Input countryCodes are null or empty");
        }

        for (String code : countryCodes) {
            if (!dao.getCountries().containsKey(code)) {
                log.warn("Country with a code = {} was not found", code);
                throw new NotFoundException("No country was found with code = " + code);
            }
        }
        var countryList = countryCodes.stream().map(dao.getCountries()::get).toList();
        var continents = countryList.stream().map(s -> s.getContinent().getName()).collect(Collectors.toSet());
        if (continents.size() > 1) {
            throw new BadRequestException("Only countries from the same continent are allowed");
        }
        var optionalContinent = continents.stream().findFirst();
        if (optionalContinent.isEmpty()) {
            throw new NotFoundException("No continent was found in the given countries");
        }
        return optionalContinent.get();
    }
}
