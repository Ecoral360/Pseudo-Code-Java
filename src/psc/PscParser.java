package psc;

import java.util.List;

import ast.Ast;
import generateurs.parser.ParserGenerator;
import psc.PscAst.*;
import tokens.Token;


public class PscParser extends ParserGenerator{
    public PscParser(){
        ajouterProgrammes();
        ajouterExpressions();
    }


    private void ajouterProgrammes(){

        ajouterProgramme("expression", null, new Ast<Object>(){
            @Override
            public Object run(List<Object> p) {
                return null;
            }
        });

        ajouterProgramme("AFFICHER expression", null, new Ast<Object>(){
            @Override
            public Object run(List<Object> p) {
                System.out.println(((PscAst<?>) p.get(0)).eval());
                return null;
            }
        });
    }

    private void ajouterExpressions(){

        ajouterExpression("ENTIER", null, new Ast<Entier>(){
            @Override
            public Entier run(List<Object> p) {
                return new Entier((Token) p.get(0));
            }
        });

        ajouterExpression("expression PLUS expression", null, new Ast<Object>(){
            @Override
            public Object run(List<Object> p) {
                return new BinaryOp(p.get(0), p.get(2)).som();
            }
        });
    }
}
