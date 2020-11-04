package psc;

import generateurs.lexer.LexerGenerator;

public class PscLexer extends LexerGenerator{
    public PscLexer(){
        setRegles();
    }

    private void setRegles(){
        // règles à ajouter
        ajouterRegle("AFFICHER", "afficher");
        ajouterRegle("ENTIER", "\\d+");

        ajouterRegle("PLUS", "\\+");
        ajouterRegle("EXP", "\\*{2}");
        ajouterRegle("MUL", "\\*");
        ajouterRegle("MOINS", "\\-");
        ajouterRegle("DIV", "\\\\");
        ajouterRegle("MOD", "mod|[%]");

        ajouterRegle("PARENT_OUV", "[(]");
        ajouterRegle("PARENT_FERM", "[)]");

        ajouterRegle("POINT", "\\.");

        ajouterRegle("CHAINE", "\".*?\"");

        // règles à ignorer
        ignorerRegle("\\s+");
    }


    public LexerGenerator build(){
        return (LexerGenerator) this;
    }

}
