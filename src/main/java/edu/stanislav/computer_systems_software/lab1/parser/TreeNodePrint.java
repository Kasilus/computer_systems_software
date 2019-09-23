package edu.stanislav.computer_systems_software.lab1.parser;

import java.util.Iterator;
import java.util.List;

public class TreeNodePrint {

    final String name;
    final List<TreeNodePrint> children;

    public TreeNodePrint(String name, List<TreeNodePrint> children) {
        this.name = name;
        this.children = children;
    }

    public String toString() {
        StringBuilder buffer = new StringBuilder(50);
        print(buffer, "", "");
        return buffer.toString();
    }

    private void print(StringBuilder buffer, String prefix, String childrenPrefix) {
        buffer.append(prefix);
        buffer.append(name);
        buffer.append('\n');
        for (Iterator<TreeNodePrint> it = children.iterator(); it.hasNext();) {
            TreeNodePrint next = it.next();
            if (it.hasNext()) {
                next.print(buffer, childrenPrefix + "├── ", childrenPrefix + "│   ");
            } else {
                next.print(buffer, childrenPrefix + "└── ", childrenPrefix + "    ");
            }
        }
    }
}
