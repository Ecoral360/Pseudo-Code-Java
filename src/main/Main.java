package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import psc.PscLexer;
import psc.PscParser;
import tokens.Token;
import compiler.Compiler;

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
                String line = codeScan.nextLine();
                codeList.add(line);
                Compiler.prochaineCoord("0V12M");
            }
            codeScan.close();

            File configGrammaire = new File("src/regle_et_grammaire/grammaire.txt");

            PscLexer lexer = new PscLexer(configGrammaire);
            PscParser parser = new PscParser();

            for (String line : codeList){
                List<Token> tokens = lexer.lex(line);
                parser.parse(tokens);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("Le fichier programme.txt n'a pas été trouvé");
        }
    } 
}
