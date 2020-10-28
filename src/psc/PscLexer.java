package psc;

import generateurs.lexer.LexerGenerator;

public class PscLexer extends LexerGenerator{
    public PscLexer(){
        setRegles();
    }

    private void setRegles(){
        ajouterRegle("AFFICHER", "afficher");
        ajouterRegle("ENTIER", "\\d+");
        ajouterRegle("PLUS", "\\+");


        ignorerRegle("\\s+");
    }


    public LexerGenerator build(){
        return (LexerGenerator) this;
    }

}
