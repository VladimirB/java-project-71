package hexlet.code;

import picocli.CommandLine;
import picocli.CommandLine.Command;

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
        var fileData1 = Parser.parse(filepath1);
        var fileData2 = Parser.parse(filepath2);

        String diff = Differ.generate(fileData1, fileData2);
        System.out.println(diff);
        return 0;
    }
}
