package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import psc.PscLexer;
import psc.PscParser;
import tokens.Token;

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
