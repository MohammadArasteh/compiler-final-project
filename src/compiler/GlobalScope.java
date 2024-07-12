package compiler;

import java.util.ArrayList;
import java.util.Objects;

public class GlobalScope extends SymbolTable {
    public ArrayList<ClassScope> classes = new ArrayList<ClassScope>();

    public GlobalScope(String name) {
        super(name);
    }

    public ClassScope lookupClass(String name) {
        for (ClassScope cs : classes) {
            if(Objects.equals(cs.tableName, name)) return cs;
        }
        return null;
    }
}