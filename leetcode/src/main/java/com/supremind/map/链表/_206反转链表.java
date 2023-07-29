package com.supremind.map.链表;

public class _206反转链表 {
    /**
     * while(head != null){
     * ListNode tmp = head.next;
     * head.next= newHead;
     * newHead = head;
     * head = tmp;
     * }
     *
     * @param head
     * @return
     */
    public ListNode reverseList(ListNode head) {
        if (head == null || head.next == null) return head;
        ListNode newHead = reverseList(head.next);
        // 5.next就是5还是指向4，5没变，再.next变成5就翻转过来了，这个看不懂就看下面一行
        // 原来是2->1，head就是2，现在2.next是1,1.next本来是null，但是让他指向了head,就是1->2->1->2,这样一直
        //无限指着了，这一行代码就是来进行翻转的，下面的那行代码就是把最大的那个给指向空的。
        head.next.next = head;
        //然后这里，2指向空，，返回递归的，然后每个最大的都指向空，

        //注意，这里如果是到底了，那么就是1，而且这里已经翻转了，1->2->null，那么返回到上一次的递归，
        // 1->2->null,然后执行head.next.next = head;之后就是1->2->3->2->3->2因为只能获取到next，
        // 所以不会1->2->3->2->1->这样的，
        head.next = null;
        return newHead;

        /**
         *  5   4   3   2   1  null
         */
    }

    public static void main(String[] args) {
        _206反转链表 c = new _206反转链表();
        ListNode listNode = new ListNode(5);

        ListNode listNode4 = new ListNode(4);
        listNode.next = listNode4;

        ListNode listNode3 = new ListNode(3);
        listNode4.next = listNode3;
        ListNode listNode2 = new ListNode(2);
        listNode3.next = listNode2;
        ListNode listNode1 = new ListNode(1);
        listNode2.next = listNode1;
        ListNode listNode11 = c.reverseList(listNode);
//        ListNode listNode12 = c.reverseList2(listNode);
//        System.out.println(listNode12);
    }

    public ListNode reverseList2(ListNode head) {
        if (head == null || head.next == null) return head;

        ListNode newHead = null;
        while (head != null) {
            ListNode tmp = head.next;
            head.next = newHead;
            newHead = head;
            head = tmp;
        }

        return newHead;
    }

}
