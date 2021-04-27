package com.supremind.map;

import com.supremind.map.binaryTree.BinaryTree;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;

public class TreeMap<K,V> implements Map<K,V>{
    private static final boolean RED = false;
    private static final boolean BLACK = true;
    protected int size;
    protected Node<K,V> root;
    private final Comparator<K> comparator;
    public TreeMap(Comparator<K> comparator){
        this.comparator = comparator;
    }
    public TreeMap(){
        this(null);
    }
    public int size(){
        return size;
    }

    public boolean isEmpty(){
        return size == 0;
    }
    public void clear(){
        root = null;
        size = 0;
    }

    @Override
    public V put(K key, V value) {
        keyNotNullCheck(key);
        //添加第一个节点
        if(root == null){
            root = new Node<>(key,value,null);
            size++;
            afterPut(root);
            //之前这个方法是void ,但是这里要求有返回值，返回你覆盖之前的那个值，就跟remove一样，
            //有返回值返回删除的元素，因为是根节点，所以返回Null
            return null;
        }
        //添加的不是第一个节点
        //找到父节点
        Node<K,V> parent = root;
        //这个node一直在变化，就是判断比element大还是小，找左右子节点，如果小往左边走，大了右边走，直到为null就天剑
        Node<K,V> node = root;
        int cmp = 0;
        while (node != null){
            cmp = compare(key,node.key);
            //下一步只有为null的时候才可能跳出循环，就是把left或者right赋值给node的时候，那么没有赋值之前，就是parent
            parent = node;
            if(cmp > 0){
                node = node.right;
            }else if(cmp < 0){
                node = node.left;
            }else {//相等
                node.key = key;
                //这里和之前不一样了，以前是只有一个元素，现在是k,v要两个都覆盖一下
                V oldValue = node.value;
                node.value = value;
                return oldValue;
            }
        }

        //看看父节点插入到哪个位置
        Node<K,V> newNode =  new Node<>(key,value,parent);
        if(cmp > 0){
            parent.right = newNode;
        }else {
            parent.left = newNode;
        }
        size++;

        //新添加节点之后的处理
        afterPut(newNode);
        //上面那个是添加根节点，这里也是新添加的节点，只不过不是根节点，新添加的节点哪有之前的元素啊，
        // 所以还是返回Null
        return null;
    }
    private void rotateLeft(Node<K,V> grand){
        Node<K,V> parent = grand.right;
        Node<K,V> child = parent.left;

        grand.right = child;
        parent.left = grand;
        //调整父子关系的代码，你parent变成根节点了，那grand的父亲应该指向parent一些操作都放到这里面去做吧。
        afterRotate(grand,parent,child);
    }
    private void rotateRight(Node<K,V> grand){
        Node<K,V> parent = grand.left;
        Node<K,V> child = parent.right;

        grand.left = child;
        parent.right = grand;
        //调整父子关系的代码，你parent变成根节点了，那grand的父亲应该指向parent一些操作都放到这里面去做吧。
        afterRotate(grand,parent,child);
    }

    private void afterRotate(Node<K,V> grand, Node<K,V> parent, Node<K,V> child){
        //让parent成为子树的根节点
        parent.parent = grand.parent;
        if(grand.isLeftChild()){
            grand.parent.left = parent;
        }else if(grand.isRightChild()){
            grand.parent.right = parent;
        }else {  //grand是root节点
            root = parent;
        }
        //更新child的parent
        if(child != null){
            child.parent = grand;
        }
        //更新grand
        grand.parent = parent;
//        //更新高度,现在抽取出来AVL树和红黑树公共的了，但是更新高度只在AVL树中才有，所以应该放到AVL树中去
//        updateHeight(grand);
//        updateHeight(parent);
    }
    private Node<K,V> color(Node<K,V> node, boolean color){
        if(node != null){
            node.color = color;
        }
        return node;
    }
    private Node<K,V> red(Node<K,V> node){
        return color(node,RED);
    }

    private Node<K,V> black(Node<K,V> node){
        return color(node,BLACK);
    }

    private boolean colorOf(Node<K,V> node){
        return node == null ? BLACK : node.color;
    }

    private boolean isBlack(Node<K,V> node){
        return colorOf(node) == BLACK;
    }
    private boolean isRed(Node<K,V> node){
        return colorOf(node) == RED;
    }

    private void afterPut(Node<K,V> node) {
        //传过来的node节点就是新添加的节点，因为你要根据判断当前节点的叔父节点是不是红色啊之类的
        //也要判断父节点
        Node<K,V> parent = node.parent;
        //如果添加的是根节点，直接染成黑色,如果上溢到了根节点，也是把它当做新添加的节点，
        // 新添加的节点也是染成黑色的

        if(parent == null){
            black(node);
            return;
        }
        //如果父节点是黑的，那你直接return就行了，这四种情况不用考虑
        if(isBlack(parent))return;
        //uncle节点
        Node<K,V> uncle = parent.sibling();
        //祖父节点
        // Node<K,V> grand = parent.parent;
        //这里直接把一个染成红色的节点给你接收了。
        Node<K,V> grand = red(parent.parent);
        if(isRed(uncle)){
            black(parent);
            black(uncle);
            //把祖父节点当做是新添加的节点
//            red(grand);
////            afterAdd(grand);
            //todo  这个逻辑是要做的，之前忘了
            afterPut(grand);
            //发现后面的都要染成红色，那还是能够抽取的。
            return;
        }
        //叔父节点不是红色
        if(parent.isLeftChild()){//L
            /*
            因为默认添加的节点都是红色的，父节点要染黑，能来到这，祖父节点都是黑的，因为就那四种情况
             */
            if(node.isLeftChild()){//LL
                black(parent);
            }else{//LR
                black(node);
                rotateLeft(parent);
            }
            rotateRight(grand);

        }else{//R
            if(node.isLeftChild()){//RL
                black(node);
                rotateRight(parent);
            }else {//RR
                black(parent);
            }
            rotateLeft(grand);

        }
    }

    @Override
    public V get(K key) {
        Node<K, V> node = node(key);
        return node != null ? node.value : null;
    }

    @Override
    public V remove(K key) {
        return remove(node(key));
    }

    @Override
    public boolean containsKey(K key) {
        return node(key) != null;
    }

    @Override
    public boolean containsValue(V value) {
        if(root == null)return false;
        Queue<Node<K,V>> queue =  new LinkedList<>();
        queue.offer(root);
        while (!queue.isEmpty()){
            Node<K, V> node = queue.poll();
            if(valEquals(value,node.value))return true;
            if(node.left != null){
                queue.offer(node.left);
            }
            if(node.right != null){
                queue.offer(node.right);
            }
        }
        return false;
    }

    @Override
    public void traversal(Visitor<K, V> visitor) {
        if(visitor == null) return;
        traversal(root,visitor);
    }
    private void traversal(Node<K,V> node,Visitor<K, V> visitor) {
        if(node == null || visitor.stop) return;
        traversal(node.left,visitor);
        if(visitor.stop)return;
        visitor.visit(node.key,node.value);
        traversal(node.right,visitor);
    }
    private boolean valEquals(V v1,V v2){
        /*
           return v1 == null ? v2 == null : v1.equals(v2);
           因为比较value的时候，传过来的时候v1可能是null，如果null调用
           equals方法，就会报空指针异常，这个表达式是啥意思?
           v1传过来的是null吗？是，那v2是null吗？不是的话就返回false了，注意，这个方法是返回的Boolean类型
           如果不是null，才会走equals方法
           下面那个方法，是jdk1.7带的。
            public static boolean equals(Object a, Object b) {
                return (a == b) || (a != null && a.equals(b));
            }
            首先判断是不是完全相等，是的话，直接返回true，两个null也是相等的啊，然后看后面那个，
            a不是null，对吗？如果是null，后面是个&&这个直接就返回false了，不是null就调用equals方法
            妙啊！
         */
        return Objects.equals(v1, v2);
    }
    //前驱结点
    protected Node<K,V> predecessor(Node<K,V> node){
        if(node == null) return null;
        //前驱节点在左子树当中(left.right.right...)
        Node<K,V> p = node.left;
        if(p != null){
            while (p.right !=null){
                p = p.right;
            }
            return p;
        }
        //程序来到这里，说明左子树为null，要从祖父节点查找
        while(node.parent != null && node == node.parent.left){
            node = node.parent;
        }
        //1.父节点为null了，那就是没有前驱结点， node.parent == null
        //2.不是父节点的左节点而是右节点 node.parent.right
        return node.parent;
    }

    //后继节点
    protected Node<K,V> successor(Node<K,V> node){
        if(node == null) return null;
        //后继节点在右子树当中(right.left.left...)
        Node<K,V> p = node.right;
        if(p != null){
            while (p.left !=null){
                p = p.left;
            }
            return p;
        }
        //程序来到这里，说明右子树为null，要从祖父节点查找
        while(node.parent != null && node == node.parent.right){
            node = node.parent;
        }
        //1.父节点为null了，那就是没有前驱结点， node.parent == null
        //2.不是父节点的左节点而是右节点 node.parent.left
        return node.parent;
    }

    private void afterRemove(Node<K,V> node) {
        //用以取代node的子节点是红色，红黑树中，直接和颜色挂钩。
        if(isRed(node)){
            //因为要取代的节点要独立成一个节点，那么直接就染成黑色就好了
            black(node);
            return;
        }
        Node<K,V> parent = node.parent;
        //删除的是根节点
        if(parent == null)return;

        boolean left = parent.left == null || node.isLeftChild();
        //是左边吗？是左边，你的兄弟就是右边，否则就是左边，如果上面那行代码不删除，这边sibling可能会
        //为null，但是我们分析的情况兄弟不可能为null的。
        Node<K,V> sibling = left ? parent.right : parent.left;

        if(left){//被删除的节点在左边，兄弟节点在右边
            if(isRed(sibling)){//兄弟节点是红色，就是要把侄子变成兄弟了，然后进行旋转
                black(sibling);
                red(parent);
                rotateLeft(parent);
                //更换兄弟
                sibling = parent.right;
            }
            //兄弟节点必然是黑色
            if(isBlack(sibling.left) && isBlack(sibling.right)){
                boolean parentBlack = isBlack(parent);
                black(parent);
                red(sibling);
                if(parentBlack){
                    afterRemove(parent);
                }
            }else{
                if(isBlack(sibling.right)){
                    rotateRight(sibling);
                    sibling = parent.right;
                }
                color(sibling,colorOf(parent));
                black(sibling.right);
                black(parent);
                rotateLeft(parent);
            }
        }else {
            if(isRed(sibling)){//兄弟节点是红色，就是要把侄子变成兄弟了，然后进行旋转
                black(sibling);
                red(parent);
                rotateRight(parent);
                //更换兄弟
                sibling = parent.left;
            }
            //兄弟节点必然是黑色
            if(isBlack(sibling.left) && isBlack(sibling.right)){
                boolean parentBlack = isBlack(parent);
                black(parent);
                red(sibling);
                if(parentBlack){
                    afterRemove(parent);
                }
            }else{
                if(isBlack(sibling.left)){
                    rotateLeft(sibling);
                    sibling = parent.left;
                }
                color(sibling,colorOf(parent));
                black(sibling.left);
                black(parent);
                rotateRight(parent);
            }
        }
    }
    private V remove(Node<K,V> node){
        if(node == null)return null;
        size--;
        V oldValue = node.value;
        if(node.hasTwoChildren()){
            //找到后继节点
            Node<K,V> s = successor(node);
            //用后继节点的值覆盖度为2的节点
            node.key = s.key;
            node.value = s.value;
            //删除的其实是后继节点
            //因为后面要删除node，这里要删除s，所以就把s赋值给node，后面统一处理就可以了
            node = s;
        }
        //删除node节点（node节点的度必然是1或者0）
        Node<K,V> replacement = node.left != null? node.left:node.right;

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
        return oldValue;
    }
    private int compare(K e1,K e2){
        if(comparator != null){
            return comparator.compare(e1,e2);
        }
        return ((Comparable<K>)e1).compareTo(e2);
    }
    private void keyNotNullCheck(K key){
        if(key == null){
            throw new IllegalArgumentException("key must not be null");
        }
    }
    private Node<K,V> node(K key){
        Node<K,V> node = root;
        while (node != null){
            int cmp = compare(key,node.key);
            if(cmp == 0)return node;
            if(cmp > 0){
                node = node.right;
            }else {
                node = node.left;
            }
        }
        return null;
    }
    private static class Node<K,V>{
        K key;
        V value;
        boolean color = RED;
        Node<K,V> left;
        Node<K,V> right;
        Node<K,V> parent;
        public Node(K key,V value,Node<K,V> parent){
            this.key = key;
            this.value = value;
            this.parent = parent;
        }
        public boolean isLeaf(){
            return left == null && right == null;
        }
        public boolean hasTwoChildren(){
            return left != null && right != null;
        }
        public boolean isLeftChild(){
            return parent != null && this == parent.left;
        }
        public boolean isRightChild(){
            return parent != null && this == parent.right;
        }
        public Node<K,V> sibling(){
            if(isLeftChild()){
                return parent.right;
            }
            if(isRightChild()){
                return parent.left;
            }
            return null;
        }

    }
}
