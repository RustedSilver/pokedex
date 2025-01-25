package com.example.pokedex.helpers;

import com.example.pokedex.web.dto.FunTranslationDto;
import com.example.pokedex.web.dto.PokemonSpecies;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import java.io.IOException;
import java.nio.charset.Charset;

public class TestUtils {

    public static final ObjectMapper MAPPER = new ObjectMapper();

    public static PokemonSpecies generateFakeSpecies() throws IOException {
        return MAPPER.readValue("""
            {
              "name": "Pikachu",
              "is_legendary": false,
              "habitat": {
                "name": "forest"
              },
              "flavor_text_entries": [
                {
                  "flavor_text": "Pikachu is an Electric-type Pokémon. It stores electricity in its cheeks."
                },
                {
                  "flavor_text": "Whenever Pikachu comes across something new, it blasts it with electricity."
                }
              ]
            }
            
            """.getBytes(Charset.defaultCharset()), PokemonSpecies.class);
    }


    public static PokemonSpecies generateFakeSpecies(String name, String habitat, boolean isLegendary) throws IOException {
        return MAPPER.readValue("""
            {
              "name": "{name}",
              "is_legendary": {legendary},
              "habitat": {
                "name": "{habitat}"
              },
              "flavor_text_entries": [
                {
                  "flavor_text": "A pokemon xxx"
                }
              ]
            }
            """
                .replace("{name}", name)
                .replace("{legendary}", String.valueOf(isLegendary))
                .replace("{habitat}", habitat)
            .getBytes(Charset.defaultCharset()), PokemonSpecies.class
        );
    }


    public static HttpClientErrorException buildClientException(int code) {
        return new HttpClientErrorException(HttpStatusCode.valueOf(code));
    }

    public static HttpServerErrorException buildServerException(int code) {
        return new HttpServerErrorException(HttpStatusCode.valueOf(code));
    }

    public static FunTranslationDto generateFakeTranslation() throws JsonProcessingException {
        return MAPPER.readValue("""
            {
              "success": {
                "total": 1
              },
              "contents": {
                "translated": "This fake, it is",
                "text": "This is fake",
                "translation": "yoda"
              }
            }
            """, FunTranslationDto.class);
    }
}
