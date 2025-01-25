package com.example.pokedex.web.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Objects;
import java.util.Optional;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FunTranslationDto {
    private Contents contents;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Contents {
        private String translated;

        public String getTranslated() {
            return translated;
        }
    }

    public Contents getContents() {
        return contents;
    }

    public Optional<String> getTranslatedText() {
        return Optional.ofNullable(contents).map(Contents::getTranslated);
    }

}
