package com.example.pokedex.web;

import com.example.pokedex.config.EndpointsConfig;
import com.example.pokedex.helpers.TestUtils;
import com.example.pokedex.web.dto.FunTranslationDto;
import com.example.pokedex.web.dto.Pokemon;
import com.example.pokedex.web.dto.PokemonSpecies;
import com.example.pokedex.web.dto.TranslationRequestBody;
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
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.STRICT_STUBS)
public class PokemonHandlerTranslationTest {

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
    public void get_translation_for_rare_pokemon_should_return_a_pokemon_with_yoda_description() throws IOException {
        // Arrange
        final PokemonSpecies pokemon = TestUtils.generateFakeSpecies("mewtwo", "rare", true);
        Mockito.when(config.getPokemonHost()).thenReturn("http://localhost:7000");
        Mockito.when(config.getTranslationHost()).thenReturn("http://localhost:8000");

        Mockito
            .doReturn(pokemon)
            .when(restTemplate)
            .getForObject(ArgumentMatchers.any(URI.class), ArgumentMatchers.eq(PokemonSpecies.class));

        Mockito
            .doAnswer(a -> {
                final URI translatedUri = a.getArgument(0);
                Assertions.assertEquals("http://localhost:8000/translate/yoda.json", translatedUri.toString());

                return TestUtils.generateFakeTranslation();
            })
            .when(restTemplate).postForObject(
                ArgumentMatchers.any(URI.class),
                ArgumentMatchers.any(TranslationRequestBody.class),
                ArgumentMatchers.eq(FunTranslationDto.class)
            );

        // Execute
        pokemonHandler.getPokemonTranslation(pokemon.getName());

        // verify
        Mockito.verify(config, Mockito.times(1)).getPokemonHost();
        Mockito.verify(config, Mockito.times(1)).getTranslationHost();



        Mockito.verify(restTemplate, Mockito.times(1))
            .postForObject(
                ArgumentMatchers.any(URI.class),
                ArgumentMatchers.any(TranslationRequestBody.class),
                ArgumentMatchers.eq(FunTranslationDto.class)
            );

        Mockito.verify(restTemplate, Mockito.times(1))
            .getForObject(
                ArgumentMatchers.any(URI.class), ArgumentMatchers.eq(PokemonSpecies.class)
            );

    }


    @Test
    public void get_translation_for_cave_pokemon_should_return_a_pokemon_with_yoda_description() throws IOException {
        // Arrange
        final PokemonSpecies pokemon = TestUtils.generateFakeSpecies("Onix", "cave", false);
        Mockito.when(config.getPokemonHost()).thenReturn("http://localhost:7000");
        Mockito.when(config.getTranslationHost()).thenReturn("http://localhost:8000");

        Mockito
            .doReturn(pokemon)
            .when(restTemplate)
            .getForObject(ArgumentMatchers.any(URI.class), ArgumentMatchers.eq(PokemonSpecies.class));

        Mockito
            .doAnswer(a -> {
                final URI translatedUri = a.getArgument(0);
                Assertions.assertEquals("http://localhost:8000/translate/yoda.json", translatedUri.toString());

                return TestUtils.generateFakeTranslation();
            })
            .when(restTemplate).postForObject(
                ArgumentMatchers.any(URI.class),
                ArgumentMatchers.any(TranslationRequestBody.class),
                ArgumentMatchers.eq(FunTranslationDto.class)
            );

        // Execute
        Pokemon result = pokemonHandler.getPokemonTranslation(pokemon.getName());

        Assertions.assertNotEquals(pokemon.getFirstDescription(), result.getDescription());

        // verify
        Mockito.verify(config, Mockito.times(1)).getPokemonHost();
        Mockito.verify(config, Mockito.times(1)).getTranslationHost();



        Mockito.verify(restTemplate, Mockito.times(1))
            .postForObject(
                ArgumentMatchers.any(URI.class),
                ArgumentMatchers.any(TranslationRequestBody.class),
                ArgumentMatchers.eq(FunTranslationDto.class)
            );

        Mockito.verify(restTemplate, Mockito.times(1))
            .getForObject(
                ArgumentMatchers.any(URI.class), ArgumentMatchers.eq(PokemonSpecies.class)
            );

    }


    @Test
    public void get_translation_for_non_cave_or_legendary_pokemon_should_return_a_pokemon_with_shakespeare_description() throws IOException {
        // Arrange
        final PokemonSpecies pokemon = TestUtils.generateFakeSpecies("Bulbasar", "grass", false);
        Mockito.when(config.getPokemonHost()).thenReturn("http://localhost:7000");
        Mockito.when(config.getTranslationHost()).thenReturn("http://localhost:8000");

        Mockito
            .doReturn(pokemon)
            .when(restTemplate)
            .getForObject(ArgumentMatchers.any(URI.class), ArgumentMatchers.eq(PokemonSpecies.class));

        Mockito
            .doAnswer(a -> {
                final URI translatedUri = a.getArgument(0);
                // Validate is a shakespeare translation
                Assertions.assertEquals("http://localhost:8000/translate/shakespeare", translatedUri.toString());

                return TestUtils.generateFakeTranslation();
            })
            .when(restTemplate).postForObject(
                ArgumentMatchers.any(URI.class),
                ArgumentMatchers.any(TranslationRequestBody.class),
                ArgumentMatchers.eq(FunTranslationDto.class)
            );

        // Execute
        Pokemon result = pokemonHandler.getPokemonTranslation(pokemon.getName());

        Assertions.assertNotEquals(pokemon.getFirstDescription(), result.getDescription());

        // verify
        Mockito.verify(config, Mockito.times(1)).getPokemonHost();
        Mockito.verify(config, Mockito.times(1)).getTranslationHost();



        Mockito.verify(restTemplate, Mockito.times(1))
            .postForObject(
                ArgumentMatchers.any(URI.class),
                ArgumentMatchers.any(TranslationRequestBody.class),
                ArgumentMatchers.eq(FunTranslationDto.class)
            );

        Mockito.verify(restTemplate, Mockito.times(1))
            .getForObject(
                ArgumentMatchers.any(URI.class), ArgumentMatchers.eq(PokemonSpecies.class)
            );

    }

    @Test
    public void get_translation_on_error_response_should_return_default_description() throws IOException {
        // Arrange
        final PokemonSpecies pokemon = TestUtils.generateFakeSpecies("Bulbasar", "grass", false);
        Mockito.when(config.getPokemonHost()).thenReturn("http://localhost:7000");
        Mockito.when(config.getTranslationHost()).thenReturn("http://localhost:8000");

        Mockito
            .doReturn(pokemon)
            .when(restTemplate)
            .getForObject(ArgumentMatchers.any(URI.class), ArgumentMatchers.eq(PokemonSpecies.class));

        Mockito
            .doThrow(TestUtils.buildServerException(400))
            .when(restTemplate).postForObject(
                ArgumentMatchers.any(URI.class),
                ArgumentMatchers.any(TranslationRequestBody.class),
                ArgumentMatchers.eq(FunTranslationDto.class)
            );

        // Execute
        Pokemon result = pokemonHandler.getPokemonTranslation(pokemon.getName());

        Assertions.assertEquals(pokemon.getFirstDescription(), result.getDescription());

        // verify
        Mockito.verify(config, Mockito.times(1)).getPokemonHost();
        Mockito.verify(config, Mockito.times(1)).getTranslationHost();



        Mockito.verify(restTemplate, Mockito.times(1))
            .postForObject(
                ArgumentMatchers.any(URI.class),
                ArgumentMatchers.any(TranslationRequestBody.class),
                ArgumentMatchers.eq(FunTranslationDto.class)
            );

        Mockito.verify(restTemplate, Mockito.times(1))
            .getForObject(
                ArgumentMatchers.any(URI.class), ArgumentMatchers.eq(PokemonSpecies.class)
            );

    }

}
