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


    class BinaryOp{
        PscAst<?> gauche, droite;

        BinaryOp(Object gauche, Object droite){
            this.gauche = (PscAst<?>) gauche;
            this.droite = (PscAst<?>) droite;
        }

        public Object som(){
            if (this.gauche instanceof Entier && this.droite instanceof Entier){
                return ((Entier) this.gauche).eval() + ((Entier) this.droite).eval();
            } else{
                return ((Number) this.gauche.eval()).doubleValue() + ((Number) this.droite.eval()).doubleValue();
            }
        }
    }

    public T eval();
}
