package compiler;

import java.util.ArrayList;



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

    Compiler(ArrayList<String> codeList){
    }

    Compiler(ArrayList<String> codeList, String start){
        this.coord = start;
    }

    public static String prochaineCoord(String coord){
        //System.out.println(Compiler.plusUn(coord));
        return null;
    }

    public static String plusUn(String coord){
        String reste = coord.split("^\\d+")[1];
        String premierNum = coord.replace(reste, "");
        int nextNum = Integer.valueOf(premierNum) + 1;

        return nextNum + reste;
    }
}
