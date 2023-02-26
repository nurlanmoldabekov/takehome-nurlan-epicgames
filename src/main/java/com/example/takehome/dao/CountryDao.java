package com.example.takehome.dao;

import com.example.takehome.model.Country;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Data
public class CountryDao {
    private Map<String, Country> countries;
}
