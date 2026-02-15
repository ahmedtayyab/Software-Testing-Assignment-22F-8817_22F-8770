package business;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;
import dal.TFIDFCalculator;

public class TFIDFCalculatorTest {
    
    private TFIDFCalculator calculator;
    
    @BeforeEach
    public void setUp() {
        calculator = new TFIDFCalculator();
    }
    
    @Test
    @DisplayName("Test TF-IDF calculation with known Arabic documents - manual verification")
    public void testKnownArabicDocumentTfIdf() {
        calculator.addDocumentToCorpus("القطة تجلس على السجادة");
        calculator.addDocumentToCorpus("الكلب يلعب في الحديقة");
        calculator.addDocumentToCorpus("القطة والكلب أصدقاء");
        
        String testDoc = "القطة تجلس";
        double actualScore = calculator.calculateDocumentTfIdf(testDoc);
        
        double expectedScore = 0.101;
        
        assertEquals(expectedScore, actualScore, 0.01, 
            "TF-IDF score should match manual calculation within ±0.01");
    }
    
    @Test
    @DisplayName("Test TF-IDF with single word Arabic document")
    public void testSingleWordArabicDocument() {
        calculator.addDocumentToCorpus("كتاب");
        calculator.addDocumentToCorpus("قلم");
        calculator.addDocumentToCorpus("دفتر");
        
        double score = calculator.calculateDocumentTfIdf("كتاب");
        
        assertEquals(0.405, score, 0.01);
    }
    
    @Test
    @DisplayName("Test TF-IDF with repeated words in Arabic document")
    public void testRepeatedWordsInArabicDocument() {
        calculator.addDocumentToCorpus("التعليم مهم جدا");
        calculator.addDocumentToCorpus("العلم نور");
        
        String testDoc = "التعليم التعليم التعليم";
        double score = calculator.calculateDocumentTfIdf(testDoc);
        
        assertEquals(0.0, score, 0.01);
    }
    
    @Test
    @DisplayName("Test TF-IDF with common and rare Arabic words")
    public void testCommonAndRareArabicWords() {
        calculator.addDocumentToCorpus("الطقس جميل اليوم");
        calculator.addDocumentToCorpus("الطقس بارد جدا");
        calculator.addDocumentToCorpus("النهار جميل");
        
        String testDoc = "الطقس اليوم";
        double score = calculator.calculateDocumentTfIdf(testDoc);
        
        assertTrue(score > 0, "TF-IDF score should be positive for valid document");
    }
    
    @Test
    @DisplayName("Test TF-IDF with multiple Arabic sentences")
    public void testMultipleArabicSentences() {
        calculator.addDocumentToCorpus("أحب القراءة كثيرا");
        calculator.addDocumentToCorpus("القراءة مفيدة للعقل");
        calculator.addDocumentToCorpus("الكتب مصدر المعرفة");
        
        String testDoc = "أحب القراءة والكتب";
        double score = calculator.calculateDocumentTfIdf(testDoc);
        
        assertTrue(score >= 0, "TF-IDF score should be non-negative");
        assertFalse(Double.isNaN(score), "TF-IDF score should not be NaN");
        assertFalse(Double.isInfinite(score), "TF-IDF score should not be infinite");
    }
    
    @Test
    @DisplayName("Test TF-IDF with empty document")
    public void testEmptyDocument() {
        calculator.addDocumentToCorpus("بعض النص العربي");
        
        String emptyDoc = "";
        
        assertDoesNotThrow(() -> {
            double score = calculator.calculateDocumentTfIdf(emptyDoc);
            assertTrue(Double.isNaN(score) || score == 0.0, 
                "Empty document should return NaN or 0.0");
        }, "Empty document should not throw exception");
    }
    
    @Test
    @DisplayName("Test TF-IDF with whitespace-only document")
    public void testWhitespaceOnlyDocument() {
        calculator.addDocumentToCorpus("نص عادي");
        
        String whitespaceDoc = "   \t\n   ";
        
        assertDoesNotThrow(() -> {
            double score = calculator.calculateDocumentTfIdf(whitespaceDoc);
            assertTrue(Double.isNaN(score) || score == 0.0, 
                "Whitespace-only document should return NaN or 0.0");
        }, "Whitespace document should not throw exception");
    }
    
    @Test
    @DisplayName("Test TF-IDF with special characters only")
    public void testSpecialCharactersOnly() {
        calculator.addDocumentToCorpus("نص عربي");
        
        String specialChars = "!@#$%^&*()_+-=[]{}|;:',.<>?/~`";
        
        assertDoesNotThrow(() -> {
            double score = calculator.calculateDocumentTfIdf(specialChars);
            assertTrue(Double.isNaN(score) || score == 0.0, 
                "Special characters should result in NaN or 0.0");
        }, "Special characters should not throw exception");
    }
    
    @Test
    @DisplayName("Test TF-IDF with mixed Arabic and special characters")
    public void testMixedArabicAndSpecialCharacters() {
        calculator.addDocumentToCorpus("الطقس جميل");
        calculator.addDocumentToCorpus("اليوم بارد");
        
        String mixedDoc = "!!!الطقس@@@ ###جميل$$$";
        
        assertDoesNotThrow(() -> {
            double score = calculator.calculateDocumentTfIdf(mixedDoc);
            assertTrue(score >= 0 || Double.isNaN(score), 
                "Mixed content should be handled gracefully");
        }, "Mixed content should not throw exception");
    }
    
    @Test
    @DisplayName("Test TF-IDF with numbers only")
    public void testNumbersOnly() {
        calculator.addDocumentToCorpus("نص عربي");
        
        String numbers = "123 456 789";
        
        assertDoesNotThrow(() -> {
            double score = calculator.calculateDocumentTfIdf(numbers);
            assertTrue(Double.isNaN(score) || score == 0.0, 
                "Numbers-only document should return NaN or 0.0");
        }, "Numbers should not throw exception");
    }
    
    @Test
    @DisplayName("Test TF-IDF with empty corpus")
    public void testEmptyCorpus() {
        String testDoc = "نص عربي";
        
        assertDoesNotThrow(() -> {
            double score = calculator.calculateDocumentTfIdf(testDoc);
            assertTrue(!Double.isInfinite(score), 
                "Empty corpus should be handled gracefully");
        }, "Empty corpus should not throw exception");
    }
    
    @Test
    @DisplayName("Test TF-IDF with null-like input")
    public void testNullHandling() {
        calculator.addDocumentToCorpus("نص عربي");
        
        assertDoesNotThrow(() -> {
            String nullString = null;
            try {
                calculator.calculateDocumentTfIdf(nullString);
            } catch (NullPointerException e) {
            }
        });
    }
    
    @Test
    @DisplayName("Test TF-IDF with very long Arabic document")
    public void testVeryLongArabicDocument() {
        calculator.addDocumentToCorpus("كلمة");
        
        StringBuilder longDoc = new StringBuilder();
        for (int i = 0; i < 1000; i++) {
            longDoc.append("كلمة ");
        }
        
        assertDoesNotThrow(() -> {
            double score = calculator.calculateDocumentTfIdf(longDoc.toString());
            assertFalse(Double.isInfinite(score), "Long document should not cause overflow");
        }, "Very long document should be handled gracefully");
    }
    
    @Test
    @DisplayName("Test TF-IDF with document containing only stopwords")
    public void testDocumentWithOnlyStopwords() {
        calculator.addDocumentToCorpus("في على من إلى");
        calculator.addDocumentToCorpus("هذا ذلك تلك");
        
        String stopwordsDoc = "في على من";
        
        assertDoesNotThrow(() -> {
            double score = calculator.calculateDocumentTfIdf(stopwordsDoc);
            assertTrue(score >= 0 || Double.isNaN(score), 
                "Stopwords-only document should be handled");
        }, "Stopwords should not cause exception");
    }
    
    @Test
    @DisplayName("Test corpus building with multiple documents")
    public void testCorpusBuilding() {
        assertDoesNotThrow(() -> {
            calculator.addDocumentToCorpus("وثيقة واحد");
            calculator.addDocumentToCorpus("وثيقة اثنان");
            calculator.addDocumentToCorpus("وثيقة ثلاثة");
            
            double score = calculator.calculateDocumentTfIdf("وثيقة");
            assertTrue(score >= 0 || Double.isNaN(score));
        });
    }
    
    @Test
    @DisplayName("Test TF-IDF consistency with same document")
    public void testConsistencyWithSameDocument() {
        calculator.addDocumentToCorpus("نص عربي للاختبار");
        calculator.addDocumentToCorpus("نص آخر");
        
        String testDoc = "نص عربي";
        double score1 = calculator.calculateDocumentTfIdf(testDoc);
        double score2 = calculator.calculateDocumentTfIdf(testDoc);
        
        assertEquals(score1, score2, 0.0001, 
            "Same document should produce same TF-IDF score");
    }
}
