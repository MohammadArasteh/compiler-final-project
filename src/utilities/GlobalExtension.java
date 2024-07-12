package utilities;

import compiler.SymbolTable;

public class GlobalExtension {

    public static SymbolTable findItemScope(String itemKey, SymbolTable currentScope) {
        SymbolTable st = currentScope;
        while (st != null) {
            if (st.map.containsKey(itemKey)){
                return st;
            }
            st = st.parentNode;
        }
        return st;
    }

    public static String findItemValue(String itemKey, SymbolTable currentSco
    ) {
        var scope = findItemScope(itemKey, currentSco);
        return scope.lookUp(itemKey);
    }

    public static boolean doesKeyExistInScope(String itemKey, SymbolTable currentSco) {
        return findItemScope(itemKey, currentSco) != null;
    }

    public static String getIndentationString(int indent) {
        return "    ".repeat(Math.max(0, indent));
    }
}