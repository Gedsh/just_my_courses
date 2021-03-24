package ru.geekbrains.garmatin.lesson6.tree;

public class Tree<E extends Comparable<E>> {
    private Node<E> root;

    public E find(E element) {
        Node<E> current = root;

        while (!element.equals(current.data)) {
            if (element.compareTo(current.data) < 0) {
                current = current.leftChild;
            } else {
                current = current.rightChild;
            }

            if (current == null) {
                return null;
            }
        }

        return current.data;
    }

    private void preOrder(Node<E> rootNode) {
        if (rootNode != null) {
            System.out.printf("%s ", rootNode.data);
            preOrder(rootNode.leftChild);
            preOrder(rootNode.rightChild);
        }
    }

    private void postOrder(Node<E> rootNode) {
        if (rootNode != null) {
            postOrder(rootNode.leftChild);
            postOrder(rootNode.rightChild);
            System.out.printf("%s ", rootNode.data);
        }
    }

    public void inOrder(Node<E> rootNode) {
        if (rootNode != null) {
            inOrder(rootNode.leftChild);
            System.out.printf("%s ", rootNode.data);
            inOrder(rootNode.rightChild);
        }
    }

    public void insert(E element) {
        Node<E> node = new Node<>();
        node.data = element;

        if (root == null) {
            root = node;
        } else {
            Node<E> current = root;
            Node<E> parent;

            while (true) {
                parent = current;
                if (element.compareTo(current.data) < 0) {
                    current = current.leftChild;
                    if (current == null) {
                        parent.leftChild = node;
                        return;
                    }
                } else {
                    current = current.rightChild;
                    if (current == null) {
                        parent.rightChild = node;
                        return;
                    }
                }
            }
        }
    }

    public boolean delete(E element) {
        Node<E> current = root;
        Node<E> parent = root;

        boolean isLeftChild = true;

        while (!element.equals(current.data)) {
            parent = current;
            if (element.compareTo(current.data) < 0) {
                isLeftChild = true;
                current = current.leftChild;
            } else {
                isLeftChild = false;
                current = current.rightChild;
            }
            if (current == null) {
                return false;
            }
        }

        if (current.leftChild == null && current.rightChild == null) {
            if (current == root) {
                root = null;
            } else if (isLeftChild) {
                parent.leftChild = null;
            } else {
                parent.rightChild = null;
            }
        } else if (current.rightChild == null) {
            if (current == null) {
                root = current.leftChild;
            } else if (isLeftChild) {
                parent.leftChild = current.leftChild;
            } else {
                parent.rightChild = current.leftChild;
            }
        } else if (current.leftChild == null) {
            if (current == null) {
                root = current.rightChild;
            } else if (isLeftChild) {
                parent.leftChild = current.rightChild;
            } else {
                parent.rightChild = current.rightChild;
            }
        } else {
            Node<E> successor = getSuccessor(current);
            if (current == root) {
                root = successor;
            } else if (isLeftChild) {
                parent.leftChild = successor;
            } else {
                parent.rightChild = successor;
            }
            successor.leftChild = current.leftChild;
        }

        return true;
    }

    private Node<E> getSuccessor(Node<E> node) {
        Node<E> successorParent = node;
        Node<E> successor = node;
        Node<E> current = node.rightChild;

        while (current != null) {
            successorParent = successor;
            successor = current;
            current = current.leftChild;
        }

        if (!successor.equals(node.rightChild)) {
            successorParent.leftChild = successor.rightChild;
            successor.rightChild = node.rightChild;
        }

        return successor;
    }

    public void displayTree() {
        Node<E> current = root;
        System.out.print("Symmetrical: ");
        inOrder(root);
        System.out.println("\nDirect: ");
        preOrder(root);
        System.out.println("\nReverse: ");
        postOrder(current);
        System.out.println();
    }

    public E min() {
        Node<E> current = root;
        Node<E> last = null;

        while (current != null) {
            last = current;
            current = current.leftChild;
        }
        return last == null ? null : last.data;
    }

    public E max() {
        Node<E> current = root;
        Node<E> last = null;

        while (current != null) {
            last = current;
            current = current.rightChild;
        }
        return last == null ? null : last.data;
    }
}
