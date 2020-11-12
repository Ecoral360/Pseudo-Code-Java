package generateurs.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ast.Ast;
import generateurs.lexer.regle.Regle;
import tokens.Token;

public class ParserGenerator {
    Hashtable<String, Ast<?>> programmes = new Hashtable<>();
    ArrayList<String> ordreProgrammes = new ArrayList<>();

    Hashtable<String, Ast<?>> expressions = new Hashtable<>();
    ArrayList<String> ordreExpressions = new ArrayList<>();

    public ParserGenerator(){
    }

    private String remplacerCategoriesParMembre(String pattern){
        String nouveauPattern = pattern;

        for (String motClef : pattern.split(" ")){  // on divise le pattern en mot clef afin d'evaluer ceux qui sont des categories (une categorie est entouree par des {})
            if (motClef.startsWith("{") && motClef.endsWith("}")){  // on test si le mot clef est une categorie
                ArrayList<String> membresCategorie = Regle.getMembreCategorie(motClef.substring(1, motClef.length()-1)); // on va chercher les membres de la categorie (toutes les regles)
                if (membresCategorie == null){      
                    throw new Error("La categorie: '" + pattern + "' n'existe pas");    // si la categorie n'existe pas, on lance une erreur
                } else {
                   nouveauPattern = nouveauPattern.replace(motClef, "(" + String.join("|", membresCategorie) + ")");     
                        // on remplace la categorie par les membres de la categorie
                        // pour ce faire, on entoure les membres dans des parentheses et on
                        // separe les membres par des |
                        // de cette facon, lorsque nous allons tester par regex si une ligne correspond
                        // a un programme ou une expression, la categorie va "matcher" avec
                        // tous les membres de celle-ci  
                }
            }
        }
        return nouveauPattern;  // on retourne le pattern avec les categories changees
    }

 
    protected void ajouterProgramme(String pattern, Ast<?> fonction) { 
        /*
            importance : 0 = plus important
            si plusieurs programmes ont la même importance, le dernier ajouté sera priorisé
        */

        String nouveauPattern = remplacerCategoriesParMembre(pattern); // remplace les categories par ses membres, s'il n'y a pas de categorie, ne modifie pas le pattern

        this.programmes.put(nouveauPattern, fonction);
    }

    protected void ajouterExpression(String pattern, Ast<?> fonction) {  
        /*
            importance : 0 = plus important
            si plusieurs expressions ont la même importance, la dernière ajoutée sera priorisée
        */
        String nouveauPattern = remplacerCategoriesParMembre(pattern);

        this.expressions.put(nouveauPattern, fonction);
    }

    protected void setOrdreProgramme(){
        for (int i = 0; i < this.programmes.size(); ++i){
            this.ordreProgrammes.add(null);
        }
        for (String pattern : this.programmes.keySet()){
            int importance = this.programmes.get(pattern).getImportance();
            if (importance == -1){
                this.ordreProgrammes.add(pattern);
            } else {
                if (this.ordreProgrammes.get(importance) == null){
                    this.ordreProgrammes.set(importance, pattern);
                } else {
                    this.ordreProgrammes.add(importance, pattern);
                }
                
            }
        }
        this.ordreProgrammes.removeIf(e -> e == null);
        //System.out.println(this.ordreProgrammes);
    }

    protected void setOrdreExpression(){
        for (int i = 0; i < this.expressions.size(); ++i){
            this.ordreExpressions.add(null);
        }
        for (String pattern : this.expressions.keySet()){
            int importance = this.expressions.get(pattern).getImportance();
            if (importance == -1){
                this.ordreExpressions.add(pattern);
            } else {
                if (this.ordreExpressions.get(importance) == null){
                    this.ordreExpressions.set(importance, pattern);
                } else {
                    this.ordreExpressions.add(importance, pattern);
                }
                
            }
        }
        this.ordreExpressions.removeIf(e -> e == null);
        //System.out.println(this.ordreExpressions);
    }



    public Object parse(List<Token> listToken) {
        //System.out.println("\n");

        String programme = getProgramme(listToken);
        if (programme == null){
            throw new Error("Programme invalide");
        }
        //System.out.println("Programme trouvé: " + programme);

        ArrayList<ArrayList<Token>> expressions = getExpressions(listToken, programme);
        //System.out.println("Expression trouvée: " + expressions);

        List<Object> expressionsResolues = new ArrayList<>();

        for (ArrayList<Token> expression : expressions) {
            ArrayList<Object> resolu = resoudreExpressions(expression);
            if (resolu.size() > 1) {
                throw new Error("Expression invalide: " + resolu);
            } else {
                expressionsResolues.add(resolu.get(0));
            }
            
            //System.out.println("Expressions résolues: " + resolu.get(0));
        }

        ArrayList<Object> finalLine = new ArrayList<>();

        
        Iterator<Object> expressionIt = expressionsResolues.iterator();
        Iterator<Token> programmeIt = listToken.iterator();
        
        Scanner lineScan = new Scanner(programme).useDelimiter(" "); 
        lineScan.forEachRemaining(token -> {
            if (token.equals("expression")){
                finalLine.add(expressionIt.hasNext() ? expressionIt.next() : null);
            } else {
                finalLine.add(programmeIt.hasNext() ? programmeIt.next() : null);
            }
        });
        lineScan.close();
        
        return this.programmes.get(programme).run(finalLine);
    }


    public String getProgramme(List<Token> listToken) {
        String programmeTrouve = null;
        List<String> structureLine = new ArrayList<>();
        listToken.forEach(e -> {
            String nom = e.getNom();
            structureLine.add(nom);
        });

        for (String programme : this.ordreProgrammes){
            //System.out.println(structureProgramme + " " + structureLine);
            
            if (memeStructure(String.join(" ", structureLine), programme)) {
                programmeTrouve = programme;
                break;
            }
        }
        return programmeTrouve;
    }


    public ArrayList<ArrayList<Token>> getExpressions(List<Token> listToken, String programme) {
        ArrayList<String> structureLine = new ArrayList<>();
        listToken.forEach(e -> structureLine.add(e.getNom()));


        ArrayList<String> structureProgramme = new ArrayList<>(Arrays.asList(programme.split(" ")));
        structureProgramme.removeIf(e -> e.equals("expression"));
        Iterator<String> iterProgramme = structureProgramme.iterator();

        ArrayList<ArrayList<Token>> expressionsList = new ArrayList<>(); 

        String clef = iterProgramme.hasNext() ? iterProgramme.next() : null;

        ArrayList<Token> expressionList = new ArrayList<>(); 

        for (int i = 0; i < structureLine.size(); ++i) {
            if (structureLine.get(i).equals(clef)) {
                clef = iterProgramme.hasNext() ? iterProgramme.next() : null;
                expressionsList.add(expressionList);
                expressionList = new ArrayList<>();
            } else {
                expressionList.add(listToken.get(i));
            }
        }
        expressionsList.add(expressionList);
        expressionsList.removeIf(l -> l.isEmpty());

        return expressionsList;
    }


    public ArrayList<Object> resoudreExpressions(ArrayList<Token> expressionsList) {
        
        ArrayList<Object> expressionArray = new ArrayList<>(expressionsList);
        
        //System.out.println(this.ordreExpressions);
        
        for (String expression : this.ordreExpressions) {

            List<String> expressionNom = new ArrayList<>();
            expressionArray.forEach(e -> expressionNom.add(e instanceof Token ? ((Token) e).getNom() : "expression"));
            //System.out.println("Nom " + expressionNom);

            int longueurExpression = expression.split(" ").length;

            if (expressionArray.size() >= longueurExpression) {
                for (int i = 0; i + longueurExpression <= expressionArray.size(); ++i) {

                    if (memeStructure(String.join(" ", expressionNom.subList(i, i+longueurExpression)), expression)) {
                        Object resolu = this.expressions.get(expression).run(expressionArray.subList(i, i+longueurExpression));
                        
                        ArrayList<Object> newArray = new ArrayList<>();
                        newArray.addAll(i != 0 ? expressionArray.subList(0, i) : new ArrayList<>());
                        newArray.add(resolu);
                        newArray.addAll(expressionArray.subList(i+longueurExpression, expressionArray.size()));

                        expressionArray = newArray;

                        //System.out.println(expressionArray);
                    }
                }
            }
        }
    
        return expressionArray;
    }


    public static boolean memeStructure(String line, String structurePotentielle) {
        Pattern structurePattern = Pattern.compile(structurePotentielle.replaceAll("( ?)expression ?", Matcher.quoteReplacement("\\b.+")));
        //System.out.println(programmePotentiel.replaceAll("( ?)expression ?", ".+") + " " + structureProgramme.matcher(line).matches());

        return (structurePattern.matcher(line).matches());
    }
}
