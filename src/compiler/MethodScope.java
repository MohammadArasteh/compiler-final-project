package compiler;

import gen.japyParser;

import java.util.ArrayList;


public class MethodScope extends SymbolTable {
    public ArrayList<BlockTable> blocks = new ArrayList<BlockTable>();

    public MethodScope(japyParser.MethodDeclarationContext ctx) {
        super(ctx.getText());
        this.tableName = ctx.methodName.getText();
        this.startLine = ctx.getStart().getLine();
        this.stopLine = ctx.getStop().getLine();
    }
}
