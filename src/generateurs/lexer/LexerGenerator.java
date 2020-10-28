package generateurs.lexer;

import java.util.ArrayList;
import java.util.List;

import generateurs.lexer.regle.Regle;
import tokens.Token;

public class LexerGenerator {
    private ArrayList<Regle> reglesAjoutees = new ArrayList<>();
    private ArrayList<Regle> reglesIgnorees = new ArrayList<>();
    
    LexerGenerator(){
        
    }


    protected void AjouterRegle(String nom, String pattern){
        this.reglesAjoutees.add(new Regle(nom, pattern));
    }

    protected void IgnorerRegle(String pattern){
        this.reglesIgnorees.add(new Regle(pattern));
    }

    public ArrayList<Regle> getReglesAjoutees(){
        return this.reglesAjoutees;
    }

    public ArrayList<Regle> getReglesIgnorees(){
        return this.reglesIgnorees;
    }



    public List<Token> lex(String line){
        
        
        
        return null;
    }

    private Token getNext(){
        
        return null;
    }

}
