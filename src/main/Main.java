package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import psc.PscLexer;

public class Main {
    public static void main(String[] args) {
        execute();
    }

    public static void execute() {
        File codeFile = new File("programme.txt");
        
        ArrayList<String> codeList = new ArrayList<>();

        try {
            Scanner codeScan = new Scanner(codeFile);

            while (codeScan.hasNextLine()){
                codeList.add(codeScan.nextLine());
            }
            codeScan.close();

            PscLexer lexer = new PscLexer();

            for (String line : codeList){
                lexer.lex(line).forEach(token -> System.out.println(token.getNom() + "  " + token.getValeur()));
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("Le fichier programme.txt n'a pas été trouvé");
        }
    } 
}
