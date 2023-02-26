package com.example.takehome.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class GraphqlConfig {
    @Value("${application.graphql.countryUrl}")
    private String countryGraphqlUrl;
}
