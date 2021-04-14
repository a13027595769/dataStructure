package com.supremind.链表;

public class _206反转链表 {
    /**
     *   while(head != null){
     *             ListNode tmp = head.next;
     *             head.next= newHead;
     *             newHead = head;
     *             head = tmp;
     *         }
     * @param head
     * @return
     */
    public ListNode reverseList(ListNode head) {
        if(head == null || head.next == null) return head;
        ListNode newHead = reverseList(head.next);
        // 5.next就是5还是指向4，5没变，再.next变成5就翻转过来了，
        head.next.next = head;
        //这个还是原来的那个5，翻转过来，就应该是null了；
        head.next = null;
        return newHead;

        /**
         *  5   4   3   2   1  null
         */
    }

    public static void main(String[] args) {
        _206反转链表 c=  new _206反转链表();
        ListNode listNode = new ListNode(5);

        ListNode listNode4 =   new ListNode(4);
        listNode.next = listNode4;

        ListNode listNode3 =   new ListNode(3);
        listNode4.next = listNode3;
        ListNode listNode2 =   new ListNode(2);
        listNode3.next = listNode2;
        ListNode listNode1 =   new ListNode(1);
        listNode2.next = listNode1;
        ListNode listNode11 = c.reverseList2(listNode);
        System.out.println(listNode11);
    }

    public ListNode reverseList2(ListNode head) {
        if(head == null || head.next == null) return head;

        ListNode newHead = null;
        while(head != null){
            ListNode tmp = head.next;
            head.next = newHead;
            newHead= head;
            head = tmp;
        }

        return newHead;
    }

}
