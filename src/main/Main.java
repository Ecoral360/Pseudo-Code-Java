package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        execute();
    }

    public static void execute() {
        File code = new File("programme.txt");

        try {
            Scanner scanner = new Scanner(code);

            

        } catch (FileNotFoundException e) {
            
            e.printStackTrace();
        }
    } 
}
