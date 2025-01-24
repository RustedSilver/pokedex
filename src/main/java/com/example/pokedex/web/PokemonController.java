package com.example.pokedex.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("pokemon")
public class PokemonController {

    private final PokemonHandler handler;

    @Autowired
    public PokemonController(PokemonHandler handler) {
        this.handler = handler;
    }

    @GetMapping("{name}")
    public ResponseEntity<?> getPokemon(@PathVariable("name") String name) {
        return ResponseEntity.ok(handler.getPokemon(name));
    }
}
