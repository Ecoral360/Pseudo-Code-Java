package ast;

import java.util.List;

public class Ast<T> {
    private int importance = -1;

    public Ast(){}
    
    public Ast(int importance){
        this.importance = importance;
    }


    public int getImportance() {
        return this.importance;
    }

    public T run(List<Object> p){
        return null;
    }
}
