package generateurs.lexer.regle;

import java.util.ArrayList;
import java.util.Hashtable;

public class Regle {
    
    static private Hashtable<String, ArrayList<String>> categories = new Hashtable<>();

    private String nom, pattern, categorie;

    public Regle(String nom, String pattern, String categorie){
        this.nom = nom;
        this.pattern = pattern;
        this.categorie = categorie;

        categories.putIfAbsent(categorie, new ArrayList<>());
        categories.get(categorie).add(nom);
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

    public String getCategorie(){
        return this.categorie;
    }



    public static Hashtable<String, ArrayList<String>> getCategories(){
        return categories;
    }

    public static ArrayList<String> getMembreCategorie(String nomCategorie){
        return categories.get(nomCategorie);
    }
}
