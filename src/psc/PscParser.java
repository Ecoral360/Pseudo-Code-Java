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

        ajouterProgramme("expression", new Ast<Object>(){
            @Override
            public Object run(List<Object> p) {
                return null;
            }
        });

        ajouterProgramme("AFFICHER expression", new Ast<Object>(){
            @Override
            public Object run(List<Object> p) {
                System.out.println(((PscAst<?>) p.get(0)).eval());
                return null;
            }
        });

        setOrdreProgramme();
    }



    private void ajouterExpressions(){

        ajouterExpression("ENTIER POINT ENTIER", new Ast<Reel>(0) {
            @Override
            public Reel run(List<Object> p) {
                return new Reel((Token) p.get(0), (Token) p.get(2));
            }
        });

        ajouterExpression("ENTIER", new Ast<Entier>(1){
            @Override
            public Entier run(List<Object> p) {
                return new Entier((Token) p.get(0));
            }
        });


        ajouterExpression("CHAINE", new Ast<Chaine>(1){
            @Override
            public Chaine run(List<Object> p) {
                return new Chaine((Token) p.get(0));
            }
        });

        ajouterExpression("PARENT_OUV expression PARENT_FERM", new Ast<Object>(2){
            @Override
            public Object run(List<Object> p) {
                return p.get(1);
            }
        });


        ajouterExpression("expression MOD expression", new Ast<Object>(3){
            @Override
            public Object run(List<Object> p) {
                return new BinaryOp(p.get(0), p.get(2)).mod();
            }
        });

        ajouterExpression("expression EXP expression", new Ast<Object>(4){
            @Override
            public Object run(List<Object> p) {
                return new BinaryOp(p.get(0), p.get(2)).exp();
            }
        });

        ajouterExpression("expression MUL expression", new Ast<Object>(5){
            @Override
            public Object run(List<Object> p) {
                return new BinaryOp(p.get(0), p.get(2)).mul();
            }
        });

        ajouterExpression("expression DIV expression", new Ast<Object>(5){
            @Override
            public Object run(List<Object> p) {
                return new BinaryOp(p.get(0), p.get(2)).div();
            }
        });

        ajouterExpression("expression PLUS expression", new Ast<Object>(6){
            @Override
            public Object run(List<Object> p) {
                return (p.get(0) instanceof Chaine || p.get(2) instanceof Chaine) ?
                        new BinaryOp(p.get(0), p.get(2)).concat():
                        new BinaryOp(p.get(0), p.get(2)).som();
            }
        });

        ajouterExpression("expression MOINS expression",  new Ast<Object>(6){
            @Override
            public Object run(List<Object> p) {
                return new BinaryOp(p.get(0), p.get(2)).sou();
            }
        });

        

        setOrdreExpression();
    }
}
