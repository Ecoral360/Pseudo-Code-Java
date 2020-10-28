package generateurs.lexer.regle;

public class Regle {
    
    public String nom, pattern;

    public Regle(String nom, String pattern){
        this.nom = nom;
        this.pattern = pattern;
    }

    public Regle(String pattern){
        this.pattern = pattern;
    }


}
