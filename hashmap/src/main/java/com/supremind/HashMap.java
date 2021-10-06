package com.supremind;

import com.supremind.map.asserta.printer.BinaryTreeInfo;
import com.supremind.map.asserta.printer.BinaryTrees;

import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;

public class HashMap<K, V> implements Map<K, V> {
    private static final boolean RED = false;
    private static final boolean BLACK = true;
    private int size;
    private Node<K, V>[] table;
    private static final int DEFAULT_CAPACITY = 1 << 4;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

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
        if (size == 0) return;
        size = 0;
        for (int i = 0; i < table.length; i++) {
            table[i] = null;
        }
    }

    @Override
    public V put(K key, V value) {
        resize();

        int index = index(key);
        // 取出index位置的红黑树根节点,看看有没有元素。
        Node<K, V> root = table[index];
        if (root == null) {
            //如果root是空的话，就是没有获取到，parent就是null，然后再放到对应的位置
            root = createNode(key, value, null);
            table[index] = root;
            size++;
            /*
            因为涉及到了LinkedList,删除度为2的，在红黑树中肯定是删除的不是自身
            你修复红黑树的时候，链表要变的
             */
            fixAfterPut(root);
            //因为你覆盖了，本来是要返回之前节点的，但是你进入这里，说明是第一次进来
            //那你之前就不存在，所以就是返回Null了。
            return null;
        }

        // 添加新的节点到红黑树上面
        Node<K, V> parent = root;
        Node<K, V> node = root;
        int cmp = 0;
        //传进来的key
        K k1 = key;
        //传进来的key，进行hash运算
        int h1 = hash(k1);
        Node<K, V> result = null;
        boolean searched = false; // 是否已经搜索过这个key
        do {
            parent = node;
            K k2 = node.key;
            int h2 = node.hash;
            /*
            为什么这里不用减了？因为数字可能非常大，一个特别大的正数减去一个特别大的负数
            可能会溢出，变成正的了，所以直接比大小，这个就不会溢出了
             */
            if (h1 > h2) {
                cmp = 1;
            } else if (h1 < h2) {
                cmp = -1;
            } else if (Objects.equals(k1, k2)) {
                cmp = 0;
            } else if (k2 != null
                    && k1 instanceof Comparable
                    && k1.getClass() == k2.getClass()
                    && (cmp = ((Comparable) k1).compareTo(k2)) != 0) {
            /*
            再看一下，比较hashcode，大于0或者小于0，往后走了，往左或者往右，类型是不是一样的，不管！
            如果走到那个comparable说明什么，说明前面的equals也不成立，说明不是同一个对象，
            如果到这个里面，说明是一个类型的，然后compareTo又会返回 -1,0,1,大于0或者小于0，
            应该往后边走，循环里面，啥都不干，就是让cmp存储一下结果，交给后面处理，如果相等呢？相等了，
            代表大小相等，年龄相等，但是，不是一个对象，只能往后边走，我这个else if啊，怎么往后面走？
            让它不符合条件不就行了？换个写法：
            等于0了，就不符合条件了，往后走。
            再说一遍，hashmap现在就是和之前的二叉树不一样了。不再认为compareTo等于0是一个对象了！
            因为你Person可能有三个属性，compareTo可能就是比较年龄，不能说年龄一样就是一个对象啊。
            然后这里不用管，大于或者小于0，交给后面处理，不用三目运算符了。
            -----------------------------------------------------------------------
            优化：优化put，为什么，你想想，如果现在添加20，那按照惯例，如果，hash值还都是0了，
            肯定都走到else里面了，然后比较内存地址，假设往左边走，比较一圈，发现没有，然后呢，
            到cmp往左往右了，然后再循环，那肯定又是到上面那个else里面了，在上面都扫描一圈了，
            发现没有，你再往下走一步，再扫描一圈，那不是浪费么，就跟斐波那契差不多，
            只不过斐波那契是没有用前面的结果算后面的结果，这个是有全部的结果，还要去算之前的结果。
            那怎么优化呢？到那之后，不让他扫描，就到那个节点直接往左往右就好了。
            加入一个flag，如果进到else里面就说明，没有，要添加了，
            下次来的时候，直接就变成true了，直接就又算出一个cmp，就往下走了，
            不用再次比较了，那这里能不能用break来取代控制变量呢？以后再想。
            然后，node可以再优化了，目前看不懂，以后再debug吧。。
            result其实就是存储一下结果，你想，没有赋值的时候，
            就是递归调用，如果有值。找到了，就赋值给临时变量，返回，
            那else呢，为啥不用result接收了，因为原来的，你就是返回的node，
            这边直接就可以返回node了，就可以不用接收了。也可以理解为，原来之所以需要，
            是因为，不接收的话，递归怎么结束？然后只有两种情况，
            下面那个就不用接收了直接可以进行下一轮循环了，就是程序能走到那里，
            就已经该往左往右走了，不用操其他的心了。如果是null呢？那while循环就不满足了，就退出了，没找到。
             */
            } else if (searched) { // 已经扫描了
                //这个cmp是决定往哪里添加的，就是左边还是右边添加，查找是在node方法里面
                //你可能还在疑惑，这个search不是进来的时候就重新赋值了嘛？那这个变量不就
                //没有用了嘛？其实不是的，这个search，既然是管着添加的东西往哪走，那么
                //是在这个while循环中的，和外面无关，大于小于都会一直循环，走到这里看看
                //左边还是右边，直到找到进行覆盖或者找不到，跳出循环，下面进行添加新的节点
                cmp = System.identityHashCode(k1) - System.identityHashCode(k2);
            } else { // searched == false; 还没有扫描，然后再根据内存地址大小决定左右
                //这个左还是右都没有关系，你看，这里先是左不是空，去node了，然后把
                //左子树扫描一遍，然后没找到的话，进入||或了。再把右子树扫描一遍，还是没找到
                //这个时候就可以根据内存地址比较了。
                if ((node.left != null && (result = node(node.left, k1)) != null)
                        || (node.right != null && (result = node(node.right, k1)) != null)) {
                    // 已经存在这个key
                    node = result;
                    cmp = 0;
                } else { // 不存在这个key
                    searched = true;
                    //这个cmp是决定往哪里添加的，就是左边还是右边添加，查找是在node方法里面
                    cmp = System.identityHashCode(k1) - System.identityHashCode(k2);
                }
            }

            if (cmp > 0) {
                node = node.right;
            } else if (cmp < 0) {
                node = node.left;
            } else { // 相等
                V oldValue = node.value;
                node.key = key;
                node.value = value;
                node.hash = h1;
                return oldValue;
            }
        } while (node != null);

        // 还是之前的，因为考虑到linkedList,删除度为2的会有问题，就是指针会变，可以加个protected的方法
        //但是这里用了RenameMethod，改个名，之前的名字，改了，改的更加清楚。
        Node<K, V> newNode = createNode(key, value, parent);
        if (cmp > 0) {
            parent.right = newNode;
        } else {
            parent.left = newNode;
        }
        size++;

        // 新添加节点之后的处理
        fixAfterPut(newNode);
        return null;
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
        //只能一个一个取出来，一个一个入队
        if (size == 0) return false;
        Queue<Node<K, V>> queue = new LinkedList<>();
        for (int i = 0; i < table.length; i++) {
            if (table[i] == null) continue;

            queue.offer(table[i]);
            while (!queue.isEmpty()) {
                Node<K, V> node = queue.poll();
                if (Objects.equals(value, node.value)) return true;

                if (node.left != null) {
                    queue.offer(node.left);
                }
                if (node.right != null) {
                    queue.offer(node.right);
                }
            }
        }
        return false;
    }

    @Override
    public void traversal(Visitor<K, V> visitor) {
        if (size == 0 || visitor == null) return;

        Queue<Node<K, V>> queue = new LinkedList<>();
        for (int i = 0; i < table.length; i++) {
            if (table[i] == null) continue;

            queue.offer(table[i]);
            while (!queue.isEmpty()) {
                Node<K, V> node = queue.poll();
                if (visitor.visit(node.key, node.value)) return;

                if (node.left != null) {
                    queue.offer(node.left);
                }
                if (node.right != null) {
                    queue.offer(node.right);
                }
            }
        }
    }

    public void print() {
        if (size == 0) return;
        for (int i = 0; i < table.length; i++) {
            final Node<K, V> root = table[i];
            System.out.println("【index = " + i + "】");
            BinaryTrees.println(new BinaryTreeInfo() {
                @Override
                public Object string(Object node) {
                    return node;
                }

                @Override
                public Object root() {
                    return root;
                }

                @Override
                public Object right(Object node) {
                    return ((Node<K, V>)node).right;
                }

                @Override
                public Object left(Object node) {
                    return ((Node<K, V>)node).left;
                }
            });
            System.out.println("---------------------------------------------------");
        }
    }

    protected Node<K, V> createNode(K key, V value, Node<K, V> parent) {
        return new Node<>(key, value, parent);
    }

    protected void afterRemove(Node<K, V> willNode, Node<K, V> removedNode) { }

    private void resize() {
        // 装填因子 <= 0.75
        if (size / table.length <= DEFAULT_LOAD_FACTOR) return;

        Node<K, V>[] oldTable = table;
        table = new Node[oldTable.length << 1];

        Queue<Node<K, V>> queue = new LinkedList<>();
        for (int i = 0; i < oldTable.length; i++) {
            if (oldTable[i] == null) continue;

            queue.offer(oldTable[i]);
            while (!queue.isEmpty()) {
                Node<K, V> node = queue.poll();
                if (node.left != null) {
                    queue.offer(node.left);
                }
                if (node.right != null) {
                    queue.offer(node.right);
                }

                // 挪动代码得放到最后面，你先挪动了，然后清空了，这后面就没法进行了，所以得放到后面
                moveNode(node);
            }
        }
    }

    private void moveNode(Node<K, V> newNode) {
        // 重置
        newNode.parent = null;
        newNode.left = null;
        newNode.right = null;
        //默认添加的节点是红色，这里得重新复制，
        newNode.color = RED;

        int index = index(newNode);
        // 取出index位置的红黑树根节点
        Node<K, V> root = table[index];
        if (root == null) {
            root = newNode;
            table[index] = root;
            fixAfterPut(root);
            return;
        }

        // 添加新的节点到红黑树上面
        Node<K, V> parent;
        Node<K, V> node = root;
        int cmp = 0;
        K k1 = newNode.key;
        int h1 = newNode.hash;
        do {
            parent = node;
            K k2 = node.key;
            int h2 = node.hash;
            if (h1 > h2) {
                cmp = 1;
            } else if (h1 < h2) {
                cmp = -1;
            } else if (k1 != null && k2 != null
                    && k1 instanceof Comparable
                    && k1.getClass() == k2.getClass()
                    && (cmp = ((Comparable)k1).compareTo(k2)) != 0) {
            } else {
                cmp = System.identityHashCode(k1) - System.identityHashCode(k2);
            }

            if (cmp > 0) {
                node = node.right;
            } else if (cmp < 0) {
                node = node.left;
            }
        } while (node != null);

        // 看看插入到父节点的哪个位置
        newNode.parent = parent;
        if (cmp > 0) {
            parent.right = newNode;
        } else {
            parent.left = newNode;
        }

        // 新添加节点之后的处理
        fixAfterPut(newNode);
    }

    protected V remove(Node<K, V> node) {
        /*
         * fixAfterRemove是修复红黑树性质的，不用操心度为0还有1指针的问题，这个处理了，
         * 只操心2就好了
         */
        if (node == null) return null;
        //你想要删除的元素，还是那样，删除度为2的，必定不是自己，指针会乱的
        Node<K, V> willNode = node;

        size--;

        V oldValue = node.value;

        if (node.hasTwoChildren()) { // 度为2的节点
            // 找到后继节点
            Node<K, V> s = successor(node);
            // 用后继节点的值覆盖度为2的节点的值
            node.key = s.key;
            node.value = s.value;
            node.hash = s.hash;
            // 删除后继节点
            node = s;
        }

        // 删除node节点（node的度必然是1或者0）
        Node<K, V> replacement = node.left != null ? node.left : node.right;
        int index = index(node);

        if (replacement != null) { // node是度为1的节点
            // 更改parent
            replacement.parent = node.parent;
            // 更改parent的left、right的指向
            if (node.parent == null) { // node是度为1的节点并且是根节点
                table[index] = replacement;
            } else if (node == node.parent.left) {
                node.parent.left = replacement;
            } else { // node == node.parent.right
                node.parent.right = replacement;
            }

            // 删除节点之后的处理
            fixAfterRemove(replacement);
        } else if (node.parent == null) { // node是叶子节点并且是根节点
            table[index] = null;
        } else { // node是叶子节点，但不是根节点
            if (node == node.parent.left) {
                node.parent.left = null;
            } else { // node == node.parent.right
                node.parent.right = null;
            }

            // 删除节点之后的处理.
            fixAfterRemove(node);
        }

        // 交给子类去处理，看LinkendList
        afterRemove(willNode, node);

        return oldValue;
    }

    private Node<K, V> successor(Node<K, V> node) {
        if (node == null) return null;

        // 前驱节点在左子树当中（right.left.left.left....）
        Node<K, V> p = node.right;
        if (p != null) {
            while (p.left != null) {
                p = p.left;
            }
            return p;
        }

        // 从父节点、祖父节点中寻找前驱节点
        while (node.parent != null && node == node.parent.right) {
            node = node.parent;
        }

        return node.parent;
    }

    private Node<K, V> node(K key) {
        Node<K, V> root = table[index(key)];
        return root == null ? null : node(root, key);
    }

    private Node<K, V> node(Node<K, V> node, K k1) {
        int h1 = hash(k1);
        // 存储查找结果
        Node<K, V> result = null;
        int cmp = 0;
        while (node != null) {
            K k2 = node.key;
            int h2 = node.hash;
            // 先比较哈希值
            if (h1 > h2) {
                node = node.right;
            } else if (h1 < h2) {
                node = node.left;
            } else if (Objects.equals(k1, k2)) {
                return node;
            } else if (k2 != null
                    && k1 instanceof Comparable
                    && k1.getClass() == k2.getClass()
                    && (cmp = ((Comparable) k1).compareTo(k2)) != 0) {
                //这个时候就需要了
                node = cmp > 0 ? node.right : node.left;
                //看一下这里，先看看右边是不是空，右边不是空，再看看右边有没有找到，找不到的话
                //那不就是不符合嘛，那就进不去这个else if,就进去else里面了，所以能左右都扫一遍
                //当然，这边是递归的,都是直接进到头，然后不符合，再扫左边，一层一层的出，一层一层
                //的左边就扫到了。
            } else if (node.right != null && (result = node(node.right, k1)) != null) {
                return result;
            } else { // 只能往左边找
                /*
                result其实就是存储一下结果，你想，没有赋值的时候，就是递归调用，如果有值。找到了，
                就赋值给临时变量，返回，那else呢，为啥不用result接收了，因为原来的，你就是返回的node，
                这边直接就可以返回node了，就可以不用接收了。也可以理解为，原来之所以需要，是因为，
                不接收的话，递归怎么结束？然后只有两种情况，下面那个就不用接收了直接可以进行下一轮循环了，
                就是程序能走到那里，就已经该往左往右走了，不用操其他的心了。如果是null呢？
                那while循环就不满足了，就退出了，没找到。
                 */
                node = node.left;
            }
        }
        return null;
    }

    /**
     * 根据key生成对应的索引（在桶数组中的位置）
     */
    private int index(K key) {
        return hash(key) & (table.length - 1);
    }

    private int hash(K key) {
        if (key == null) return 0;
        int hash = key.hashCode();
        return hash ^ (hash >>> 16);
    }

    private int index(Node<K, V> node) {
        return node.hash & (table.length - 1);
    }

    private void fixAfterRemove(Node<K, V> node) {
        // 如果删除的节点是红色
        // 或者 用以取代删除节点的子节点是红色
        if (isRed(node)) {
            black(node);
            return;
        }

        Node<K, V> parent = node.parent;
        if (parent == null) return;

        // 删除的是黑色叶子节点【下溢】
        // 判断被删除的node是左还是右
        boolean left = parent.left == null || node.isLeftChild();
        Node<K, V> sibling = left ? parent.right : parent.left;
        if (left) { // 被删除的节点在左边，兄弟节点在右边
            if (isRed(sibling)) { // 兄弟节点是红色
                black(sibling);
                red(parent);
                rotateLeft(parent);
                // 更换兄弟
                sibling = parent.right;
            }

            // 兄弟节点必然是黑色
            if (isBlack(sibling.left) && isBlack(sibling.right)) {
                // 兄弟节点没有1个红色子节点，父节点要向下跟兄弟节点合并
                boolean parentBlack = isBlack(parent);
                black(parent);
                red(sibling);
                if (parentBlack) {
                    fixAfterRemove(parent);
                }
            } else { // 兄弟节点至少有1个红色子节点，向兄弟节点借元素
                // 兄弟节点的左边是黑色，兄弟要先旋转
                if (isBlack(sibling.right)) {
                    rotateRight(sibling);
                    sibling = parent.right;
                }

                color(sibling, colorOf(parent));
                black(sibling.right);
                black(parent);
                rotateLeft(parent);
            }
        } else { // 被删除的节点在右边，兄弟节点在左边
            if (isRed(sibling)) { // 兄弟节点是红色
                black(sibling);
                red(parent);
                rotateRight(parent);
                // 更换兄弟
                sibling = parent.left;
            }

            // 兄弟节点必然是黑色
            if (isBlack(sibling.left) && isBlack(sibling.right)) {
                // 兄弟节点没有1个红色子节点，父节点要向下跟兄弟节点合并
                boolean parentBlack = isBlack(parent);
                black(parent);
                red(sibling);
                if (parentBlack) {
                    fixAfterRemove(parent);
                }
            } else { // 兄弟节点至少有1个红色子节点，向兄弟节点借元素
                // 兄弟节点的左边是黑色，兄弟要先旋转
                if (isBlack(sibling.left)) {
                    rotateLeft(sibling);
                    sibling = parent.left;
                }

                color(sibling, colorOf(parent));
                black(sibling.left);
                black(parent);
                rotateRight(parent);
            }
        }
    }

    private void fixAfterPut(Node<K, V> node) {
        Node<K, V> parent = node.parent;

        // 添加的是根节点 或者 上溢到达了根节点
        if (parent == null) {
            black(node);
            return;
        }

        // 如果父节点是黑色，直接返回
        if (isBlack(parent)) return;

        // 叔父节点
        Node<K, V> uncle = parent.sibling();
        // 祖父节点
        Node<K, V> grand = red(parent.parent);
        if (isRed(uncle)) { // 叔父节点是红色【B树节点上溢】
            black(parent);
            black(uncle);
            // 把祖父节点当做是新添加的节点
            fixAfterPut(grand);
            return;
        }

        // 叔父节点不是红色
        if (parent.isLeftChild()) { // L
            if (node.isLeftChild()) { // LL
                black(parent);
            } else { // LR
                black(node);
                rotateLeft(parent);
            }
            rotateRight(grand);
        } else { // R
            if (node.isLeftChild()) { // RL
                black(node);
                rotateRight(parent);
            } else { // RR
                black(parent);
            }
            rotateLeft(grand);
        }
    }

    private void rotateLeft(Node<K, V> grand) {
        Node<K, V> parent = grand.right;
        Node<K, V> child = parent.left;
        grand.right = child;
        parent.left = grand;
        afterRotate(grand, parent, child);
    }

    private void rotateRight(Node<K, V> grand) {
        Node<K, V> parent = grand.left;
        Node<K, V> child = parent.right;
        grand.left = child;
        parent.right = grand;
        afterRotate(grand, parent, child);
    }

    private void afterRotate(Node<K, V> grand, Node<K, V> parent, Node<K, V> child) {
        // 让parent称为子树的根节点
        parent.parent = grand.parent;
        if (grand.isLeftChild()) {
            grand.parent.left = parent;
        } else if (grand.isRightChild()) {
            grand.parent.right = parent;
        } else { // grand是root节点
            table[index(grand)] = parent;
        }

        // 更新child的parent
        if (child != null) {
            child.parent = grand;
        }

        // 更新grand的parent
        grand.parent = parent;
    }

    private Node<K, V> color(Node<K, V> node, boolean color) {
        if (node == null) return node;
        node.color = color;
        return node;
    }

    private Node<K, V> red(Node<K, V> node) {
        return color(node, RED);
    }

    private Node<K, V> black(Node<K, V> node) {
        return color(node, BLACK);
    }

    private boolean colorOf(Node<K, V> node) {
        return node == null ? BLACK : node.color;
    }

    private boolean isBlack(Node<K, V> node) {
        return colorOf(node) == BLACK;
    }

    private boolean isRed(Node<K, V> node) {
        return colorOf(node) == RED;
    }

    protected static class Node<K, V> {
        int hash;
        K key;
        V value;
        boolean color = RED;
        Node<K, V> left;
        Node<K, V> right;
        Node<K, V> parent;
        public Node(K key, V value, Node<K, V> parent) {
            this.key = key;
            int hash = key == null ? 0 : key.hashCode();
            this.hash = hash ^ (hash >>> 16);
            this.value = value;
            this.parent = parent;
        }

        public boolean hasTwoChildren() {
            return left != null && right != null;
        }

        public boolean isLeftChild() {
            return parent != null && this == parent.left;
        }

        public boolean isRightChild() {
            return parent != null && this == parent.right;
        }

        public Node<K, V> sibling() {
            if (isLeftChild()) {
                return parent.right;
            }

            if (isRightChild()) {
                return parent.left;
            }

            return null;
        }

        @Override
        public String toString() {
            return "Node_" + key + "_" + value;
        }
    }
}
