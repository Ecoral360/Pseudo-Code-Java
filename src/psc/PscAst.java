package psc;

import java.util.Hashtable;

import tokens.Token;

public interface PscAst<T> {
    
    class VariableManager {
        static Hashtable<String, Hashtable<String, Variable>> varDict = new Hashtable<>();
        
        static String varDispo = "<main>";

        VariableManager() {
            VariableManager.varDict.putIfAbsent("<main>", new Hashtable<String, Variable>());
        }

        public static void changerValeur(String nom, PscAst<?> newValeur) {
            Variable var = VariableManager.varDict.get(VariableManager.varDispo).get(nom);
            PscAst<?> oldValeur = var.getValeur();
            
            if (oldValeur.getClass() == newValeur.getClass()){
                var.setValeur(newValeur);
                VariableManager.varDict.get(VariableManager.varDispo).put(nom, var);

            } else{
                throw new Error("La variable '" + 
                                nom + 
                                "' de type '" + 
                                oldValeur.getClass() + 
                                "' ne peut pas prendre une valeur de type '" + 
                                newValeur.getClass() + 
                                "'.");
            }
            
        }


    }

    class Variable implements PscAst<Object>{
        
        private String nom;
        private PscAst<?> valeur = null;

        
        Variable(String nom, PscAst<?> valeur) {
            this.nom = nom;
            if (VariableManager.varDict.get(VariableManager.varDispo).containsKey(this.nom)){
                VariableManager.changerValeur(nom, valeur);
            } else {
                this.valeur = valeur;
                VariableManager.varDict.get(VariableManager.varDispo).put(this.nom, this);
            }
        }

        public void setValeur(PscAst<?> valeur){
            this.valeur = valeur;
        }

        public String getNom(){
            return this.nom;
        }

        public PscAst<?> getValeur(){
            return this.valeur;
        }

        public Object eval(){
            return this.valeur.eval();
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

    class Reel implements PscAst<Double>{
        private double valeur;
        
        Reel(Token valeur){
            this.valeur = Double.parseDouble(valeur.getValeur());
        }

        Reel(Number valeur){
            this.valeur = valeur.doubleValue();
        }

        @Override
        public Double eval() {
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

    class Booleen implements PscAst<Boolean>{
        private boolean valeur;
        
        Booleen(Token valeur){
            this.valeur = valeur.getValeur().equals("vrai");
        }

        Booleen(Boolean valeur){
            this.valeur = valeur.booleanValue();
        }

        @Override
        public Boolean eval() {
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

    class BinaryComp{
        PscAst<?> gauche, droite;

        BinaryComp(Object gauche, Object droite){
            this.gauche = (PscAst<?>) gauche;
            this.droite = (PscAst<?>) droite;
        }

        public Booleen egal(){
            return new Booleen(this.gauche.eval().equals(this.droite.eval()));
        }

        public Booleen pasEgal(){
            return new Booleen(! this.gauche.eval().equals(this.droite.eval()));
        }

        public Booleen plusGrand(){
            return new Booleen(((Number) this.gauche.eval()).doubleValue() > ((Number) this.droite.eval()).doubleValue());
        }

        public Booleen plusPetit(){

            return new Booleen(((Number) this.gauche.eval()).doubleValue() < ((Number) this.droite.eval()).doubleValue());
        }

        public Booleen plusGrandEgal(){
            return new Booleen(((Number) this.gauche.eval()).doubleValue() >= ((Number) this.droite.eval()).doubleValue());
        }

        public Booleen plusPetitEgal(){
            return new Booleen(((Number) this.gauche.eval()).doubleValue() <= ((Number) this.droite.eval()).doubleValue());
        }
    }

    class porteLogique{
        Booleen gauche, droite;

        porteLogique(Object gauche, Object droite){
            this.gauche = (Booleen) gauche;
            this.droite = (Booleen) droite;
        }

        porteLogique(Object gauche){
            this.gauche = (Booleen) gauche;
        }

        public Booleen et(){
            return new Booleen(Boolean.logicalAnd(this.gauche.eval(), this.droite.eval()));
        }

        public Booleen ou(){
            return new Booleen(Boolean.logicalOr(this.gauche.eval(), this.droite.eval()));
        }

        public Booleen pas(){
            return new Booleen(! this.gauche.eval());
        }
    }

    public T eval();
}
