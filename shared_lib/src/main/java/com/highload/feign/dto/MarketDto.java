package com.highload.feign.dto;

import com.highload.feign.model.Person;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@RequiredArgsConstructor
public class MarketDto {
    private UUID marketId;
    private String country;
    private String city;
    private Long cost;
    private Set<Person> persons;
}

