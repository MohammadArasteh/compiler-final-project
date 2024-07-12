package utilities;

import compiler.SymbolTable;
import gen.japyParser;

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

    public static String getClassAttributes(japyParser.ClassDeclarationContext ctx) {
        var sb = new StringBuilder();
        sb.append("(name: ").append(ctx.className.getText()).append(")");
        var modifier = ctx.access_modifier();
        sb.append("(accessModifier: ").append(modifier == null ? "public" : modifier.getText()).append(")");
        if(ctx.classParent != null) sb.append("(inherits: class_").append(ctx.classParent.getText()).append(")");
        return sb.toString();
    }
    public static String getFieldAttributes(japyParser.FieldDeclarationContext ctx) {
        var sb = new StringBuilder();
        sb.append("(name: ").append(ctx.fieldName.getText()).append(")");
        var modifier = ctx.access_modifier();
        sb.append("(accessModifier: ").append(modifier == null ? "public" : modifier.getText()).append(")");
        sb.append("(type: ").append(ctx.fieldType.getText()).append(")");
        return sb.toString();
    }
    public static String getMethodAttributes(japyParser.MethodDeclarationContext ctx) {
        var sb = new StringBuilder();
        sb.append("(name: " + ctx.methodName.getText() + ")");
        var modifier = ctx.access_modifier();
        sb.append("(accessModifier: ").append(modifier == null ? "public" : modifier.getText()).append(")");
        sb.append("(return: " + ctx.t.getText() + ")");
        return sb.toString();
    }
    public static String getBlockAttributes(japyParser.StatementBlockContext ctx) {
        return "block attributes";
    }

    public static String getIndentationString(int indent) {
        return "    ".repeat(Math.max(0, indent));
    }
}