package compiler;

import gen.japyListener;
import gen.japyParser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.RuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;
import utilities.GlobalExtension;

import java.util.ArrayList;
import java.util.Stack;
import java.util.stream.Collectors;

public class ProgramPrinter  implements japyListener {
    ErrorHandler errorHandler = new ErrorHandler();
    GlobalScope scope = new GlobalScope();
    Stack<SymbolTable> scopes = new Stack<SymbolTable>();
    private String returnType;
    int indentation = 0;

    int program_counter = 1;
    Stack<Number> loopsStartingLines = new Stack<>();
    Stack<Number> loopsEndingLines = new Stack<>();

    private void indentedPrintf(String str, Object... args) {
        System.out.printf(this.program_counter + ".\t" + this.indentedString(str) + "\n", args);
        program_counter++;
    }
    private void indentedPrint(String str) {
        System.out.print(this.program_counter + ".\t" + this.indentedString(str) + "\n");
        program_counter++;
    }
    private String indentedString(String str) {
        return GlobalExtension.getIndentationString(indentation) + str;
    }

    @Override
    public void enterProgram(japyParser.ProgramContext ctx) {

    }

    @Override
    public void exitProgram(japyParser.ProgramContext ctx) {
    }

    @Override
    public void enterClassDeclaration(japyParser.ClassDeclarationContext ctx) {
        indentation++;
        StringBuilder result = new StringBuilder("<class '%s'");
        if(ctx.access_modifier() != null) result.append(", %s");
        if(ctx.classParent != null) result.append(", inherits '%s'");
        if(ctx.access_modifier() != null && ctx.classParent != null)
            indentedPrintf(result.toString(), ctx.className.getText(), ctx.access_modifier().getText(), ctx.classParent.getText());
        else if(ctx.access_modifier() == null && ctx.classParent != null)
            indentedPrintf(result.toString(), ctx.className.getText() , ctx.classParent.getText());
        else if(ctx.access_modifier() != null && ctx.classParent == null) {
            indentedPrintf(result.toString(), ctx.className.getText() , ctx.access_modifier().getText());
        }
        else indentedPrintf(result.toString(), ctx.className.getText());
    }

    @Override
    public void exitClassDeclaration(japyParser.ClassDeclarationContext ctx) {
        indentedPrintf("</class>");
        indentation--;
    }

    @Override
    public void enterEntryClassDeclaration(japyParser.EntryClassDeclarationContext ctx) {

    }

    @Override
    public void exitEntryClassDeclaration(japyParser.EntryClassDeclarationContext ctx) {

    }

    @Override
    public void enterFieldDeclaration(japyParser.FieldDeclarationContext ctx) {
        indentation++;
        var modifier = "public";
        if(ctx.access_modifier() != null) modifier = ctx.access_modifier().getText();
        var names = new ArrayList<String>();
        ctx.ID().forEach(id -> names.add(id.getText()));
        indentedPrintf(String.join(", ", names) + ": (field, %s, %s)", modifier, ctx.fieldType.getText());
    }

    @Override
    public void exitFieldDeclaration(japyParser.FieldDeclarationContext ctx) {
        indentation--;
    }

    @Override
    public void enterAccess_modifier(japyParser.Access_modifierContext ctx) {

    }

    @Override
    public void exitAccess_modifier(japyParser.Access_modifierContext ctx) {

    }

    @Override
    public void enterMethodDeclaration(japyParser.MethodDeclarationContext ctx) {
        indentation++;
        var modifier = "public";
        if(ctx.access_modifier() != null) modifier = ctx.access_modifier().getText();
        var params = new ArrayList<String>();
        var ids = ctx.ID();
        var types = ctx.japyType();
        ids.remove(0);
        returnType = ctx.t.getText();
        types.remove(types.size() -1);
        for(var i = 0; i < ids.size(); i++)
            params.add("(" + ids.get(i) + ", " + types.get(i).getText() + ")");
        indentedPrintf("<function '%s, %s, parameters: %s'>", ctx.methodName.getText(), modifier, params);
        indentation++;
    }

    @Override
    public void exitMethodDeclaration(japyParser.MethodDeclarationContext ctx) {
        indentation--;
        indentedPrintf("</function %s>", returnValue);
        indentation--;
    }

    @Override
    public void enterClosedStatement(japyParser.ClosedStatementContext ctx) {
    }

    @Override
    public void exitClosedStatement(japyParser.ClosedStatementContext ctx) {

    }


    @Override
    public void enterClosedConditional(japyParser.ClosedConditionalContext ctx) {

    }

    @Override
    public void exitClosedConditional(japyParser.ClosedConditionalContext ctx) {

    }

    @Override
    public void enterOpenConditional(japyParser.OpenConditionalContext ctx) {
    }

    @Override
    public void exitOpenConditional(japyParser.OpenConditionalContext ctx) {

    }

    @Override
    public void enterIf(japyParser.IfContext ctx) {
        indentedPrint("<if condition: " + ctx.ifExp.getText());
        indentation++;
    }

    @Override
    public void exitIf(japyParser.IfContext ctx) {
        indentation--;
        indentedPrint("</if>");
    }

    @Override
    public void enterElif(japyParser.ElifContext ctx) {
        indentedPrint("<elif condition: " + ctx.elifExp.getText() + ">");
        indentation++;
    }

    @Override
    public void exitElif(japyParser.ElifContext ctx) {
        indentation--;
        indentedPrint("</elif>");
    }

    @Override
    public void enterCloseElse(japyParser.CloseElseContext ctx) {
        indentedPrint("<else>");
        indentation++;
    }

    @Override
    public void exitCloseElse(japyParser.CloseElseContext ctx) {
        indentation--;
        indentedPrint("</else>");
    }

    @Override
    public void enterOpenElse(japyParser.OpenElseContext ctx) {
        indentedPrint("<else>");
        indentation++;
    }

    @Override
    public void exitOpenElse(japyParser.OpenElseContext ctx) {
        indentation--;
        indentedPrint("</else>");
    }

    @Override
    public void enterOpenStatement(japyParser.OpenStatementContext ctx) {

    }

    @Override
    public void exitOpenStatement(japyParser.OpenStatementContext ctx) {

    }

    @Override
    public void enterStatement(japyParser.StatementContext ctx) {

    }

    @Override
    public void exitStatement(japyParser.StatementContext ctx) {

    }

    @Override
    public void enterStatementVarDef(japyParser.StatementVarDefContext ctx) {
        var ids = ctx.ID();
        var expressions = ctx.expression();
        indentedPrintf(
                ids.stream().map(ParseTree::getText).collect(Collectors.joining())
                        + ": "
                        + expressions.stream().map(RuleContext::getText).collect(Collectors.joining(", "))
                        + " -> (var)");
    }

    @Override
    public void exitStatementVarDef(japyParser.StatementVarDefContext ctx) {
    }

    @Override
    public void enterStatementBlock(japyParser.StatementBlockContext ctx) {

    }

    @Override
    public void exitStatementBlock(japyParser.StatementBlockContext ctx) {

    }

    @Override
    public void enterStatementContinue(japyParser.StatementContinueContext ctx) {
        indentedPrintf("goto %s", loopsStartingLines.peek());
    }

    @Override
    public void exitStatementContinue(japyParser.StatementContinueContext ctx) {

    }

    @Override
    public void enterStatementBreak(japyParser.StatementBreakContext ctx) {
        indentedPrintf("goto %s", loopsEndingLines.peek());
    }

    @Override
    public void exitStatementBreak(japyParser.StatementBreakContext ctx) {

    }

    String returnValue;
    @Override
    public void enterStatementReturn(japyParser.StatementReturnContext ctx) {
        returnValue = "return (" + ctx.e.getText() + ", " + returnType + ")";
    }

    @Override
    public void exitStatementReturn(japyParser.StatementReturnContext ctx) {

    }

    @Override
    public void enterStatementClosedLoop(japyParser.StatementClosedLoopContext ctx) {
        loopsStartingLines.push(ctx.getStart().getLine());
        loopsEndingLines.push(ctx.getStop().getLine());
        indentedPrint("<while condition: <" + ctx.e.getText() + ">>");
        indentation++;
    }

    @Override
    public void exitStatementClosedLoop(japyParser.StatementClosedLoopContext ctx) {
        loopsStartingLines.pop();
        loopsEndingLines.pop();
        indentation--;
        indentedPrint("</while>");
    }

    @Override
    public void enterStatementOpenLoop(japyParser.StatementOpenLoopContext ctx) {
        loopsStartingLines.push(ctx.getStart().getLine());
        loopsEndingLines.push(ctx.getStop().getLine());
        indentedPrint("<while condition: <" + ctx.e.getText() + ">>");
        indentation++;
    }

    @Override
    public void exitStatementOpenLoop(japyParser.StatementOpenLoopContext ctx) {
        loopsStartingLines.pop();
        loopsEndingLines.pop();
        indentation--;
        indentedPrint("</while>");
    }

    @Override
    public void enterStatementWrite(japyParser.StatementWriteContext ctx) {

    }

    @Override
    public void exitStatementWrite(japyParser.StatementWriteContext ctx) {

    }

    @Override
    public void enterStatementAssignment(japyParser.StatementAssignmentContext ctx) {
        indentedPrint(ctx.right.getText() + " -> " + ctx.left.getText());
    }

    @Override
    public void exitStatementAssignment(japyParser.StatementAssignmentContext ctx) {
    }

    @Override
    public void enterStatementInc(japyParser.StatementIncContext ctx) {
        indentedPrintf("%s + 1 -> %s", ctx.lvalExpr.getText(), ctx.lvalExpr.getText());
    }

    @Override
    public void exitStatementInc(japyParser.StatementIncContext ctx) {
    }

    @Override
    public void enterStatementDec(japyParser.StatementDecContext ctx) {
        indentedPrintf("%s - 1 -> %s", ctx.lvalExpr.getText(), ctx.lvalExpr.getText());
    }

    @Override
    public void exitStatementDec(japyParser.StatementDecContext ctx) {
    }

    @Override
    public void enterExpression(japyParser.ExpressionContext ctx) {
    }

    @Override
    public void exitExpression(japyParser.ExpressionContext ctx) {
    }

    @Override
    public void enterExpressionOr(japyParser.ExpressionOrContext ctx) {

    }

    @Override
    public void exitExpressionOr(japyParser.ExpressionOrContext ctx) {

    }

    @Override
    public void enterExpressionOrTemp(japyParser.ExpressionOrTempContext ctx) {

    }

    @Override
    public void exitExpressionOrTemp(japyParser.ExpressionOrTempContext ctx) {

    }

    @Override
    public void enterExpressionAnd(japyParser.ExpressionAndContext ctx) {

    }

    @Override
    public void exitExpressionAnd(japyParser.ExpressionAndContext ctx) {

    }

    @Override
    public void enterExpressionAndTemp(japyParser.ExpressionAndTempContext ctx) {

    }

    @Override
    public void exitExpressionAndTemp(japyParser.ExpressionAndTempContext ctx) {

    }

    @Override
    public void enterExpressionEq(japyParser.ExpressionEqContext ctx) {

    }

    @Override
    public void exitExpressionEq(japyParser.ExpressionEqContext ctx) {

    }

    @Override
    public void enterExpressionEqTemp(japyParser.ExpressionEqTempContext ctx) {

    }

    @Override
    public void exitExpressionEqTemp(japyParser.ExpressionEqTempContext ctx) {

    }

    @Override
    public void enterExpressionCmp(japyParser.ExpressionCmpContext ctx) {

    }

    @Override
    public void exitExpressionCmp(japyParser.ExpressionCmpContext ctx) {

    }

    @Override
    public void enterExpressionCmpTemp(japyParser.ExpressionCmpTempContext ctx) {

    }

    @Override
    public void exitExpressionCmpTemp(japyParser.ExpressionCmpTempContext ctx) {

    }

    @Override
    public void enterExpressionAdd(japyParser.ExpressionAddContext ctx) {

    }

    @Override
    public void exitExpressionAdd(japyParser.ExpressionAddContext ctx) {

    }

    @Override
    public void enterExpressionAddTemp(japyParser.ExpressionAddTempContext ctx) {

    }

    @Override
    public void exitExpressionAddTemp(japyParser.ExpressionAddTempContext ctx) {

    }

    @Override
    public void enterExpressionMultMod(japyParser.ExpressionMultModContext ctx) {

    }

    @Override
    public void exitExpressionMultMod(japyParser.ExpressionMultModContext ctx) {

    }

    @Override
    public void enterExpressionMultModTemp(japyParser.ExpressionMultModTempContext ctx) {

    }

    @Override
    public void exitExpressionMultModTemp(japyParser.ExpressionMultModTempContext ctx) {

    }

    @Override
    public void enterExpressionUnary(japyParser.ExpressionUnaryContext ctx) {

    }

    @Override
    public void exitExpressionUnary(japyParser.ExpressionUnaryContext ctx) {

    }

    @Override
    public void enterExpressionMethods(japyParser.ExpressionMethodsContext ctx) {

    }

    @Override
    public void exitExpressionMethods(japyParser.ExpressionMethodsContext ctx) {

    }

    @Override
    public void enterExpressionMethodsTemp(japyParser.ExpressionMethodsTempContext ctx) {

    }

    @Override
    public void exitExpressionMethodsTemp(japyParser.ExpressionMethodsTempContext ctx) {

    }

    @Override
    public void enterExpressionOther(japyParser.ExpressionOtherContext ctx) {

    }

    @Override
    public void exitExpressionOther(japyParser.ExpressionOtherContext ctx) {

    }

    @Override
    public void enterJapyType(japyParser.JapyTypeContext ctx) {

    }

    @Override
    public void exitJapyType(japyParser.JapyTypeContext ctx) {

    }

    @Override
    public void enterSingleType(japyParser.SingleTypeContext ctx) {

    }

    @Override
    public void exitSingleType(japyParser.SingleTypeContext ctx) {

    }

    @Override
    public void visitTerminal(TerminalNode terminalNode) {

    }

    @Override
    public void visitErrorNode(ErrorNode errorNode) {

    }

    @Override
    public void enterEveryRule(ParserRuleContext parserRuleContext) {

    }

    @Override
    public void exitEveryRule(ParserRuleContext parserRuleContext) {

    }
}