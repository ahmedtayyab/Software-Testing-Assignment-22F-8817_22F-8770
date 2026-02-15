package data;

import dal.HashCalculator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

public class HashCalculatorTest {

    @Test
    @DisplayName("MD5 hash should match known value")
    void testKnownMD5Hash() throws Exception {
        String input = "hello";
        String expectedHash = "5D41402ABC4B2A76B9719D911017C592";

        String actualHash = HashCalculator.calculateHash(input);

        assertEquals(expectedHash, actualHash,
                "Hash should match known MD5 value");
    }

    @Test
    @DisplayName("Same input should produce same hash")
    void testHashConsistency() throws Exception {
        String input = "consistent text";

        String hash1 = HashCalculator.calculateHash(input);
        String hash2 = HashCalculator.calculateHash(input);

        assertEquals(hash1, hash2,
                "Same input must always produce same hash");
    }

    @Test
    @DisplayName("Edited text should produce different hash")
    void testHashChangesAfterEditing() throws Exception {
        String originalContent = "This is original content";
        String editedContent = "This is edited content";

        String originalHash = HashCalculator.calculateHash(originalContent);
        String editedHash = HashCalculator.calculateHash(editedContent);

        assertNotEquals(originalHash, editedHash,
                "Editing content must change hash value");
    }

    @Test
    @DisplayName("Hash should be 32-character uppercase hexadecimal")
    void testHashFormat() throws Exception {
        String input = "format test";

        String hash = HashCalculator.calculateHash(input);

        assertEquals(32, hash.length(),
                "MD5 hash must be 32 characters long");

        assertTrue(hash.matches("[0-9A-F]+"),
                "Hash must contain only uppercase hexadecimal characters");
    }

    @Test
    @DisplayName("Empty string should produce valid MD5 hash")
    void testEmptyStringHash() throws Exception {
        String expected = "D41D8CD98F00B204E9800998ECF8427E";

        String actual = HashCalculator.calculateHash("");

        assertEquals(expected, actual,
                "Empty string should produce known MD5 hash");
    }

    @Test
    @DisplayName("Null input should throw exception")
    void testNullInput() {
        assertThrows(NullPointerException.class, () -> {
            HashCalculator.calculateHash(null);
        }, "Null input should throw NullPointerException");
    }

    @Test
    @DisplayName("Original import hash should remain unchanged after edit simulation")
    void testImportHashIntegrityScenario() throws Exception {

        String importedFileContent = "Initial file content";
        String originalImportHash = HashCalculator.calculateHash(importedFileContent);

        String editedContent = "Initial file content with modification";
        String currentSessionHash = HashCalculator.calculateHash(editedContent);

        assertNotEquals(originalImportHash, currentSessionHash,
                "Session hash must change after editing");

        String recalculatedOriginal = HashCalculator.calculateHash(importedFileContent);

        assertEquals(originalImportHash, recalculatedOriginal,
                "Original import hash should remain unchanged");
    }
}
