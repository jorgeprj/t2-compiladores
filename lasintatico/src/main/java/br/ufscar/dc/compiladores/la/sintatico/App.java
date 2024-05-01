package br.ufscar.dc.compiladores.la.sintatico;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Token;

import java.nio.charset.StandardCharsets;
import java.io.FileWriter;
import java.io.IOException;

public class App {
    public static void main(String args[]) throws IOException {

        try {
            CharStream cs = CharStreams.fromFileName(args[0]);
            LALexer lexer = new LALexer(cs);
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            LAParser parser = new LAParser(tokens);

            String filename = args[1];
            FileWriter writer = new FileWriter(filename);
            
            //Configuração do erro customizado
            MyCustomErrorListener mcel = new MyCustomErrorListener(writer);
            parser.removeErrorListeners();
            parser.addErrorListener(mcel);

            parser.programa();

            writer.close();
        }catch (IOException ex) {
        }
    }
}
