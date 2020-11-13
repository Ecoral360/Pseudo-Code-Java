package psc;

import java.util.ArrayList;
import java.util.List;

import ast.Ast;
import executeur.Executeur;
import generateurs.parser.ParserGenerator;
import psc.PscAst.*;
import tokens.Token;


public class PscParser extends ParserGenerator {
    VariableManager variableManager = new VariableManager();
    
    public PscParser(){
        ajouterProgrammes();
        ajouterExpressions();
    }


    private void ajouterProgrammes(){

        ajouterProgramme("", new Ast<Object>(){
            @Override
            public Object run(List<Object> p) {
                return new Nul();
            }
        });

        ajouterProgramme("expression", new Ast<Object>(){
            @Override
            public Object run(List<Object> p) {
                return p.size() == 0 ? new Nul() : p.get(0);
            }
        });

        ajouterProgramme("AFFICHER expression", new Ast<Object>(0){
            @Override
            public Object run(List<Object> p) {
                System.out.println(((PscAst<?>) p.get(1)).eval());
                return new Nul();
            }
        });

        ajouterProgramme("NOM_VARIABLE ASSIGNEMENT expression", new Ast<Variable>(0){
            @Override
            public Variable run(List<Object> p) {
                return new Variable(((Token) p.get(0)).getValeur(), (PscAst<?>) p.get(2));
            }
        });


        ajouterProgramme("SI expression ALORS", new Ast<Booleen>(0){
            @Override
            public Booleen run(List<Object> p) {
                if (((Booleen) p.get(1)).eval()){
                    Executeur.nouveauBloc("si");
                } else {
                    if (Executeur.obtenirCoordDict().containsKey("<0>sinon" + Executeur.obtenirCoord())){
                        Executeur.nouveauBloc("sinon");
                    }
                }
                return (Booleen) p.get(1);
            }
            @Override
            public String prochaineCoord(String coord){
                return Executeur.nouveauBloc("si");
            }
        });


        ajouterProgramme("SINON", new Ast<Object>(0){
            @Override
            public Object run(List<Object> p) {
                Executeur.finBloc();
                return new Nul();
            }
            @Override
            public String prochaineCoord(String coord){
                Executeur.finBloc();
                return Executeur.nouveauBloc("sinon");
            }
        });

        ajouterProgramme("FIN_SI", new Ast<Object>(0){
            @Override
            public Object run(List<Object> p) {
                Executeur.finBloc();
                return new Nul();
            }
            @Override
            public String prochaineCoord(String coord){
                return Executeur.finBloc();
            }
        });

        ajouterProgramme("TANT_QUE expression", new Ast<Booleen>(0){
            @Override
            public Booleen run(List<Object> p) {
                if (((Booleen) p.get(1)).eval()){
                    Executeur.nouveauBloc("tantque");
                }
                return (Booleen) p.get(1);
            }
            @Override
            public String prochaineCoord(String coord){
                return Executeur.nouveauBloc("tantque");
            }
        });


        ajouterProgramme("{fin_bloucle}", new Ast<Object>(0){
            @Override
            public Object run(List<Object> p) {
                Executeur.finBloc();
                Executeur.coordMoinsUn();
                return new Nul();
            }
            @Override
            public String prochaineCoord(String coord) {
                return Executeur.finBloc();
            }
        });
        setOrdreProgramme();
    }



    private void ajouterExpressions(){

        ajouterExpression("{type_de_donnees}", new Ast<Object>(0) {
            @Override
            public Object run(List<Object> p) {
                Token valeur = (Token) p.get(0);
                String nom = valeur.getNom();
                switch (nom) {
                    case "ENTIER":
                    return new Entier(valeur);

                    case "REEL":
                    return new Reel(valeur);

                    case "CHAINE":
                    return new Chaine(valeur);

                    case "BOOLEEN":
                    return new Booleen(valeur);

                    case "NUL":
                    return new Nul();
                
                    default:
                    throw new Error("Type de donnee invalide");
                }
            }
        });

        ajouterExpression("expression A expression", new Ast<Liste<Entier>>(){
            @Override
            public Liste<Entier> run(List<Object> p) {
                ArrayList<Entier> range = new ArrayList<>();
                for (int i = ((Entier) p.get(0)).eval(); i < ((Entier) p.get(2)).eval(); ++i){
                    range.add(new Entier(i));
                }
                return new Liste<Entier>(range.toArray(Entier[]::new));
            };
        });

        ajouterExpression("NOM_VARIABLE", new Ast<Object>(0) {
            @Override
            public Object run(List<Object> p) {
                return VariableManager.varDict.get(VariableManager.varDispo).get(((Token) p.get(0)).getValeur()).getValeur();
            }
        });

        ajouterExpression("PARENT_OUV expression PARENT_FERM", new Ast<Object>(2){
            @Override
            public Object run(List<Object> p) {
                return p.get(1);
            }
        });


        ajouterExpression("expression MOD expression", new Ast<Object>(3) {
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
                return (p.get(0) instanceof Chaine) ?
                        new BinaryOp(p.get(0), p.get(2)).repeat():
                        new BinaryOp(p.get(0), p.get(2)).mul();
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

        ajouterExpression("expression {comparaison} expression", new Ast<Booleen>(){
            @Override
            public Booleen run(List<Object> p) {
                String nom = ((Token) p.get(1)).getNom();
                BinaryComp comp = new BinaryComp(p.get(0), p.get(2));
                switch (nom) {
                    case "EGAL":
                    return comp.egal();

                    case "PAS_EGAL":
                    return comp.pasEgal();

                    case "PLUS_GRAND":
                    return comp.plusGrand();

                    case "PLUS_PETIT":
                    return comp.plusPetit();

                    case "PLUS_GRAND_EGAL":
                    return comp.plusGrandEgal();

                    case "PLUS_PETIT_EGAL":
                    return comp.plusPetitEgal();
                
                    default:
                    throw new Error("Comparaison invalide");
                }
            }
        });
        setOrdreExpression();
    }
}
