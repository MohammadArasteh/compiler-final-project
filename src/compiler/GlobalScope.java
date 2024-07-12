package compiler;

import java.util.ArrayList;

public class GlobalScope extends SymbolTable {
    public ArrayList<MethodScope> methods = new ArrayList<MethodScope>();
}