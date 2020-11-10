package generateurs.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import ast.Ast;
import tokens.Token;

public class ParserGenerator {
    Hashtable<String, Ast<?>> programmes = new Hashtable<>();
    ArrayList<String> ordreProgrammes = new ArrayList<>();

    Hashtable<String, Ast<?>> expressions = new Hashtable<>();
    ArrayList<String> ordreExpressions = new ArrayList<>();

    public ParserGenerator(){

    }
 
    protected void ajouterProgramme(String nom, Ast<?> fonction) { 
        /*
            importance : 0 = plus important
            si plusieurs programmes ont la même importance, le dernier ajouté sera priorisé
        */
        this.programmes.put(nom, fonction);

    }

    protected void ajouterExpression(String nom, Ast<?> fonction) {  
        /*
            importance : 0 = plus important
            si plusieurs expressions ont la même importance, la dernière ajoutée sera priorisée
        */
        this.expressions.put(nom, fonction);
    }

    protected void setOrdreProgramme(){
        for (int i = 0; i < this.programmes.size(); ++i){
            this.ordreProgrammes.add("");
        }
        for (String nom : this.programmes.keySet()){
            int importance = this.programmes.get(nom).getImportance();
            if (importance == -1){
                this.ordreProgrammes.add(nom);
            } else {
                if (this.ordreProgrammes.get(importance).isEmpty()){
                    this.ordreProgrammes.set(importance, nom);
                } else {
                    this.ordreProgrammes.add(importance, nom);
                }
                
            }
        }
        this.ordreProgrammes.removeIf(e -> e.isBlank());
        //System.out.println(this.ordreProgrammes);
    }

    protected void setOrdreExpression(){
        for (int i = 0; i < this.expressions.size(); ++i){
            this.ordreExpressions.add("");
        }
        for (String nom : this.expressions.keySet()){
            int importance = this.expressions.get(nom).getImportance();
            if (importance == -1){
                this.ordreExpressions.add(nom);
            } else {
                if (this.ordreExpressions.get(importance).isEmpty()){
                    this.ordreExpressions.set(importance, nom);
                } else {
                    this.ordreExpressions.add(importance, nom);
                }
                
            }
        }
        this.ordreExpressions.removeIf(e -> e.isBlank());
        //System.out.println(this.ordreExpressions);
    }



    public boolean containsAllInRelativePosition(ArrayList<?> container, ArrayList<?> content) {
        ArrayList<?> containerCopy = new ArrayList<>(container);
        if (containerCopy.isEmpty() && content.isEmpty()) {
            return true;
        }
        if ((containerCopy.retainAll(content) || containerCopy.equals(content)) && ! (containerCopy.isEmpty() || content.isEmpty())) {
            return containerCopy.equals(content);
        }
        return false;
    }


    public Object parse(List<Token> listToken){
        //System.out.println("\n");

        String programme = getProgramme(listToken);
        //System.out.println("Programme trouvé: " + programme);

        ArrayList<ArrayList<Token>> expressions = getExpressions(listToken, programme);
        //System.out.println("Expression trouvée: " + expressions);

        List<Object> expressionsResolues = new ArrayList<>();

        for (ArrayList<Token> expression : expressions){
            ArrayList<Object> resolu = resoudreExpressions(expression);
            if (resolu.size() > 1){
                throw new Error("Expression invalide: " + resolu);
            } else{
                expressionsResolues.add(resolu.get(0));
            }
            
            //System.out.println("Expressions résolues: " + resolu.get(0));
        }
        return this.programmes.get(programme).run(expressionsResolues);
    }


    public String getProgramme(List<Token> listToken) {
        String programmeTrouve = null;
        ArrayList<String> structureLine = new ArrayList<>();
        listToken.forEach(e -> structureLine.add(e.getNom()));

        for (String programme : this.ordreProgrammes){
            ArrayList<String> structureProgramme = new ArrayList<>(Arrays.asList(programme.split(" ")));
            structureProgramme.removeIf(e -> e.equals("expression"));
            
            if (containsAllInRelativePosition(structureLine, structureProgramme)) {
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
        
        for (String expression : this.ordreExpressions){

            List<String> expressionNom = new ArrayList<>();
            expressionArray.forEach(e -> expressionNom.add(e instanceof Token ? ((Token) e).getNom() : "expression"));
            //System.out.println("Nom " + expressionNom);

            ArrayList<String> structureExpression = new ArrayList<>(Arrays.asList(expression.split(" ")));

            int longueurExpression = structureExpression.size();

            if (expressionArray.size() >= longueurExpression){
                
                for (int i = 0; i + longueurExpression <= expressionArray.size(); ++i){
                    if (expressionNom.subList(i, i+longueurExpression).equals(structureExpression)){
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
}
