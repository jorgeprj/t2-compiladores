package br.ufscar.dc.compiladores.la.sintatico;

import org.antlr.v4.runtime.ANTLRErrorListener;
import org.antlr.v4.runtime.Token;
import java.io.FileWriter;
import java.io.IOException;
import java.util.BitSet;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.atn.ATNConfigSet;
import org.antlr.v4.runtime.dfa.DFA;

public class MyCustomErrorListener implements ANTLRErrorListener {

    private FileWriter file;
    private boolean jaErrou;

    public MyCustomErrorListener(FileWriter file) {
        this.jaErrou = false;
        this.file = file;
    }

    @Override
    public void reportAmbiguity(Parser recognizer, DFA dfa, int startIndex, int stopIndex, boolean exact,
            BitSet ambigAlts, ATNConfigSet configs) {
        // Método vazio, não é necessário para o trabalho 2
    }

    @Override
    public void reportAttemptingFullContext(Parser recognizer, DFA dfa, int startIndex, int stopIndex,
            BitSet conflictingAlts, ATNConfigSet configs) {
        // Método vazio, não é necessário para o trabalho 2
    }

    @Override
    public void reportContextSensitivity(Parser recognizer, DFA dfa, int startIndex, int stopIndex, int prediction,
            ATNConfigSet configs) {
        // Método vazio, não é necessário para o trabalho 2
    }

    @Override
    public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine,
            String msg, RecognitionException e) {
        // Condicional para não imprimir mais de um erro sintático
        if (this.jaErrou)
            return;

        try {
            // Converte o símbolo ofensivo para um objeto Token
            Token t = (Token) offendingSymbol;
            // Obtém o nome simbólico do tipo de token usando o Vocabulary da lexer
            String displayName = LALexer.VOCABULARY.getDisplayName(t.getType());
            switch (displayName) {

                // Condicional para detectar erros léxicos de caracteres inválidos na linguagem
                case "ERRO":
                    file.write("Linha " + t.getLine() + ": " + t.getText() + " - simbolo nao identificado\n");
                    break;

                // Condicional para detectar erros léxicos de comentários não fechados
                case "COMENTARIO_NAO_FECHADO":
                    file.write("Linha " + t.getLine() + ": comentario nao fechado\n");
                    break;

                // Condicional para detectar erros léxicos de cadeias não fechadas
                case "CADEIA_NAO_FECHADA":
                    file.write("Linha " + t.getLine() + ": cadeia literal nao fechada\n");
                    break;

                // Condicional para detectar erros sintáticos, no caso usado para tirar as "<>"
                // do EOF
                case "EOF":
                    file.write("Linha " + t.getLine() + ": erro sintatico proximo a EOF\n");
                    break;

                // Condicional para detectar qualquer outro erro sintático
                default:
                    file.write("Linha " + t.getLine() + ": erro sintatico proximo a " + t.getText() + "\n");
            }
            // Escreve uma mensagem indicando o fim da compilação
            file.write("Fim da compilacao\n");
            // Atualiza a variável para indicar que um erro já foi reportado
            this.jaErrou = true;
        } catch (IOException ex) {
            // Lida com exceções de E/S, caso ocorram
        }

    }
}
