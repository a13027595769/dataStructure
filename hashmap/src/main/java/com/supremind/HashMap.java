package com.supremind;

import java.util.Objects;

public class HashMap<K,V> implements Map<K,V>{
    //java.util.HashMap
    private int size;
    private static final boolean RED = false;
    private static final boolean BLACK = true;
    private Node<K,V>[] table;
    private static final int DEFAULT_CAPACITY = 1 << 4;
    public HashMap() {
        table = new Node[DEFAULT_CAPACITY];
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public void clear() {
        if(size == 0) return;
        size = 0;
        for (int i = 0; i < table.length; i++) {
            table[i] = null;
        }
    }

    @Override
    public V put(K key, V value) {
        //计算出索引
        int index = index(key);
        //获取到对应位置的元素
        Node<K, V> root = table[index];
        if(root == null)
        {   //如果root是空的话，就是没有获取到，parent就是null，然后再放到对应的位置
            root = new Node<>(key,value,null);
            table[index] = root;
            size++;
            afterPut(root);
            //这里要的返回值是你替换之前的，根节点之前肯定没有别的元素啊，直接返回null就行了

            return null;
        }
        //程序来到这，说明hash冲突了，要变成红黑树了
        Node<K,V> parent = root;
        Node<K,V> node = root;
        int h1 = Objects.hashCode(key);
        int cmp = 0;
        do{
            cmp = compare(key,node.key,h1,node.hash);
            //下一步只有为null的时候才可能跳出循环，就是把left或者right赋值给node的时候，那么没有赋值之前，就是parent
            parent = node;
            if(cmp > 0){
                node = node.right;
            }else if(cmp < 0){
                node = node.left;
            }else {//相等
                //这里和之前不一样了，以前是只有一个元素，现在是k,v要两个都覆盖一下
                V oldValue = node.value;
                node.key = key;
                node.value = value;
                return oldValue;
            }
        }while (node != null);

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

    @Override
    public V get(K key) {
        return null;
    }

    @Override
    public V remove(K key) {
        return null;
    }

    @Override
    public boolean containsKey(K key) {
        return false;
    }

    @Override
    public boolean containsValue(V value) {
        return false;
    }

    @Override
    public void traversal(Visitor<K, V> visitor) {

    }

    /**
     * 根据key生成对应的索引（在桶数组中的位置是什么）
     * @param key
     * @return
     */
    private int index(K key){
        //hashMap中允许有一个null键
        if(key == null) return 0;
        int hash = key.hashCode();
        //按理说这个hash值是能用的，但是hashCode这个方法也是native的，java官方不放心
        //又运用了double和float的那个方法，进行高16位和低16位进行运算
        return (hash ^ (hash >>> 16)) & (table.length - 1);
    }
    private int index(Node<K,V> node){
        //hashMap中允许有一个null键
        return (node.hash ^ (node.hash >>> 16)) & (table.length - 1);
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
//            root = parent;
            //这里已经没有root这个属性了，是一个数组中，每个位置存放着一个红黑树的根节点
            //那，其实传过来的都是一棵树上面的，用谁都行。
            table[index(grand.key)] = parent;
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

    /**
     *
     * @param k1
     * @param k2
     * @param h1 k1的hashcode
     * @param h2 k2的hashcode
     * @return
     */
    private int compare(K k1,K k2,int h1,int h2){
        //比较hash值，大的放右边，小的放左边
        int result = h1 - h2;
        //如果不等于0，那就说明没有冲突，返回去，就直接添加就好了
        if(result != 0) return result;
        //如果是对象的话，各个属性都一样，那就返回0，直接覆盖就好了
        if(Objects.equals(k1,k2))return 0;
        //如果到这，就说明hashcode相等，但是不equals，就是可能对象和String都算出来了相同的hash值
        //比较类名，类名是String，比较ascll码值，那肯定能比较
        if(k1 != null && k2 != null){
            String k1Clz = k1.getClass().getName();
            String k2Clz = k2.getClass().getName();
            result = k1Clz.compareTo(k2Clz);
            //如果不等于0，就说明不是同一种类型，也就可以直接返回
            if(result != 0)return result;
            //如果走到这里，说明是同一种类型，hashcode也相同，但是就是不equals，
            //俩Person对象，Person对象是基类，他俩都是Person的实例，肯定k2也实现了啊
            //所以就不用判断k2有没有实现comparable接口，肯定实现了。
            if(k1 instanceof Comparable){
                return ((Comparable) k1).compareTo(k2);
            }
        }
        //同一种类型，但是不具备可比较性
        //就是说到这里，Person没有实现Comparable接口，就是不具备可比较性+
        // k1不为null， k2为null
        // k1为null，k2不为null
        // 不存在k1,k2都为null，因为都为null的话，在Objects.equals(k1,k2)这里就拦截了，这就
        // 说明相等了
        return System.identityHashCode(k1) - System.identityHashCode(k2);
    }

    private static class Node<K,V>{
        int hash;
        K key;
        V value;
        boolean color = RED;
        Node<K,V> left;
        Node<K,V> right;
        Node<K,V> parent;
        public Node(K key, V value, Node<K,V> parent){
            this.key = key;
            this.hash = Objects.hashCode(key);
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
