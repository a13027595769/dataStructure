package com.supremind;

import java.util.Objects;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class LinkedHashMap<K, V> extends HashMap<K, V> {
    //既然是链表，就弄个双向的，头指针和尾指针
    private LinkedNode<K, V> first;
    private LinkedNode<K, V> last;
    private static class LinkedNode<K, V> extends Node<K, V> {
        //没有重写父类，在父类之前又做了一点东西，前后添加的顺序。
        LinkedNode<K, V> prev;
        LinkedNode<K, V> next;
        public LinkedNode(K key, V value, Node<K, V> parent) {
            super(key, value, parent);
        }
    }
    @Override
    public void clear() {
        //这个是在之后做一些清理工作，要把头尾指针的指向给清0
        super.clear();
        first = null;
        last = null;
    }

    @Override
    public boolean containsValue(V value) {
        LinkedNode<K, V> node = first;
        while (node != null) {
            if (Objects.equals(value, node.value)) return true;
            node = node.next;
        }
        return false;
    }

    @Override
    public void traversal(Visitor<K, V> visitor) {
        if (visitor == null) return;
        LinkedNode<K, V> node = first;
        while (node != null) {
            if (visitor.visit(node.key, node.value)) return;
            node = node.next;
        }
    }

    @Override
    protected void afterRemove(Node<K, V> willNode, Node<K, V> removedNode) {
        //willNode你刚开始想删除的那个节点
        //removedNode真正被删除的节点，就是willNode的前驱或者是后继
        LinkedNode<K, V> node1 = (LinkedNode<K, V>) willNode;
        LinkedNode<K, V> node2 = (LinkedNode<K, V>) removedNode;
        //如果度为1或者是0，就没必要比较了，因为两个是一样的这个只适用于度为2的，只有度为2才能进来。
        //他们俩个必须不能相同，相同的话，就是度为1或者是0，在后面，随便拿哪个都行，因为在父类，是传了
        //两个，如果是度为1，就是一样的了，
        if (node1 != node2) {
            // 交换linkedWillNode和linkedRemovedNode在链表中的位置
            // 交换prev
            LinkedNode<K, V> tmp = node1.prev;
            node1.prev = node2.prev;
            node2.prev = tmp;
            if (node1.prev == null) {
                //如果当前节点的前一个是null，那么就说明你是头结点，头结点一删，那first指针就该指向你的下一个。
                first = node1;
            } else {
                //现在已经交换了，node1就是node2，这个就是原来的指向的prev，
                // 现在这个原来指向的prev的指针也是要变化的，但是这里只是弄了prev,所以，下面的next指针也是要变的
                node1.prev.next = node1;
            }
            //两个节点，都得弄
            if (node2.prev == null) {
                first = node2;
            } else {
                node2.prev.next = node2;
            }

            // 交换next
            tmp = node1.next;
            node1.next = node2.next;
            node2.next = tmp;
            if (node1.next == null) {
                last = node1;
            } else {
                //现在已经交换了，node1就是node2
                node1.next.prev = node1;
            }
            if (node2.next == null) {
                last = node2;
            } else {
                node2.next.prev = node2;
            }
        }
        //其实这里node1和node2都是一样的。
        LinkedNode<K, V> prev = node2.prev;
        LinkedNode<K, V> next = node2.next;
        /*
        双向的，前面的if else是头指针，下一个if else是尾指针
         */
        if (prev == null) {
            //如果当前节点的前一个是null，那么就说明你是头结点，头结点一删，那first指针就该指向你的下一个。
            first = next;
        } else {
            //prev.next -> prev -> next
            //因为删除中间的了，所以，第一个要指向第三个
            prev.next = next;
        }

        if (next == null) {
            //如果你的下一个是空，那就说明你是最后一个，尾指针就该指向你的前一个。
            last = prev;
        } else {
            //你还是中间的，你的下一个的往前面的指针指向你的前一个
            next.prev = prev;
        }
    }
    @Override
    protected Node<K, V> createNode(K key, V value, Node<K, V> parent) {
        LinkedNode node = new LinkedNode(key, value, parent);
        //新添加第一个节点，那肯定头结点和尾结点都是第一个元素啊，
        if (first == null) {
            first = last = node;
        } else {
            //添加是从尾添加的，所以尾部的下一个指向当前节点
            last.next = node;
            //双向的，node的前面指向last
            node.prev = last;
            //node变成最新的last
            last = node;
        }
        return node;
    }


}
