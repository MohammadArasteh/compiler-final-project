package compiler;

import gen.japyListener;
import gen.japyParser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.TerminalNode;
import utilities.GlobalExtension;

public class ProgramPrinter implements japyListener {

    private int indentation = 0;

    public int getIndentation() {
        return this.indentation;
    }

    public void setIndentation(int indentation) {
        this.indentation = indentation;
    }

    private String className;

    public String getClassName() {
        return this.className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    private String classParent;

    public String getClassParent() {
        return this.classParent;
    }

    public void setClassParent(String classParent) {
        this.classParent = classParent;
    }

    private boolean isEntry;

    public boolean getIsEntry() {
        return this.isEntry;
    }

    public void setIsEntry(boolean isEntry) {
        this.isEntry = isEntry;
    }

    @Override
    public void enterProgram(japyParser.ProgramContext ctx) {
        System.out.println("program start {");
        setIndentation(0);
    }

    @Override
    public void exitProgram(japyParser.ProgramContext ctx) {
        setIndentation(0);
        System.out.println("}");
    }

    @Override
    public void enterClassDeclaration(japyParser.ClassDeclarationContext ctx) {
        setIndentation(1);
        setIsEntry(GlobalExtension.isEntryClass(ctx.methodDeclaration()));

        if (getIsEntry()) return;

        setClassName(ctx.className.getText());
        setClassParent(ctx.classParent == null ? "none" : ctx.classParent.getText());

        System.out.print(GlobalExtension.tabbedString(getIndentation()));
        System.out.printf("class: %s / class parent: %s / isEntry: %b {", getClassName(), this.getClassParent(), getIsEntry());
        System.out.println();
    }

    @Override
    public void exitClassDeclaration(japyParser.ClassDeclarationContext ctx) {
        if (getIsEntry()) return;
        setIndentation(1);
        System.out.println(GlobalExtension.tabbedString(getIndentation()) + "}");
    }

    @Override
    public void enterEntryClassDeclaration(japyParser.EntryClassDeclarationContext ctx) {
        var context = ctx.classDeclaration();

        setIndentation(1);
        setIsEntry(GlobalExtension.isEntryClass(context.methodDeclaration()));
        if (!getIsEntry()) return;

        setClassName(context.className.getText());
        setClassParent(context.classParent == null ? "none" : context.classParent.getText());

        System.out.print(GlobalExtension.tabbedString(getIndentation()));
        System.out.printf("class: %s / class parents: %s / isEntry: %b {", getClassName(), getClassParent(), getIsEntry());
        System.out.println();
    }

    @Override
    public void exitEntryClassDeclaration(japyParser.EntryClassDeclarationContext ctx) {
        var context = ctx.classDeclaration();

        if (!GlobalExtension.isEntryClass(context.methodDeclaration())) return;

        setIndentation(1);
        System.out.println(GlobalExtension.tabbedString(getIndentation()) + "}");
    }

    @Override
    public void enterFieldDeclaration(japyParser.FieldDeclarationContext ctx) {
        setIndentation(2);
        System.out.print(GlobalExtension.tabbedString(getIndentation()));
        System.out.printf("filed: %s / type: %s", ctx.fieldName.getText(), ctx.fieldType.st.getText());
    }

    @Override
    public void exitFieldDeclaration(japyParser.FieldDeclarationContext ctx) {
        setIndentation(2);
        System.out.println();
    }

    @Override
    public void enterAccess_modifier(japyParser.Access_modifierContext ctx) {

    }

    @Override
    public void exitAccess_modifier(japyParser.Access_modifierContext ctx) {

    }

    @Override
    public void enterMethodDeclaration(japyParser.MethodDeclarationContext ctx) {
        setIndentation(2);
        System.out.print(GlobalExtension.tabbedString(getIndentation()));

        String MAIN = "main";
        if (getIsEntry() && ctx.methodName.getText().equals(MAIN)) {
            System.out.printf("main method / type %s {\n", ctx.t.st.getText());
            setIndentation(3);
            return;
        }

        if (ctx.methodName.getText().equals(getClassName())) {
            System.out.printf("class constructor: %s / return type: %s / type: %s {\n", ctx.methodName.getText(),
                    ctx.t.st.getText(), ctx.methodAccessModifier.getText());
        } else {
            System.out.printf("class method: %s / return type: %s / type: %s {\n", ctx.methodName.getText(),
                    ctx.t.st.getText(), ctx.methodAccessModifier.getText());

        }

        GlobalExtension.prepareParameterList(ctx);

        setIndentation(3);
    }

    @Override
    public void exitMethodDeclaration(japyParser.MethodDeclarationContext ctx) {
        setIndentation(2);
        System.out.print(GlobalExtension.tabbedString(getIndentation()) + "}");
        System.out.println();
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
        System.out.printf(GlobalExtension.tabbedString(getIndentation()) + "field: %s / type: local var", ctx.i1.getText());
    }

    @Override
    public void exitStatementVarDef(japyParser.StatementVarDefContext ctx) {
        System.out.println();
    }

    @Override
    public void enterStatementBlock(japyParser.StatementBlockContext ctx) {
        if (!getIsEntry()) {
            System.out.println(GlobalExtension.tabbedString(getIndentation()) + "nested {");
            setIndentation(4);
        }
    }

    @Override
    public void exitStatementBlock(japyParser.StatementBlockContext ctx) {
        if (!getIsEntry()) {
            setIndentation(3);
            System.out.println(GlobalExtension.tabbedString(getIndentation()) + "}");
        }
    }

    @Override
    public void enterStatementContinue(japyParser.StatementContinueContext ctx) {

    }

    @Override
    public void exitStatementContinue(japyParser.StatementContinueContext ctx) {

    }

    @Override
    public void enterStatementBreak(japyParser.StatementBreakContext ctx) {

    }

    @Override
    public void exitStatementBreak(japyParser.StatementBreakContext ctx) {

    }

    @Override
    public void enterStatementReturn(japyParser.StatementReturnContext ctx) {

    }

    @Override
    public void exitStatementReturn(japyParser.StatementReturnContext ctx) {

    }

    @Override
    public void enterStatementClosedLoop(japyParser.StatementClosedLoopContext ctx) {

    }

    @Override
    public void exitStatementClosedLoop(japyParser.StatementClosedLoopContext ctx) {

    }

    @Override
    public void enterStatementOpenLoop(japyParser.StatementOpenLoopContext ctx) {
        System.out.println("nested {");
    }

    @Override
    public void exitStatementOpenLoop(japyParser.StatementOpenLoopContext ctx) {
        System.out.println("}");
    }

    @Override
    public void enterStatementWrite(japyParser.StatementWriteContext ctx) {

    }

    @Override
    public void exitStatementWrite(japyParser.StatementWriteContext ctx) {

    }

    @Override
    public void enterStatementAssignment(japyParser.StatementAssignmentContext ctx) {
        setIndentation(3);

        var leftExpression = ctx.expression().get(0)
                .expressionOr()
                .expressionAnd()
                .expressionEq()
                .expressionCmp()
                .expressionAdd()
                .expressionMultMod()
                .expressionUnary()
                .expressionMethods()
                .expressionOther();

        var rightExpression = ctx.expression().get(1)
                .expressionOr()
                .expressionAnd()
                .expressionEq()
                .expressionCmp()
                .expressionAdd()
                .expressionMultMod()
                .expressionUnary()
                .expressionMethods()
                .expressionOther();

        if (rightExpression.st != null) {
            System.out.println(GlobalExtension.tabbedString(getIndentation()) + String.format("field: %s / type: %s[]", leftExpression.i1.getText(), rightExpression.st.getText()));
        }

        if (rightExpression.i != null) {
            System.out.println(GlobalExtension.tabbedString(getIndentation()) + String.format("field: %s / type: %s", leftExpression.i1.getText(), rightExpression.i.getText()));
        }
    }

    @Override
    public void exitStatementAssignment(japyParser.StatementAssignmentContext ctx) {

    }

    @Override
    public void enterStatementInc(japyParser.StatementIncContext ctx) {

    }

    @Override
    public void exitStatementInc(japyParser.StatementIncContext ctx) {

    }

    @Override
    public void enterStatementDec(japyParser.StatementDecContext ctx) {

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