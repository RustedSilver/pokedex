package com.example.pokedex.web.integration;

import com.example.pokedex.web.dto.ErrorMessage;
import com.example.pokedex.web.dto.Pokemon;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.net.URI;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, properties = {
    "server.port=6000",
    "endpoints.pokemon=http://localhost:6000",
    "endpoint.translations=http://localhost:6000"
})
@DirtiesContext
class PokemonControllerIT {

    private static final TestRestTemplate TEMPLATE = new TestRestTemplate();

    @LocalServerPort
    private int port;

    @Test
    public void should_return_a_pokemon_information_when_requesting_by_name() {
        // Arrange
        final RequestEntity<?> request = RequestEntity.get(
            buildUrl(port, "/pokemon/mewtwo")
        ).build();

        // Execute
        final ResponseEntity<Pokemon> response = TEMPLATE.exchange(request, Pokemon.class);

        // Assert
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());

        final Pokemon pokemon = response.getBody();

        Assertions.assertEquals("mewtwo", pokemon.getName());
        Assertions.assertEquals("It was created by a scientist after years of horrific gene splicing and DNA engineering experiments.", pokemon.getDescription());
        Assertions.assertEquals("rare", pokemon.getHabitat());
        Assertions.assertEquals(Boolean.TRUE, pokemon.getIsLegendary());
    }

    /**
     * Verify Client Errors are handled correctly
     */
    @Test
    public void should_return_404_not_found_when_pokemon_is_not_found() {
        // Arrange
        final RequestEntity<?> request = RequestEntity.get(
            buildUrl(port, "/pokemon/xyz")
        ).build();

        // Execute
        final ResponseEntity<ErrorMessage> response = TEMPLATE.exchange(request, ErrorMessage.class);

        // Assert
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());

        final ErrorMessage e = response.getBody();

        Assertions.assertEquals("Pokemon not found", e.message());
    }

    @Test
    public void should_return_a_pokemon_information_translated_as_yoda_when_requesting_rare_pokemon() {
        // Arrange
        final RequestEntity<?> request = RequestEntity.get(
            buildUrl(port, "/pokemon/translated/mewtwo")
        ).build();

        // Execute
        final ResponseEntity<Pokemon> response = TEMPLATE.exchange(request, Pokemon.class);

        // Assert
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());

        final Pokemon pokemon = response.getBody();

        Assertions.assertEquals("mewtwo", pokemon.getName());
        Assertions.assertEquals("Created by a scientist after years of horrific gene splicing and dna engineering experiments, it was.", pokemon.getDescription());
        Assertions.assertEquals("rare", pokemon.getHabitat());
        Assertions.assertEquals(Boolean.TRUE, pokemon.getIsLegendary());
    }

    private URI buildUrl(int port, String path) {
        return URI.create("http://localhost:" + port + path);
    }
}