package com.example.takehome;


import com.example.takehome.dao.CountryDao;
import com.example.takehome.exception.BadRequestException;
import com.example.takehome.exception.NotFoundException;
import com.example.takehome.model.Country;
import com.example.takehome.service.CountryService;
import com.example.takehome.service.DataCollectionService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Nurlan Moldabekov
 */
class CountryServiceTests {
    static DataCollectionService dataCollectionService;
    static CountryDao countryDao;
    static CountryService service;

    @BeforeAll
    static void init() {

        ObjectMapper objectMapper = new ObjectMapper();
        ClassPathResource staticDataResource = new ClassPathResource("countries.json");
        JsonNode json = null;
        try {
            json = objectMapper.readTree(staticDataResource.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        var listOfCountries = objectMapper.convertValue(json.at("/data/countries"), new TypeReference<List<Country>>() {
        });
        countryDao = new CountryDao();
        countryDao.setCountries(listOfCountries.stream().collect(Collectors.toMap(Country::getCode, s -> s)));
        service = new CountryService(countryDao);
    }

    /**
     * Test for North America
     */
    @Test
    void testPositiveNorthAmerica() {
        var res = service.getSameContinentCountries(List.of("CA", "US"));
        Assertions.assertEquals("North America", res.getName());
        Assertions.assertEquals(List.of("CA", "US"), res.getCountries());
        Assertions.assertEquals(List.of("PR", "AG", "AI", "AW", "BB", "BL", "BM", "BQ", "BS", "BZ", "SV", "SX", "CR",
                "TC", "CU", "CW", "TT", "DM", "DO",
                "VC", "VG", "VI", "GD", "GL", "GP", "GT", "HN", "HT", "JM", "KN", "KY", "LC", "MF", "MQ", "MS",
                "MX", "NI", "PA", "PM"), res.getOtherCountries());
    }

    /**
     * Test for South America
     */
    @Test
    void testPositiveSouthAmerica() {
        var res = service.getSameContinentCountries(List.of("GY", "PE"));
        Assertions.assertEquals("South America", res.getName());
        Assertions.assertEquals(List.of("GY", "PE"), res.getCountries());
        Assertions.assertEquals(List.of("PY", "AR", "BO", "BR", "SR", "CL", "CO", "EC", "UY", "VE", "FK", "GF")
                , res.getOtherCountries());
    }

    /**
     * Test for Europe
     */
    @Test
    void testPositiveEurope() {
        var res = service.getSameContinentCountries(List.of("GB", "ES"));
        Assertions.assertEquals("Europe", res.getName());
        Assertions.assertEquals(List.of("GB", "ES"), res.getCountries());
        Assertions.assertEquals(List.of("PT", "AD", "AL", "AT", "AX", "RO", "BA", "RS", "BE", "RU", "BG", "SE", "SI",
                "BY", "SJ", "SK", "SM", "CH", "CY", "CZ", "DE", "DK", "UA", "EE", "VA", "FI", "FO", "FR", "GG", "GI",
                "GR", "XK", "HR", "HU", "IE", "IM", "IS", "IT", "JE", "LI", "LT", "LU", "LV", "MC", "MD", "ME", "MK",
                "MT", "NL", "NO", "PL"), res.getOtherCountries());
    }

    /**
     * Test for Africa
     */
    @Test
    void testPositiveAfrica() {
        var res = service.getSameContinentCountries(List.of("ET", "GA"));
        Assertions.assertEquals("Africa", res.getName());
        Assertions.assertEquals(List.of("ET", "GA"), res.getCountries());
        Assertions.assertEquals(List.of("AO", "RE", "BF", "RW", "BI", "BJ", "SC", "SD", "BW", "SH", "SL", "SN", "SO",
                "SS", "CD", "ST", "CF", "CG", "CI", "SZ", "CM", "TD", "CV", "TG", "TN", "DJ", "TZ", "UG", "DZ", "EG",
                "EH", "ER", "GH", "GM", "GN", "GQ", "GW", "YT", "ZA", "ZM", "ZW", "KE", "KM", "LR", "LS", "LY", "MA",
                "MG", "ML", "MR", "MU", "MW", "MZ", "NA", "NE", "NG"), res.getOtherCountries());
    }

    /**
     * Test for Asia
     */
    @Test
    void testPositiveAsia() {
        var res = service.getSameContinentCountries(List.of("GE", "HK"));
        Assertions.assertEquals("Asia", res.getName());
        Assertions.assertEquals(List.of("GE", "HK"), res.getCountries());
        Assertions.assertEquals(List.of("PS", "QA", "AE", "AF", "AM", "AZ", "BD", "BH", "BN", "SA", "BT", "SG", "CC",
                "SY", "CN", "TH", "CX", "TJ", "TM", "TR", "TW", "UZ", "VN", "YE", "ID", "IL", "IN", "IO", "IQ", "IR",
                "JO", "JP", "KG", "KH", "KP", "KR", "KW", "KZ", "LA", "LB", "LK", "MM", "MN", "MO", "MV", "MY", "NP",
                "OM", "PH", "PK"), res.getOtherCountries());
    }

    /**
     * Test for Africa
     */
    @Test
    void testNegativeBadRequest() {
        Exception exception = Assertions.assertThrows(BadRequestException.class, () -> {
            service.getSameContinentCountries(null);
        });
        String expectedMessage = "Input countryCodes are null or empty";
        String actualMessage = exception.getMessage();

        Assertions.assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void testNegativeBadRequestTwo() {
        Exception exception = Assertions.assertThrows(BadRequestException.class, () ->
                service.getSameContinentCountries(Collections.emptyList()));
        String expectedMessage = "Input countryCodes are null or empty";
        String actualMessage = exception.getMessage();

        Assertions.assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void testNegativeCountryNotFound() {
        Exception exception = Assertions.assertThrows(NotFoundException.class, () -> {
            service.getSameContinentCountries(List.of("NNN", "US"));
        });
        String expectedMessage = "No country was found with code = NNN";
        String actualMessage = exception.getMessage();

        Assertions.assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void testNegativeDifferentContinents() {
        Exception exception = Assertions.assertThrows(BadRequestException.class, () -> {
            service.getSameContinentCountries(List.of("CA", "HK"));
        });
        String expectedMessage = "Only countries from the same continent are allowed";
        String actualMessage = exception.getMessage();

        Assertions.assertEquals(expectedMessage, actualMessage);
    }
}
