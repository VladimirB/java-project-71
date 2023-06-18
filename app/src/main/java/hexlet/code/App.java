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
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.stream.Stream;

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

        String diff = makeDiff(fileData1, fileData2);
        System.out.println(diff);
        return 0;
    }

    private Map<String, String> getFileData(String filePath, ObjectMapper mapper) throws IOException {
        String fileContent = readFileContent(filePath);
        return mapper.readValue(fileContent, new TypeReference<>() {});
    }

    private String readFileContent(String filePath) throws IOException {
        Path path = Paths.get(filePath).toAbsolutePath().normalize();
        if (!Files.exists(path)) {
            throw new FileNotFoundException("Not found file in path " + filePath);
        }
        return Files.readString(path);
    }

    private String makeDiff(Map<String, String> data1, Map<String, String> data2) {
        List<String> keys = Stream.concat(data1.keySet().stream(), data2.keySet().stream())
                .distinct()
                .sorted()
                .toList();

        StringBuilder sb = new StringBuilder();
        sb.append("{").append("\n");

        keys.forEach(key -> {
            if (data1.containsKey(key) && data2.containsKey(key)) {
                if (data1.get(key).equals(data2.get(key))) {
                    appendLine(sb, "   ", key, data1.get(key));
                } else {
                    appendLine(sb, " - ", key, data1.get(key));
                    appendLine(sb, " + ", key, data2.get(key));
                }
            } else if (!data2.containsKey(key) && data1.containsKey(key)) {
                appendLine(sb, " - ", key, data1.get(key));
            } else {
                appendLine(sb, " + ", key, data2.get(key));
            }
        });

        sb.append("}");
        return sb.toString();
    }

    private void appendLine(StringBuilder sb, String prefix, String key, String value) {
        sb.append(String.format("%s%s: %s", prefix, key, value))
                .append("\n");
    }
}