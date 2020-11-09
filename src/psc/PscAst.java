package psc;

import java.util.ArrayList;

import tokens.Token;

public interface PscAst<T> {
    
    class VariableManager {
        ArrayList<Variable> varDispo = new ArrayList<>();

        VariableManager() {

        }


    }

    class Variable {
        Variable() {

        }
    }

    class Entier implements PscAst<Integer>{
        private int valeur;
        
        Entier(Token valeur){
            this.valeur = Integer.parseInt(valeur.getValeur());
        }

        Entier(Number valeur){
            this.valeur = valeur.intValue();
        }

        @Override
        public Integer eval() {
            return valeur;
        }

    }

    class Chaine implements PscAst<String>{
        private String valeur;
        
        Chaine(Token valeur){
            this.valeur = valeur.getValeur().replaceAll("\"", "");
        }

        Chaine(Object valeur){
            this.valeur = String.valueOf(valeur);
        }

        Chaine(Number valeur){
            this.valeur = String.valueOf(valeur.intValue());
        }

        @Override
        public String eval() {
            return valeur;
        }
    }

    class Reel implements PscAst<Double>{
        private double valeur;
        
        Reel(Token valeur1, Token valeur2){
            this.valeur = Double.parseDouble(valeur1.getValeur() + "." + valeur2.getValeur());
        }

        Reel(Number valeur){
            this.valeur = valeur.doubleValue();
        }

        @Override
        public Double eval() {
            return valeur;
        }

    }


    class BinaryOp{
        PscAst<?> gauche, droite;

        BinaryOp(Object gauche, Object droite){
            this.gauche = (PscAst<?>) gauche;
            this.droite = (PscAst<?>) droite;
        }

        public Object concat(){
            return new Chaine(String.valueOf(this.gauche.eval()) + String.valueOf(this.droite.eval()));
        }

        public Object repeat(){
            return new Chaine(String.valueOf(this.gauche.eval()).repeat( (Integer) this.droite.eval() ));
        }

        public Object som(){
            return (this.gauche instanceof Entier && this.droite instanceof Entier) ?
                    new Entier(((Entier) this.gauche).eval() + ((Entier) this.droite).eval()) :
                    new Reel(((Number) this.gauche.eval()).doubleValue() + ((Number) this.droite.eval()).doubleValue());
            
        }

        public Object sou(){
            return (this.gauche instanceof Entier && this.droite instanceof Entier) ?
                    new Entier(((Entier) this.gauche).eval() - ((Entier) this.droite).eval()) :
                    new Reel(((Number) this.gauche.eval()).doubleValue() - ((Number) this.droite.eval()).doubleValue());
        }

		public Object mul() {
			return (this.gauche instanceof Entier && this.droite instanceof Entier) ?
                    new Entier(((Entier) this.gauche).eval() * ((Entier) this.droite).eval()) :
                    new Reel(((Number) this.gauche.eval()).doubleValue() * ((Number) this.droite.eval()).doubleValue());
		}

		public Object div() {
            return new Reel(((Number) this.gauche.eval()).doubleValue() / ((Number) this.droite.eval()).doubleValue());
		}

		public Object mod() {
		    return new Entier(((Entier) this.gauche).eval() % ((Entier) this.droite).eval());
		}

		public Object exp() {
			return (this.gauche instanceof Entier && this.droite instanceof Entier) ?
                    new Entier(Math.pow(((Entier) this.gauche).eval(), ((Entier) this.droite).eval())):
                    new Reel(Math.pow(((Number) this.gauche.eval()).doubleValue(), ((Number) this.droite.eval()).doubleValue()));
		}
    }

    public T eval();
}
