package com.example.pokedex.web;

import com.example.pokedex.config.EndpointsConfig;
import com.example.pokedex.web.dto.Pokemon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PokemonHandler {

    private final EndpointsConfig endpointsConfig;

    @Autowired
    public PokemonHandler(EndpointsConfig endpointsConfig) {
        this.endpointsConfig = endpointsConfig;
    }

    public Pokemon getPokemon(String name) {

        return new Pokemon();
    }
}
