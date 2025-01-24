package com.example.pokedex.web.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PokemonSpecies {

    private String name;

    @JsonProperty("is_legendary")
    private Boolean isLegendary;

    private PokemonHabitat habitat;

    @JsonProperty("flavor_text_entries")
    private List<FlavorText> flavorTexts;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PokemonHabitat {
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class FlavorText {
        @JsonProperty("flavor_text")
        private String flavorText;

        public String getFlavorText() {
            return flavorText;
        }

        public void setFlavorText(String flavorText) {
            this.flavorText = flavorText;
        }
    }


    public String getName() {
        return name;
    }

    public Boolean getIsLegendary() {
        return isLegendary;
    }

    public String getHabitat() {
        if (Objects.isNull(habitat)) {
            return null;
        }

        return habitat.getName();
    }

    public List<FlavorText> getFlavorTexts() {
        return flavorTexts;
    }

    public String getFirstDescription() {
        // Find first element if present. We do not want to perform transformation on the entire List.
        return flavorTexts.stream()
            .findFirst()
            .map(flavorText -> {
                return flavorText
                    .getFlavorText()
                    .replaceAll("[\\n\\f]", " "); // replace new lines with space
            })
            .orElse("");
    }
}
