package ast;

import java.util.List;

public interface Ast<T> {
    public T run(List<Object> p);
}
