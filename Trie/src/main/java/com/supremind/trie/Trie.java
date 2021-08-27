package com.supremind.trie;

import java.util.HashMap;

public class Trie<V> {
    private int size;
    private Node<V> root;

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void clear() {
        size = 0;
        root = null;
    }

    public V get(String key) {
        Node<V> node = node(key);
        return node != null && node.word ? node.value : null;
    }

    public boolean contains(String key) {
        Node<V> node = node(key);
        //不是null，并且是一个单词
        return node != null && node.word;
    }

    public V add(String key, V value) {
        keyCheck(key);
        //这里没有初始化，在这里第一次添加肯定得初始化
        if (root == null) {
            root = new Node<>(null);
        }
        Node<V> node = root;
        int len = key.length();
        for (int i = 0; i < len; i++) {
            Character c = key.charAt(i);// d o g
            boolean emptyChildren = node.children == null;
            //node不是空，但是children，就是里面的属性HashMap可能是null，如果HashMap不是空
            //那HashMap的get也有可能是空
            Node<V> childNode = emptyChildren ? null : node.children.get(c);
            //如果不是空，就不需要做任何事，往下继续循环，找到之后，
            if (childNode == null) {
                //如果没有就创建
                childNode = new Node<>(node);
                childNode.character = c;
                node.children = emptyChildren ? new HashMap<>() : node.children;
                //创建好之后，因为子节点作为root的hashmap的value，所以，put进去，字符作为key,node作为value
                node.children.put(c, childNode);
            }
            //然后新建的子节点再变成父节点，添加，就跟链表一样，跟递归似的。
            node = childNode;
        }
        //到这里就说明之前是有的，那就是覆盖Value了
        if (node.word) {//如果存在了
            V oldValue = node.value;
            node.value = value;
            return oldValue;
        }
        //能来到后面，说明要么就是之前没有，我新建一个，要么就是之前有，但是不是一个单词，是之前某个的一部分。
        //新增一个单词,返回值是之前的，之前的是null，就返回null就好了
        node.word = true;
        node.value = value;
        size++;
        return null;
    }

    /*
    删除要考虑的情况还挺多的
    如果，你要删除dog,找到了，但是还有doggy，你不能直接把g给删除了，你要把g的红色变成蓝色，就是把单词变成不是单词
    是别的单词的一部分，然后value给清空，size--
    如果没有doggy,只有dog你要怎么办？删掉g之后，o要不要删？如果o是有子节点的，比如有dos，就是还有别的子节点
    或者do，o就是红色节点，那就不能删除了如果发现是蓝色的，继续往上删除，递归的删除，存在就删，不存在就不删了。
     */
    public V remove(String key) {
        //找到最后一个节点
        Node<V> node = node(key);
        if (node == null || !node.word) return null;
        size--;
        V oldValue = node.value;
        //如果没有找到，或者不是单词，就没必要删,比如现在dog不是单词,但是doggy是，你不能把g给删了
        // ，就是不能把dog给删了
        //如果还有子节点,
        if (node.children != null && !node.children.isEmpty()) {
            node.word = false;
            node.value = null;
            return oldValue;
        }
        //如果没有子节点,找到父节点，通过父节点删除自己，自己应该对应一个字符，所以，新加了一个属性。
        //而且，这个东西应该是递归的
        Node<V> parent = null;
        while ((parent = node.parent) != null) {
            //有点像找前驱后继了，比如前驱，当前节点在右子树，右子树的左子树是null，一直往上走
            //如果发现node的parent是node的右子树，OK，找到了
            parent.children.remove(node.character);
            //如果发现父节点还有其他的子节点就不要删了，这个怎么判断的?isEmpty(),就是看你的节点数量，
            //如果删了之后还大于0，那就说明还有其他子节点，还有，dog和doggy，删除了y,g,然后dog还是一个单词
            //那dog的g就不该去删除了啊，
            if(parent.word ||!parent.children.isEmpty())break;
            node = parent;
        }
        node.parent.children.remove(node.character);
        return oldValue;
    }

    public boolean startsWith(String prefix) {
        //node是返回全部，你这只查一部分，当然可以了，
        return node(prefix) != null;
    }


    private Node<V> node(String key) {
        keyCheck(key);
        Node<V> node = root;
        int len = key.length();
        for (int i = 0; i < len; i++) {
            //如果根节点都是null，那就没必要遍历啊。如果children是空的，那也没必要去，没有子节点，那遍历个啥啊
            //或者子节点里面没有key-value，那也没必要去遍历。
            if (node == null || node.children == null || node.children.isEmpty()) return null;
            Character c = key.charAt(i);// d o g
            node = node.children.get(c);

        }
        //能来到这里说明，node肯定没进入if判断，如果进了直接就return了，到这里，并不一定说明就有这个单词
        //可能还是单词一部分，所以要判断是不是一个单词，如果是，就返回，不是，就返回null，没有找到
//        return node.word ? node : null;
        //这里是把条件放松了，不去判断它是不是单词了，
        return node;
    }

    private void keyCheck(String key) {
        if (key == null || key.length() == 0) {
            throw new IllegalArgumentException("key must not be empty");
        }
    }

    private static class Node<V> {
        Character character;

        Node<V> parent;

        public Node(Node<V> parent) {
            this.parent = parent;
        }
        //注意看一下HashMap的value，也是一个node，那就是说明，dog,doy,y和g就是那个node，就是那个value
        HashMap<Character, Node<V>> children;
        V value;
        boolean word;//是否为单词的结尾，(是否是一个完整的单词)

    }
}
