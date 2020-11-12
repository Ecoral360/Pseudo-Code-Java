package psc;

import java.io.File;
import java.io.FileNotFoundException;

import generateurs.lexer.LexerGenerator;

public class PscLexer extends LexerGenerator {
    public PscLexer(File configGrammaire) throws FileNotFoundException {
        setRegles(configGrammaire);
        sortRegle();
        //this.getReglesAjoutees().forEach(e -> System.out.println(e.getNom()));
    }

    private void setRegles(File configGrammaire){
        chargerRegles(configGrammaire);
    }


    public LexerGenerator build(){
        return (LexerGenerator) this;
    }

}
