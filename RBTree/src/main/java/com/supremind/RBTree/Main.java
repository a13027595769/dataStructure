package com.supremind.RBTree;

import com.supremind.RBTree.binaryTree.AVLTree;
import com.supremind.RBTree.binaryTree.RBTree;
import com.supremind.RBTree.binaryTree.RBTreeTrueBak;
import com.supremind.asserta.printer.BinaryTrees;

public class Main {

    static void test1(){
        Integer data[] = new Integer[]{
               85,19,69,3,7,99,95,2,1,70,44,58,11,21,14,93,54,4,56
        };
        AVLTree<Integer> avl = new AVLTree<>();
        for (int i = 0; i < data.length; i++) {
            avl.add(data[i]);
        }
        BinaryTrees.println(avl);
    }
    static void test2(){
        Integer data[] = new Integer[]{
                85,19,69,3,7,99,95,2,1,70,44,58,11,21,14,93,54,4,56
        };
        RBTreeTrueBak<Integer> avl = new RBTreeTrueBak<>();
        for (int i = 0; i < data.length; i++) {
            avl.add(data[i]);
        }
        BinaryTrees.println(avl);
    }
    static void test3(){
        Integer data[] = new Integer[]{
                55,87,56,74,96,22,62,20,70,68,90,50
        };
        RBTreeTrueBak<Integer> rb = new RBTreeTrueBak<>();
        for (int i = 0; i < data.length; i++) {
            rb.add(data[i]);
        }
        BinaryTrees.println(rb);
        for (int i = 0; i < data.length; i++) {
            rb.remove(data[i]);
            System.out.println("----------------------------");
            System.out.println("【" + data[i] +"】");
            BinaryTrees.println(rb);
        }
        BinaryTrees.println(rb);
    }
    static void test4(){
        Integer data[] = new Integer[]{
                85,19,69,3,7,99,95,2,1,70,44,58,11,21,14,93,54,4,56
        };
        RBTree<Integer> rb = new RBTree<>();
        for (int i = 0; i < data.length; i++) {
            rb.add(data[i]);
        }
        BinaryTrees.println(rb);
        for (int i = 0; i < data.length; i++) {
            rb.remove(data[i]);
            System.out.println("----------------------------");
            System.out.println("【" + data[i] +"】");
            BinaryTrees.println(rb);
        }
    //    BinaryTrees.println(rb);
    }

    public static void main(String[] args) {
        test4();
    }
    
}
