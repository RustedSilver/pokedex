package com.example.pokedex.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(value = "prefix.endpoints")
public class EndpointsConfig {
    private String pokemon;
    private String translation;

    public EndpointsConfig(String pokemonHost, String translationHost) {
        this.pokemon = pokemonHost;
        this.translation = translationHost;
    }

    public String getPokemon() {
        return pokemon;
    }

    public void setPokemon(String pokemon) {
        this.pokemon = pokemon;
    }

    public String getTranslation() {
        return translation;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }
}
