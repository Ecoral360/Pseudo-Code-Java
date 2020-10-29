package psc;

import java.util.List;

import ast.Ast;
import generateurs.parser.ParserGenerator;
import psc.PscAst.*;


public class PscParser extends ParserGenerator{
    public PscParser(){
        ajouterProgrammes();
        ajouterExpressions();
    }


    private void ajouterProgrammes(){

        ajouterProgramme("expression", new Ast<Object>(){
            @Override
            public Object run(List<Object> p) {
                return null;
            }
        });

        ajouterProgramme("AFFICHER expression", new Ast<Object>(){
            @Override
            public Object run(List<Object> p) {
                return null;
            }
        });
    }

    private void ajouterExpressions(){

        ajouterExpression("ENTIER", new Ast<Entier>(){
            @Override
            public Entier run(List<Object> p) {
                return null;
            }
        });

        ajouterExpression("expression PLUS expression", new Ast<Object>(){
            @Override
            public Object run(List<Object> p) {
                return new BinaryOp(p.get(0), p.get(2)).som();
            }
        });
    }
}
