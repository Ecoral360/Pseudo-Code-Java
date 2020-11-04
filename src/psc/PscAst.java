package psc;

import tokens.Token;

public interface PscAst<T> {
    


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

        public Object som(){
            if (this.gauche instanceof Entier && this.droite instanceof Entier){
                return new Entier(((Entier) this.gauche).eval() + ((Entier) this.droite).eval());
            } else{
                return new Reel(((Number) this.gauche.eval()).doubleValue() + ((Number) this.droite.eval()).doubleValue());
            }
        }

        public Object sou(){
            if (this.gauche instanceof Entier && this.droite instanceof Entier){
                return new Entier(((Entier) this.gauche).eval() - ((Entier) this.droite).eval());
            } else{
                return new Reel(((Number) this.gauche.eval()).doubleValue() - ((Number) this.droite.eval()).doubleValue());
            }
        }

		public Object mul() {
			if (this.gauche instanceof Entier && this.droite instanceof Entier){
                return new Entier(((Entier) this.gauche).eval() * ((Entier) this.droite).eval());
            } else{
                return new Reel(((Number) this.gauche.eval()).doubleValue() * ((Number) this.droite.eval()).doubleValue());
            }
		}

		public Object div() {
            return new Reel(((Number) this.gauche.eval()).doubleValue() / ((Number) this.droite.eval()).doubleValue());
		}

		public Object mod() {
			if (this.gauche instanceof Entier && this.droite instanceof Entier){
                return new Entier(((Entier) this.gauche).eval() % ((Entier) this.droite).eval());
            } else{
                return new Reel(((Number) this.gauche.eval()).doubleValue() * ((Number) this.droite.eval()).doubleValue());
            }
		}

		public Object exp() {
			if (this.gauche instanceof Entier && this.droite instanceof Entier){
                return new Entier(Math.pow(((Entier) this.gauche).eval(), ((Entier) this.droite).eval()));
            } else{
                return new Reel(Math.pow(((Number) this.gauche.eval()).doubleValue(), ((Number) this.droite.eval()).doubleValue()));
            }
		}
    }

    public T eval();
}
