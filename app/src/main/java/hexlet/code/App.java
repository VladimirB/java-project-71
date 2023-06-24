package hexlet.code;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import picocli.CommandLine;
import picocli.CommandLine.Command;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.concurrent.Callable;

@Command(
        name = "gendiff",
        mixinStandardHelpOptions = true,
        description = "Compares two configuration files and shows a difference.",
        version = "1.0"
)
public class App implements Callable<Integer> {

    @CommandLine.Option(names = {"-f", "--format"}, description = "output format [default: stylish]")
    String format;

    @CommandLine.Parameters(index = "0", description = "path to first file")
    String filepath1;

    @CommandLine.Parameters(index = "1", description = "path to second file")
    String filepath2;

    public static void main(String[] args) {
        int exitCode = new CommandLine(new App()).execute(args);
        System.exit(exitCode);
    }

    @Override
    public Integer call() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        var fileData1 = getFileData(filepath1, mapper);
        var fileData2 = getFileData(filepath2, mapper);

        String diff = Differ.generate(fileData1, fileData2);
        System.out.println(diff);
        return 0;
    }

    private static Map<String, String> getFileData(String filePath, ObjectMapper mapper) throws IOException {
        String fileContent = readFileContent(filePath);
        return mapper.readValue(fileContent, new TypeReference<>() {
        });
    }

    private static String readFileContent(String filePath) throws IOException {
        Path path = Paths.get(filePath).toAbsolutePath().normalize();
        if (!Files.exists(path)) {
            throw new FileNotFoundException("Not found file in path " + filePath);
        }
        return Files.readString(path);
    }
}
