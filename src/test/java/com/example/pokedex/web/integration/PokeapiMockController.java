package com.example.pokedex.web.integration;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

/**
 * <b>Test by Contract.</b>
 *<br/>
 * This class will operate as a Mock Server Controller.<br/><br/>
 * Given the simplicity of this project, implementing a mock server within the application is a practical solution. <br/>
 * In a more complex or production-grade environment, I would use a dedicated mock server, either via a library or a standalone application .
 *<br/><br>
 * The <i>"Test by Contract"</i> approach ensures we follow a defined request-response model. <br/>
 * Using a mock server that mimics the original lets us confirm our logic works correctly during testing, without needing real external dependencies.
 *<br/>
 */
@RestController
@RequestMapping("api/v2/")
@Profile("test")
public class PokeapiMockController {

    private final ObjectMapper mapper;

    public PokeapiMockController() {
        this.mapper = new ObjectMapper();
    }

    /**
     * This API is used as a mock of the real pokeapi pokemon-species Endpoint.<br />
     * By obtaining an example response in a json file, we later use it as a response imitating a real environment<br />
     *
     *
     * @param name pokemon
     * @return Pokemon Species
     */
    @GetMapping("pokemon-species/{name}")
    public ResponseEntity<JsonNode> getCachedPokemonSpecies(@PathVariable("name") String name) throws IOException {

        final URL resource = PokeapiMockController.class.getResource("/responses/" + name + "-species.json");

        if (Objects.isNull(resource)) {
            return ResponseEntity
                .notFound()
                .header("Content-type", "text/plain; charset=utf-8")
                .build();
        }

        final File file = new File(resource.getPath());
        final byte[] content = Files.readAllBytes(Paths.get(file.getPath()));;
        final JsonNode json = mapper.readTree(content);

        return ResponseEntity.ok(json);
    }
}
