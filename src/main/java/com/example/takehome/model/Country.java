package com.example.takehome.model;

import lombok.Data;

@Data
public class Country {
    private String name;
    private String code;
    private Continent continent;
}
