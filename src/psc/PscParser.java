package psc;

import java.util.List;

import ast.Ast;
import generateurs.parser.ParserGenerator;



public class PscParser extends ParserGenerator{
    public PscParser(){
        ajouterProgrammes();
        ajouterExpressions();
    }


    private void ajouterProgrammes(){

        ajouterProgramme("expression", new Ast(){
            @Override
            public Object run(List<Object> p) {
                return null;
            }
        });

        ajouterProgramme("AFFICHER expression", new Ast(){
            @Override
            public Object run(List<Object> p) {
                return null;
            }
        });
    }

    private void ajouterExpressions(){

        ajouterExpression("ENTIER", new Ast(){
            @Override
            public Object run(List<Object> p) {
                return null;
            }
        });

        ajouterExpression("expression PLUS expression", new Ast(){
            @Override
            public Object run(List<Object> p) {
                return null;
            }
        });
    }
}
