package dataStructures;

class RBNode<E> extends BTNode<E> {

    private Color color = Color.RED;

    public RBNode(E elem) {
        super(elem);
    }

    public RBNode(E element, RBNode<E> parent,
                  RBNode<E> left, RBNode<E> right) {
        super(element, parent, left, right);
    }

    public RBNode(E element, RBNode<E> parent) {
        super(element, parent, null, null);
    }

    public boolean isLeftChild(){
        Node<E> p = getParent();
        if (p == null) return false;
        return this == p.getLeftChild();
    }

    public void flipColor(){
        color = (color == Color.RED) ? Color.BLACK : Color.RED;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
