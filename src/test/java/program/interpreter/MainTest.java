package program.interpreter;

import org.junit.Test;
import program.Io;

import static org.junit.Assert.*;

public class MainTest {
    @Test
    public void shouldDoStuff() throws Exception {
        Main.main("/home/ibardych/git/scheme.explore/src/main/resources/interpreter.scm");
    }
}