package tokens;

public class Token {
    
    private String nom, valeur;

    Token(String nom, String valeur){
        this.nom = nom; 
        this.valeur = valeur;
    }

    public String getNom(){
        return this.nom;
    }

    public String getValeur(){
        return this.valeur;
    }
}
