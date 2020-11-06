package compiler;

import java.util.ArrayList;
import java.util.Hashtable;



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
    private Hashtable<String, String> codeDict = new Hashtable<>();

    String coord = "0M";

    Compiler(ArrayList<String> codeList){
    }

    Compiler(ArrayList<String> codeList, String start){
        this.coord = start;
    }

    public String prochaineCoord(String coord){
        
        return null;
    }
}
