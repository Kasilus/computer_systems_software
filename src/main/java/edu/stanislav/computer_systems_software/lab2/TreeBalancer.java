package edu.stanislav.computer_systems_software.lab2;

public class TreeBalancer {
    public static Node balance(Node root) {
        int result = 0;
        while (true) {
            if (result == 2) {
                // WA when branches are equivalent
                break;
            }
            if (root.leftChild != null) {
                if (root.leftChild.maxLength() - root.rightChild.maxLength() > 1) {
                    if (root.value.equals(root.leftChild.value)) {
                        root = root.turnRight();
                        result++;
                    } else {
                        break;
                    }
                } else if (root.rightChild.maxLength() - root.leftChild.maxLength() > 1) {
                    if (root.value.equals(root.rightChild.value)) {
                        root = root.turnLeft();
                        result++;
                    } else {
                        break;
                    }
                } else {
                    break;
                }
            } else {
                break;
            }
        }

        if (root.leftChild != null) {
            if (root.leftChild.leftChild != null) {
                root.leftChild = balance(root.leftChild);
            }
        }
        if (root.rightChild.rightChild != null || root.rightChild.leftChild != null) {
            root.rightChild = balance(root.rightChild);
        }

        return root;
    }
}
