package compiler;

import java.util.ArrayList;

public class BlockTable extends SymbolTable {
    public ArrayList<BlockTable> blocks = new ArrayList<BlockTable>();

    public BlockTable(String name) {
        super(name);
    }
}