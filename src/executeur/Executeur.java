package executeur;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import psc.PscLexer;
import psc.PscParser;
import tokens.Token;



/*

Système de coordonnées:
    Main:
        0M
        1M
        2M
        ...
        xM

    Dans un bloc "si":
        vrai:
            0VxM
            1VxM
            ...
        faux:
            0FxM
            1FxM
            ...
    


    Dans un bloc "pour"

*/



public class Executeur {

    private static String coord = "<0>main";
    private static Hashtable<String, String> coordDict = new Hashtable<>();


    public static void executer(PscLexer lexer, PscParser parser){
        while (coordDict.containsKey(coord)){
            String line = coordDict.get(coord);

            //System.out.println(coord + " " + line);

            List<Token> tokens = lexer.lex(line.trim());
            parser.parse(tokens);
            Executeur.coordPlusUn();
        }
    }

    public static String obtenirCoord(){
        return coord;
    }

    public static Hashtable<String, String> obtenirCoordDict(){
        return coordDict;
    }

    public static void reset(){
        coord = "<0>main";
    }

    public static String nouveauBloc(String nom){
        coord = "<0>" + nom + coord;
        return coord;
    }

    public static String finBloc(){
        coord = coord.replaceFirst("<\\d+>\\w+", "");
        return coord;
    }

    public static void compiler(ArrayList<String> lines, PscLexer lexer, PscParser parser){
        for (String line: lines){
            String programme = parser.obtenirProgramme(lexer.lex(line.trim()));

            //System.out.println(coord);
            
            coordDict.put(coord, line);

            coord = parser.obtenirProgrammeDict().get(programme).prochaineCoord(coord);
            Executeur.coordPlusUn();
        }
        reset();
    }

    public static String coordPlusUn(){
        String premierNum = coord.substring(coord.indexOf("<")+1, coord.indexOf(">"));
        int nextNum = Integer.valueOf(premierNum) + 1;
        coord = "<" + nextNum + coord.substring(coord.indexOf(">"));
        return coord;
    }

    public static String coordMoinsUn(){
        String premierNum = coord.substring(coord.indexOf("<")+1, coord.indexOf(">"));
        int nextNum = Integer.valueOf(premierNum) - 1;
        coord = "<" + nextNum + coord.substring(coord.indexOf(">"));
        return coord;
    }
}
