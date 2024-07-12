package compiler;

import gen.japyParser;

import java.util.ArrayList;

public class ClassScope extends SymbolTable {
    public ArrayList<MethodScope> methods = new ArrayList<MethodScope>();

    public ClassScope(japyParser.ClassDeclarationContext ctx) {
        super(ctx.getText());
        this.tableName = ctx.className.getText();
        this.startLine = ctx.getStart().getLine();
        this.stopLine = ctx.getStop().getLine();
    }
}
