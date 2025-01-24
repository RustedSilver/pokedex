package com.example.pokedex.web.integration;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.InputStream;

@RestController
@RequestMapping("api/v2/")
public class PokeapiMockController {

    @GetMapping("pokemon-species/{name}")
    public ResponseEntity<JsonNode> getCachedPokemonSpecies(String name) {

        //final InputStream inputStream = PokeapiMockController.class.getResource(name + "-species.json").getFile().getBytes();

        return null;
    }
}
