package generateurs.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Iterator;
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


    public boolean containsAllInRelativePosition(ArrayList<?> container, ArrayList<?> content){
        ArrayList<?> containerCopy = new ArrayList<>(container);
        if (containerCopy.isEmpty() && content.isEmpty()){
            return true;
        }
        if ((containerCopy.retainAll(content) || containerCopy.equals(content)) && ! (containerCopy.isEmpty() || content.isEmpty())){
            return containerCopy.equals(content);
        }
        return false;
    }


    public void parse(List<Token> listToken){
        String programme = getProgramme(listToken);
        System.out.println(programme);
        ArrayList<ArrayList<Token>> expressions = getExpressions(listToken, programme);
        System.out.println(expressions);
    }


    public String getProgramme(List<Token> listToken){
        String programmeTrouve = null;
        ArrayList<String> structureLine = new ArrayList<>();
        listToken.forEach(e -> structureLine.add(e.getNom()));
        
        for (String programme : this.programmes.keySet()){
            ArrayList<String> structureProgramme = new ArrayList<>(Arrays.asList(programme.split(" ")));
            structureProgramme.removeIf(e -> e.equals("expression"));
            
            if (containsAllInRelativePosition(structureLine, structureProgramme)){
                programmeTrouve = programme;
                break;
            }
        }
        return programmeTrouve;
    }


    public ArrayList<ArrayList<Token>> getExpressions(List<Token> listToken, String programme){
        ArrayList<String> structureLine = new ArrayList<>();
        listToken.forEach(e -> structureLine.add(e.getNom()));


        ArrayList<String> structureProgramme = new ArrayList<>(Arrays.asList(programme.split(" ")));
        structureProgramme.removeIf(e -> e.equals("expression"));
        Iterator<String> iterProgramme = structureProgramme.iterator();

        ArrayList<ArrayList<Token>> expressionsList = new ArrayList<>(); 

        String clef = iterProgramme.hasNext() ? iterProgramme.next() : "";

        ArrayList<Token> expressionList = new ArrayList<>(); 

        for (int i = 0; i < structureLine.size(); ++i){
            if (clef.equals(structureLine.get(i))){
                clef = iterProgramme.hasNext() ? iterProgramme.next() : "";
                expressionsList.add(expressionList);
                expressionList = new ArrayList<>();
            } else {
                expressionList.add(listToken.get(i));
            }
        }
        return expressionsList;
    }


    public void resoudreExpressions(){

    }

}
