package generateurs.parser;

import java.util.Hashtable;
import java.util.List;

import ast.Ast;
import tokens.Token;

public class ParserGenerator {
    Hashtable<String, Ast> programmes = new Hashtable<>();
    Hashtable<String, Ast> expressions = new Hashtable<>();


    public ParserGenerator(){

    }

    protected void ajouterProgramme(String nom, Ast fonction){
        this.programmes.put(nom, fonction);
    }

    protected void ajouterExpression(String nom, Ast fonction){
        this.expressions.put(nom, fonction);
    }


    protected void parse(List<Token> listToken){
        
    }

}
