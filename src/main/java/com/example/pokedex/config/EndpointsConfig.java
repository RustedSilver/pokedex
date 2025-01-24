package com.example.pokedex.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.Assert;

@ConfigurationProperties(value = "endpoints")
public class EndpointsConfig {
    private final String pokemon;
    private final String translations;

    public EndpointsConfig(String pokemon, String translations) {
        Assert.notNull(pokemon, "Pokemon endpoint not provided");
        Assert.notNull(translations, "Translation endpoint not provided");
        this.pokemon = pokemon;
        this.translations = translations;
    }

    public String getPokemonHost() {
        return pokemon;
    }

    public String getTranslationHost() {
        return translations;
    }


}
