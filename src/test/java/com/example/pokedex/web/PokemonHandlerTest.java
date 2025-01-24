package com.example.pokedex.web;

import com.example.pokedex.config.EndpointsConfig;
import com.example.pokedex.helpers.TestUtils;
import com.example.pokedex.web.dto.HttpRuntimeException;
import com.example.pokedex.web.dto.PokemonSpecies;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.STRICT_STUBS)
class PokemonHandlerTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private EndpointsConfig config;

    @InjectMocks
    private PokemonHandler pokemonHandler;

    @AfterEach
    public void teardown() {
        Mockito.verifyNoMoreInteractions(restTemplate, config);
    }

    @Test
    public void get_pokemon_method_should_request_a_pokemon_from_pokeapi_server_only_once() throws IOException {
        // Arrange
        final PokemonSpecies pokemon = TestUtils.generateFakeSpecies();
        Mockito.when(config.getPokemonHost()).thenReturn("http://localhost:7000");

        Mockito.doAnswer(a -> {
            URI argument = a.getArgument(0);
            Assertions.assertTrue(argument.getPath().contains("/api/v2/pokemon-species/Pikachu"));

            return pokemon;
        }).when(restTemplate).getForObject(ArgumentMatchers.any(URI.class), ArgumentMatchers.eq(PokemonSpecies.class));

        // Execute
        pokemonHandler.getPokemon("Pikachu");

        // verify
        Mockito.verify(config, Mockito.times(1)).getPokemonHost();
        Mockito.verify(config, Mockito.times(0)).getTranslationHost();

        Mockito.verify(restTemplate, Mockito.times(1))
            .getForObject(
                ArgumentMatchers.any(URI.class), ArgumentMatchers.eq(PokemonSpecies.class)
            );

    }

    @Test
    public void get_pokemon_method_should_throw_a_HttpRuntimeException_with_code_400_when_bad_reqiest_error() throws IOException {
        // Arrange
        Mockito.when(config.getPokemonHost()).thenReturn("http://localhost:7000");

        Mockito
            .doThrow(TestUtils.buildClientException(400))
            .when(restTemplate)
            .getForObject(ArgumentMatchers.any(URI.class), ArgumentMatchers.eq(PokemonSpecies.class));

        // Execute
        try {
            pokemonHandler.getPokemon("Pikachu");

        } catch (HttpRuntimeException e) {
            // I am capturing the error as I need to verify the number of interactions and if the error codes are handled correctly
            Assertions.assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
            Assertions.assertEquals("400 BAD_REQUEST", e.getMessage()); // Default message
        }

        // verify
        Mockito.verify(config, Mockito.times(1)).getPokemonHost();
        Mockito.verify(config, Mockito.times(0)).getTranslationHost();

        Mockito.verify(restTemplate, Mockito.times(1))
            .getForObject(
                ArgumentMatchers.any(URI.class), ArgumentMatchers.eq(PokemonSpecies.class)
            );

    }

    @Test
    public void get_pokemon_method_should_throw_a_HttpRuntimeException_with_code_404_when_not_found_error() throws IOException {
        // Arrange
        Mockito.when(config.getPokemonHost()).thenReturn("http://localhost:7000");

        Mockito
            .doThrow(TestUtils.buildClientException(404))
            .when(restTemplate)
            .getForObject(ArgumentMatchers.any(URI.class), ArgumentMatchers.eq(PokemonSpecies.class));

        // Execute
        try {
            pokemonHandler.getPokemon("Pikachu");

        } catch (HttpRuntimeException e) {
            // I am capturing the error as I need to verify the number of interactions and if the error codes are handled correctly
            Assertions.assertEquals(HttpStatus.NOT_FOUND, e.getStatusCode());
            Assertions.assertEquals("Pokemon not found", e.getMessage()); // Default message
        }

        // verify
        Mockito.verify(config, Mockito.times(1)).getPokemonHost();
        Mockito.verify(config, Mockito.times(0)).getTranslationHost();

        Mockito.verify(restTemplate, Mockito.times(1))
            .getForObject(
                ArgumentMatchers.any(URI.class), ArgumentMatchers.eq(PokemonSpecies.class)
            );

    }

    @Test
    public void get_pokemon_method_should_throw_a_HttpRuntimeException_with_code_500_when_rest_api_responds_with_other_4xx_codes() throws IOException {
        // Arrange
        Mockito.when(config.getPokemonHost()).thenReturn("http://localhost:7000");

        Mockito
            .doThrow(TestUtils.buildClientException(415))
            .when(restTemplate)
            .getForObject(ArgumentMatchers.any(URI.class), ArgumentMatchers.eq(PokemonSpecies.class));

        // Execute
        try {
            pokemonHandler.getPokemon("Pikachu");

        } catch (HttpRuntimeException e) {
            // I am capturing the error as I need to verify the number of interactions and if the error codes are handled correctly
            Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, e.getStatusCode());
        }

        // verify
        Mockito.verify(config, Mockito.times(1)).getPokemonHost();
        Mockito.verify(config, Mockito.times(0)).getTranslationHost();

        Mockito.verify(restTemplate, Mockito.times(1))
            .getForObject(
                ArgumentMatchers.any(URI.class), ArgumentMatchers.eq(PokemonSpecies.class)
            );

    }

    @Test
    public void get_pokemon_method_should_throw_a_HttpRuntimeException_with_code_5xx_server_error() throws IOException {
        // Arrange
        Mockito.when(config.getPokemonHost()).thenReturn("http://localhost:7000");

        Mockito
            .doThrow(TestUtils.buildServerException(503))
            .when(restTemplate)
            .getForObject(ArgumentMatchers.any(URI.class), ArgumentMatchers.eq(PokemonSpecies.class));

        // Execute
        try {
            pokemonHandler.getPokemon("Pikachu");

        } catch (HttpServerErrorException e) {
            // I am capturing the error as I need to verify the number of interactions and if the error codes are handled correctly
            Assertions.assertEquals(HttpStatus.SERVICE_UNAVAILABLE, e.getStatusCode());
        }

        // verify
        Mockito.verify(config, Mockito.times(1)).getPokemonHost();
        Mockito.verify(config, Mockito.times(0)).getTranslationHost();

        Mockito.verify(restTemplate, Mockito.times(1))
            .getForObject(
                ArgumentMatchers.any(URI.class), ArgumentMatchers.eq(PokemonSpecies.class)
            );

    }

}