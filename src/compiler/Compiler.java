package compiler;

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



public class Compiler {

    String coord = "0M";
    Hashtable<String, String> coordDict = new Hashtable<>();

    public Compiler(){
    }

    public Compiler(String start){
        this.coord = start == null ? this.coord : start;
    }

    public String getDictCoord(ArrayList<String> lines, PscLexer lexer, PscParser parser){
        for (String line: lines){
            String programme = parser.getProgramme(lexer.lex(line));

            switch (programme){
                case "SI expression ALORS":

                break;

                

                default:
                this.coordDict.put(this.coord, line);
                this.coord = Compiler.plusUn(this.coord);
                break;
            }
        }
        
        return null;
    }

    public static String plusUn(String coord){
        String reste = coord.split("^\\d+")[1];
        String premierNum = coord.replace(reste, "");
        int nextNum = Integer.valueOf(premierNum) + 1;
        return nextNum + reste;
    }
}
