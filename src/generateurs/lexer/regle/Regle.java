package generateurs.lexer.regle;

public class Regle {
    
    private String nom, pattern;

    public Regle(String nom, String pattern){
        this.nom = nom;
        this.pattern = pattern;
    }

    public Regle(String pattern){
        this.pattern = pattern;
    }

    public String getNom(){
        return this.nom;
    }

    public String getPattern(){
        return this.pattern;
    }


}
