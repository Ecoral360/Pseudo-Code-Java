package executeur;

import java.util.ArrayList;
import java.util.Hashtable;

import psc.PscLexer;
import psc.PscParser;



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

    public static void mettreCoord(String start){
        coord = start;
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


    public static void nouveauBloc(String nom){
        coord = "<0>" + nom + coord;
    }

    public static void finBloc(){
        coord = coord.replaceFirst("<.*(?=<)", "");
    }

    public static void compile(ArrayList<String> lines, PscLexer lexer, PscParser parser){
        for (String line: lines){
            String programme = parser.getProgramme(lexer.lex(line));

            System.out.println(coord);

            switch (programme){
                case "SI expression ALORS":

                break;

            
                default:
                coordDict.put(coord, line);
                coord = Executeur.plusUn(coord);
                break;
            }
        }
    }


    public static String obtenirProchaineCoordonnee(){
        coord = Executeur.plusUn(coord);
        return coord;
    }

    public static String plusUn(String coord){
        String premierNum = coord.substring(coord.indexOf("<")+1, coord.indexOf(">"));
        int nextNum = Integer.valueOf(premierNum) + 1;
        return "<" + nextNum + coord.substring(coord.indexOf(">"));
    }
}
