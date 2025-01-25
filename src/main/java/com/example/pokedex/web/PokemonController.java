package com.example.pokedex.web;

import com.example.pokedex.web.dto.ErrorMessage;
import com.example.pokedex.web.dto.Pokemon;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    /**
     * Get Pokemon information
     * @param name pokemon name
     * @return Pokemon basic information
     */
    @Operation(
        summary = "Get pokemon information",
        description = "By passing a name as path variable it will download basic pokemon information"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retried pokemon",
            content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = Pokemon.class)) }
        ),
        @ApiResponse(responseCode = "404", description = "Pokemon not found",
            content = {
                @Content(schema = @Schema(implementation = ErrorMessage.class))
            })
    })
    @GetMapping("{name}")
    public ResponseEntity<Pokemon> getPokemon(@PathVariable("name") String name) {
        return ResponseEntity.ok(handler.getPokemon(name));
    }

    /**
     * Get Pokemon Translations
     * @param name pokemon name
     * @return Pokemon information with translated description
     */
    @Operation(
        summary = "Get pokemon fun translation",
        description = "By passing a name as path variable it will download basic pokemon information with translated description"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retried pokemon",
            content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = Pokemon.class)) }
        ),
        @ApiResponse(responseCode = "404", description = "Pokemon not found",
            content = {
                @Content(schema = @Schema(implementation = ErrorMessage.class))
            })
    })
    @GetMapping("translated/{name}")
    public ResponseEntity<Pokemon> getPokemonTranslation(@PathVariable("name") String name) {
        return ResponseEntity.ok(handler.getPokemonTranslation(name));
    }
}
