package edu.isistan.spellchecker;
import edu.isistan.spellchecker.tokenizer.TokenScanner;
import org.junit.*;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import static org.junit.Assert.*;

/** Cree sus propios tests. */
public class MyTests {
    @Test public void testGetNextTokenWord() throws IOException {
        Reader in = new StringReader("D");
        TokenScanner d = new TokenScanner(in);
        try {
            assertTrue("has next", d.hasNext());
            assertEquals("D", d.next());

            assertFalse("reached end of stream", d.hasNext());
        } finally {
            in.close();
        }
    }
}