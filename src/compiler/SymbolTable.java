package compiler;

import java.util.Hashtable;
import java.util.Map;

public class SymbolTable {
    public int startLine, stopLine;
    public SymbolTable parentNode;
    public String tableName;
    public Hashtable<String,String> map = new Hashtable<String,String>();

    public SymbolTable(String name) {
        this.tableName = name;
    }

    public String printItems(){
        StringBuilder itemsStr = new StringBuilder();
        for (Map.Entry<String,String> entry : this.map.entrySet()) {
            itemsStr.append("key = ").append(entry.getKey()).append(", value = ").append(entry.getValue()).append("\n");
        }
        return itemsStr.toString();
    }

    public void insert(String idefName, String attributes){
        this.map.put(idefName, attributes);
    }

    public String lookUp(String idefName){
        return this.map.get(idefName);
    }

    public String toString(){
        return "------------- " + this.tableName + ": (" + this.startLine + ", " + this.stopLine + ") -------------\n" +
                this.printItems() +
                "-----------------------------------------\n";
    }
    public void tablePrinter(){
        System.out.print(this);
        System.out.println("==============================================");
        if(this instanceof GlobalScope) {
            for (ClassScope scope : ((GlobalScope) this).classes)
                scope.tablePrinter();
        }
        if(this instanceof ClassScope) {
            for(MethodScope scope: ((ClassScope)this).methods)
                scope.tablePrinter();
        }
        else if(this instanceof MethodScope) {
            for (BlockTable block : ((MethodScope) this).blocks)
                block.tablePrinter();
        }
        else if(this instanceof BlockTable) {
            for (BlockTable block : ((BlockTable) this).blocks)
                block.tablePrinter();
        }
    }

    public String fieldType() {
        if(this instanceof GlobalScope) {
            return "GlobalField";
        }
        else if(this instanceof ClassScope) {
            return "ClassField";
        }
        else if(this instanceof MethodScope){
            return "MethodField";
        }
        else if(this instanceof BlockTable){
            return "BlockField";
        }
        return "";
    }
}
