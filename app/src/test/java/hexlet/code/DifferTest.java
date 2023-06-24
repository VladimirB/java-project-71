package hexlet.code;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;

class DifferTest {

    @Test
    public void testParameterExistsInBothFiles() {
        var file1 = Map.of("timeout", "50");
        var file2 = Map.of("timeout", "20");

        var diff = Differ.generate(file1, file2);

        assertTrue(diff.contains("- timeout: 50") && diff.contains("+ timeout: 20"));
    }

    @Test
    public void testParameterAddedInSecondFile() {
        var file1 = Map.of("", "");
        var file2 = Map.of("timeout", "20");

        var diff = Differ.generate(file1, file2);

        assertTrue(diff.contains("+ timeout: 20"));
    }

    @Test
    public void testParameterRemovedFromSecondFile() {
        var file1 = Map.of("timeout", "20");
        var file2 = Map.of("", "");

        var diff = Differ.generate(file1, file2);

        assertTrue(diff.contains("- timeout: 20"));
    }
}
