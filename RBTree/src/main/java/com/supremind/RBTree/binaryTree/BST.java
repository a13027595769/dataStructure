package com.supremind.RBTree.binaryTree;



import java.util.Comparator;

public class BST<E> extends BinaryTree<E> {
    private Comparator<E> comparator;

    public BST(Comparator<E> comparator){
        this.comparator = comparator;
    }
    public BST(){
        this(null);
    }



    public void add(E element){
        elementNotNullCheck(element);
        //添加第一个节点
        if(root == null){
            root = createNode(element,null);
            size++;
            afterAdd(root);
            return;
        }
        //添加的不是第一个节点
        //找到父节点
        Node<E> parent = root;
        //这个node一直在变化，就是判断比element大还是小，找左右子节点，如果小往左边走，大了右边走，直到为null就天剑
        Node<E> node = root;
        int cmp = 0;
        while (node != null){
            cmp = compare(element,node.element);
            //下一步只有为null的时候才可能跳出循环，就是把left或者right赋值给node的时候，那么没有赋值之前，就是parent
            parent = node;
            if(cmp > 0){
                node = node.right;
            }else if(cmp < 0){
                node = node.left;
            }else {//相等
                node.element = element;
                return;
            }
        }

        //看看父节点插入到哪个位置
        Node<E> newNode = createNode(element,parent);
        if(cmp > 0){
            parent.right = newNode;
        }else {
            parent.left = newNode;
        }
        size++;

        //新添加节点之后的处理
        afterAdd(newNode);
    }

    /**
     * 添加node之后的调整
     * @param node  新添加的节点
     */
    protected void afterAdd(Node<E> node){}

    /**
     * 删除node之后的调整
     * @param node
     */
    protected void afterRemove(Node<E> node){}
    public void remove(E element){
        remove(node(element));
    }

    private void remove(Node<E> node){
        if(node == null)return;
        size--;

        if(node.hasTwoChildren()){
            //找到后继节点
            Node<E> s = successor(node);
            //用后继节点的值覆盖度为2的节点
            node.element = s.element;
            //删除的其实是后继节点
            //因为后面要删除node，这里要删除s，所以就把s赋值给node，后面统一处理就可以了
            node = s;
        }
        //删除node节点（node节点的度必然是1或者0）
        Node<E> replacement = node.left != null? node.left:node.right;

        if(replacement != null){//node是度为1的节点
            //更改parent
            replacement.parent = node.parent;
            //更改parent的left、right的指向
            if(node.parent == null){//node是度为1并且是根节点，但是下面还有子节点，就需要将root指针指向子节点
                root = replacement;
            }else if(node == node.parent.left){
                node.parent.left = replacement;
            }else {//node == node.parent.right
                node.parent.right = replacement;
            }
            //删除节点之后的处理
            afterRemove(replacement);
        }else if(node.parent == null){//node是叶子结点,并且是根节点
            root = null;
            //删除节点之后的处理
            afterRemove(node);
        }else {//node是叶子结点,但不是根节点
            if(node == node.parent.left){
                node.parent.left = null;
            }else {
                node.parent.right = null;
            }
            //删除节点之后的处理
            afterRemove(node);
        }

    }

    private Node<E> node(E element){
        Node<E> node = root;
        while (node != null){
            int cmp = compare(element,node.element);
            if(cmp == 0)return node;
            if(cmp > 0){
                node = node.right;
            }else {
                node = node.left;
            }
        }
        return null;
    }

    /**
     * @return  返回值等于0代表e1和e2相等,返回值大于0代表e1大于e2,返回值小于0,代表e1小于e2
     */
    private int compare(E e1,E e2){
        if(comparator != null){
            return comparator.compare(e1,e2);
        }
        return ((Comparable<E>)e1).compareTo(e2);
    }
    public boolean contains(E element){
        return node(element) != null;
    }

    private void elementNotNullCheck(E element){
        if(element == null){
            throw new IllegalArgumentException("element must not be null");
        }
    }
}
