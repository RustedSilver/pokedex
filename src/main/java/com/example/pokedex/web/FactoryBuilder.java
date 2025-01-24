package com.example.pokedex.web;

import com.example.pokedex.web.dto.Pokemon;
import com.example.pokedex.web.dto.PokemonSpecies;

public class FactoryBuilder {

    public static Pokemon buildPokemon(PokemonSpecies species) {
        final Pokemon p = new Pokemon();
        p.setName(species.getName());
        p.setIsLegendary(species.getIsLegendary());
        p.setHabitat(species.getHabitat());
        p.setDescription(species.getFirstDescription());

        return p;
    }
}
