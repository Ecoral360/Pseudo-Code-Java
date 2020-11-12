package generateurs.lexer;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import generateurs.lexer.regle.Regle;
import tokens.Token;

public class LexerGenerator {
    private ArrayList<Regle> reglesAjoutees = new ArrayList<>();
    private ArrayList<Regle> reglesIgnorees = new ArrayList<>();
    
    public LexerGenerator(){
    }

    protected void chargerRegles(File configGrammaire){
        try {
            Scanner grammaire = new Scanner(configGrammaire);

            String section = "";
            String categorie = "";
            boolean commentaire = false;

            while (grammaire.hasNextLine()) {
                String line = grammaire.nextLine().trim();

                if (line.startsWith("\"\"\"")) {
                    commentaire = ! commentaire;
                }

                if (commentaire || line.isBlank() || line.startsWith("#")) {
                    continue;
                } else {
                    if (line.startsWith("[") && line.endsWith("]")) {
                        section = line.substring(1, line.length() - 1);
                        continue;
                    }
                    if (line.endsWith("{")) {
                        categorie = line.substring(0, line.length() - 1).trim();
                        continue;
                    }
                    if (line.endsWith("}")) {
                        categorie = "";
                        continue;
                    }
                }

                switch (section) {
                    case "Ajouter":
                    String[] elements = line.split("->", 2);
                    
                    ajouterRegle(elements[0].trim(), elements[1].trim(), categorie);
                    //System.out.println(elements[0].trim() + " " + elements[1].trim().substring(1, elements[1].trim().length()-1));
                    break;

                    case "Ignorer":
                    ignorerRegle(line);
                    //System.out.println(line);
                    break;

                    default:
                    break;
                }
        }

        grammaire.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    protected void ajouterRegle(String nom, String pattern, String categorie){
        this.reglesAjoutees.add(new Regle(nom, pattern, categorie));
    }

    protected void sortRegle(){
        Collections.sort(this.reglesAjoutees, new Comparator<Regle>(){
            @Override
            public int compare(Regle o1, Regle o2) {
                return o2.getPattern().length() - o1.getPattern().length();
            }
            
        });
    }

    protected void ignorerRegle(String pattern){
        this.reglesIgnorees.add(new Regle(pattern));
    }

    public ArrayList<Regle> getReglesAjoutees(){
        return this.reglesAjoutees;
    }

    public ArrayList<Regle> getReglesIgnorees(){
        return this.reglesIgnorees;
    }


    public List<Token> lex(String s){

        List<Token> tokenList = new ArrayList<>();

        int idx = 0;

        while (idx < s.length()){
            
            while (true){
                boolean trouve = false;
                for (Regle regle : this.getReglesIgnorees()){
                    Matcher match = Pattern.compile(regle.getPattern()).matcher(s);
                    if (match.find(idx) && match.start() == idx){
                        idx = match.end();
                        trouve = true;
                        break;
                    }
                }
                if (! trouve){
                    break;
                }
            }
            boolean trouve = false;
            for (Regle regle : this.getReglesAjoutees()){
                Matcher match = Pattern.compile(regle.getPattern()).matcher(s);
                    if (match.find(idx) && match.start() == idx){
                        tokenList.add(new Token(regle.getNom(), s.substring(match.start(), match.end())));
                        idx = match.end();
                        trouve = true;
                        break;
                    }
            }
            if (! trouve){
                throw new Error("Lexing Error");
            }
        }

        return tokenList;
    }
}
