package com.example.pokedex.web;

import com.example.pokedex.config.EndpointsConfig;
import com.example.pokedex.web.dto.Pokemon;
import com.example.pokedex.web.dto.PokemonSpecies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@Service
public class PokemonHandler {

    private final EndpointsConfig endpointsConfig;
    private final RestTemplate restTemplate;

    @Autowired
    public PokemonHandler(EndpointsConfig endpointsConfig) {
        this.endpointsConfig = endpointsConfig;
        restTemplate = new RestTemplate();
    }

    public Pokemon getPokemon(String name) {
        final URI uri = URI.create(endpointsConfig.getPokemonHost() + "/api/v2/pokemon-species/" + name);

        final PokemonSpecies speciesInformation = restTemplate.getForObject(uri, PokemonSpecies.class);
        Assert.notNull(speciesInformation, "Received empty response from pokemon API on retrieving species information");

        return FactoryBuilder.buildPokemon(speciesInformation);
    }

}
