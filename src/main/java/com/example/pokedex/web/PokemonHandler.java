package com.example.pokedex.web;

import com.example.pokedex.config.EndpointsConfig;
import com.example.pokedex.web.dto.FunTranslationDto;
import com.example.pokedex.web.dto.HttpRuntimeException;
import com.example.pokedex.web.dto.Pokemon;
import com.example.pokedex.web.dto.PokemonSpecies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.Objects;
import java.util.logging.Logger;

@Service
public class PokemonHandler {

    private static final Logger logger = Logger.getLogger(PokemonHandler.class.getName());

    private final EndpointsConfig endpointsConfig;
    private final RestTemplate restTemplate;

    @Autowired
    public PokemonHandler(EndpointsConfig endpointsConfig, RestTemplate restTemplate) {
        this.endpointsConfig = endpointsConfig;
        this.restTemplate = restTemplate;
    }

    public Pokemon getPokemon(String name) {
        final URI uri = URI.create(endpointsConfig.getPokemonHost() + "/api/v2/pokemon-species/" + name);

        try {
            /*
                For our simple server with only two request a RestTemplate client is enough.
                In a larger project, I would centralize all third-party API handling logic within a dedicated service.
                Additionally, I would consider using a library to streamline and standardize the API interaction process
             */
            final PokemonSpecies speciesInformation = restTemplate.getForObject(uri, PokemonSpecies.class);
            Assert.notNull(speciesInformation, "Received empty response from pokemon API on retrieving species information");

            return FactoryBuilder.buildPokemon(speciesInformation);

        } catch (HttpClientErrorException e) {
            if (e.getStatusCode().is4xxClientError()) {
                boolean notFound = e.getStatusCode().value() == HttpStatus.NOT_FOUND.value();
                boolean badRequest = e.getStatusCode().value() == HttpStatus.BAD_REQUEST.value();

                if (notFound || badRequest) {
                    final String message = notFound ? "Pokemon not found" : e.getMessage();
                    throw new HttpRuntimeException(HttpStatus.valueOf(e.getStatusCode().value()), message, e);
                }
            }
            // An error different from 404 or 400 would most likely indicate an error with our implementation
            // It will be an unfair blame on the client, creating confusion
            throw new HttpRuntimeException(HttpStatus.INTERNAL_SERVER_ERROR, "Pokedex internal error", null);

        }

    }


    public Pokemon getPokemonTranslation(String name) {
        final Pokemon pokemon = getPokemon(name);
        final URI translationUri = getTranslationUri(pokemon);

        try {
            final FunTranslationDto translatedText = restTemplate.getForObject(translationUri, FunTranslationDto.class);
            Assert.notNull(translatedText, "Received empty response from translation server");

            if (translatedText.getTranslatedText().isPresent()) {
                pokemon.setDescription(translatedText.getTranslatedText().get());
            }

        } catch (Exception e) {
            // If translation fails it is not considered severe. Only log information and proceed with operations
            logger.info("Translation APIs responded with error " + e.getMessage());

        }

        return pokemon;
    }


    private URI getTranslationUri(Pokemon pokemon) {
        final String text = URLEncoder.encode(pokemon.getDescription(), Charset.defaultCharset());
        final String basePath = endpointsConfig.getTranslationHost() + "/translate/";

        if (pokemon.getIsLegendary() || Objects.equals(pokemon.getHabitat(), "cave")) {
            return  URI.create(basePath + "yoda.json?text=" + text);
        } else {
            return  URI.create(basePath + "shakespeare.json?text=" + text);
        }
    }

}
