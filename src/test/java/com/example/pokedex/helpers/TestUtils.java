package com.example.pokedex.helpers;

import com.example.pokedex.web.dto.PokemonSpecies;
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

    public static HttpClientErrorException buildClientException(int code) {
        return new HttpClientErrorException(HttpStatusCode.valueOf(code));
    }

    public static HttpServerErrorException buildServerException(int code) {
        return new HttpServerErrorException(HttpStatusCode.valueOf(code));
    }
}
