package compiler;

import gen.japyLexer;
import gen.japyParser;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.io.IOException;
import java.nio.file.Paths;

public class Compiler {
    public static void main(String[] args) throws IOException {
        var path = Paths.get("sample", "input-2.trl");
        var stream = CharStreams.fromFileName(path.toString());
        var lexer = new japyLexer(stream);
        var tokens = new CommonTokenStream(lexer);
        var parser = new japyParser(tokens);
        parser.setBuildParseTree(true);
        var tree = parser.program();
        var walker = new ParseTreeWalker();
        var listener = new ProgramPrinter();

        walker.walk(listener, tree);
    }
}