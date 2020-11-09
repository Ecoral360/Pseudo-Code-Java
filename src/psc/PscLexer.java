package psc;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import generateurs.lexer.LexerGenerator;

public class PscLexer extends LexerGenerator {
    public PscLexer(File configGrammaire) throws FileNotFoundException {
        setRegles(configGrammaire);
    }

    private void setRegles(File configGrammaire){
        try {
            Scanner grammaire = new Scanner(configGrammaire);

            String section = "";
            boolean commentaire = false;

            while (grammaire.hasNextLine()) {
                String line = grammaire.nextLine().trim();

                if (line.startsWith("\"\"\"")) {
                    commentaire = ! commentaire;
                }

                if (commentaire || line.isBlank()) {
                    continue;
                } else {
                    if (line.startsWith("[") && line.endsWith("]")) {
                        section = line.substring(1, line.length() - 1);
                        continue;
                    }
                }

                switch (section) {
                    case "Ajouter":
                    String[] elements = line.split("->");
                    ajouterRegle(elements[0].trim(), elements[1].trim());
                    //System.out.println(elements[0].trim() + " " + elements[1].trim().substring(1, elements[1].trim().length()-1));
                    break;

                    case "Ignorer":
                    ignorerRegle(line);
                    //System.out.println(line.substring(1, line.length()-1));
                    break;

                    default:
                    break;
                }
        }

        grammaire.close();

        } catch (FileNotFoundException e) {
            ajouterRegle("AFFICHER", "afficher");
            ajouterRegle("ENTIER", "\\d+");

            ajouterRegle("PLUS", "\\+");
            ajouterRegle("EXP", "\\*{2}");
            ajouterRegle("MUL", "\\*");
            ajouterRegle("MOINS", "\\-");
            ajouterRegle("DIV", "\\\\");
            ajouterRegle("MOD", "mod|[%]");

            ajouterRegle("PARENT_OUV", "[(]");
            ajouterRegle("PARENT_FERM", "[)]");

            ajouterRegle("POINT", "\\.");

            ajouterRegle("CHAINE", "\".*?\"");

            // règles à ignorer
            ignorerRegle("\\s+");
        }
        
    }


    public LexerGenerator build(){
        return (LexerGenerator) this;
    }

}
