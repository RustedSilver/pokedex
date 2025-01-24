package com.example.pokedex.web.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Pokemon {
    private String name;
    private String description;
    private String habitat;
    private Boolean isLegendary;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getIsLegendary() {
        return isLegendary;
    }

    public void setIsLegendary(Boolean legendary) {
        isLegendary = legendary;
    }

    public String getHabitat() {
        return habitat;
    }

    public void setHabitat(String habitat) {
        this.habitat = habitat;
    }
}
