package hexlet.code;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

public final class Parser {

    public static Map<String, String> parse(String filePath) throws IOException {
        var extension = filePath.substring(filePath.lastIndexOf('.') + 1);

        var objectMapper = createMapperBy(extension);

        String fileContent = readFileContent(filePath);
        return objectMapper.readValue(fileContent, new TypeReference<>() {
        });
    }

    private static ObjectMapper createMapperBy(String fileExtension) {
        return switch (fileExtension) {
            case "json" -> new ObjectMapper();
            case "yml" -> new YAMLMapper();
            default -> throw new IllegalStateException("File extension is not supported by parser: " + fileExtension);
        };
    }

    private static String readFileContent(String filePath) throws IOException {
        Path path = Paths.get(filePath).toAbsolutePath().normalize();
        if (!Files.exists(path)) {
            throw new FileNotFoundException("Not found file in path " + filePath);
        }
        return Files.readString(path);
    }
}
