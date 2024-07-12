package compiler;

import java.util.Hashtable;
import java.util.Map;

public class SymbolTable {
    public int lineNumber = 0;
    public SymbolTable parentNode;
    public String tableName="";
    public Hashtable<String,String> map = new Hashtable<String,String>();

    public String printItems(){

        StringBuilder itemsStr = new StringBuilder();
        for (Map.Entry<String,String> entry : this.map.entrySet()) {
            itemsStr.append("Key : ").append(entry.getKey()).append(" | Value : ").append(entry.getValue()).append("\n");
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
        return "------------- " + this.tableName + " : " + this.lineNumber + " -------------\n" +
                this.printItems() +
                "-----------------------------------------\n";
    }
    public void tablePrinter(){
        System.out.print(this.toString());
        System.out.println("==============================================");
        if(this instanceof GlobalScope){
            for (MethodScope method : ((GlobalScope) this).methods)
                method.tablePrinter();
        }
        else if(this instanceof MethodScope){
            for (BlockTable block : ((MethodScope) this).blocks)
                block.tablePrinter();
        }
        else if(this instanceof BlockTable){
            for (BlockTable block : ((BlockTable) this).blocks)
                block.tablePrinter();
        }
    }

    public String fieldType(){
        if(this instanceof GlobalScope){
            return "GlobalField";
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
