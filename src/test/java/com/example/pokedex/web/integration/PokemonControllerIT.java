package com.example.pokedex.web.integration;

import com.example.pokedex.web.dto.Pokemon;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.net.URI;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class PokemonControllerIT {

    private static final TestRestTemplate TEMPLATE = new TestRestTemplate();

    @LocalServerPort
    private int port;

    @Test
    public void should_return_a_pokemon_information_when_requesting_by_name() {
        final RequestEntity<?> request = RequestEntity.get(
            buildUrl(port, "/pokemon/mew-two")
        ).build();

        final ResponseEntity<Pokemon> response = TEMPLATE.exchange(request, Pokemon.class);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    private URI buildUrl(int port, String path) {
        return URI.create("http://localhost:" + port + path);
    }
}