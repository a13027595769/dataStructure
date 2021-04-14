package com.supremind;


import com.supremind.asserta.printer.BinaryTrees;
import com.supremind.binaryTree.BST;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.function.BinaryOperator;

public class MainBinarySearchTree {

    private static class PersonComparator implements Comparator<Person>{

        @Override
        public int compare(Person e1, Person e2) {
            return e1.getAge() - e2.getAge();
        }
    }
    private static class PersonComparator2 implements Comparator<Person>{

        @Override
        public int compare(Person e1, Person e2) {
            return e2.getAge() - e1.getAge();
        }
    }
    static void test1(){
        Integer data[] = new Integer[]{
                7,4,9,2,5,8,11,3,12,1
        };
        BinarySearchTree<Integer> bst = new BinarySearchTree<>();

        for (int i = 0; i < data.length; i++) {
            bst.add(data[i]);
        }
    }

    static void test2(){

        BinarySearchTree<Person> bst2 = new BinarySearchTree<>(Comparator.comparingInt(Person::getAge));
        bst2.add(new Person(12));
        bst2.add(new Person(15));
        bst2.add(new Person(7));
        bst2.add(new Person(4));
        bst2.add(new Person(9));
        bst2.add(new Person(2));
        bst2.add(new Person(5));
        BinarySearchTree<Person> bst3 = new BinarySearchTree<>(new PersonComparator());

    }

    static void test6(){
        Integer data[] = new Integer[]{
                7,4,2,1,3,5,9,8,11,10,12
        };
        BinarySearchTree<Integer> bst = new BinarySearchTree<>();

        for (int i = 0; i < data.length; i++) {
            bst.add(data[i]);
        }
        BinaryTrees.println(bst);
//        bst.levelOrderTraversal();
//        bst.levelOrder((Integer element)->{
//            System.out.print("_" + element + "_");
//            return false;
//        });
//        bst.postorderTraversal((element)->{
//            System.out.print(element + " ");
//            return false;
//        });
        bst.levelOrder(new BinarySearchTree.Visitor<Integer>(){
            public boolean visit(Integer element){
                System.out.print(element + " ");
                return false;
            }
        });
        System.out.println();
    }


    static void test9(){
        Integer data[] = new Integer[]{
                //7,4,9,2,5,1
                7,4,2,1,3,5,9,8,11,10,12
        };
        BinarySearchTree<Integer> bst = new BinarySearchTree<>();

        for (int i = 0; i < data.length; i++) {
            bst.add(data[i]);
        }
        BinaryTrees.println(bst);
        // bst.levelOrderTraversal();
        bst.preorderTraversal(new BinarySearchTree.Visitor<Integer>(){
            public boolean visit(Integer element){
                System.out.print(element + " ");
                return element == 2 ? true : false;
            }
        });
        System.out.println();
        bst.inorderTraversal(new BinarySearchTree.Visitor<Integer>(){
            public boolean visit(Integer element){
                System.out.print(element + " ");
                return element == 4 ? true : false;
            }
        });
        System.out.println();
        bst.postorderTraversal(new BinarySearchTree.Visitor<Integer>(){
            public boolean visit(Integer element){
                System.out.print(element + " ");
                return element == 4 ? true : false;
            }
        });
        System.out.println();
        bst.levelOrder(new BinarySearchTree.Visitor<Integer>(){
            public boolean visit(Integer element){
                System.out.print(element + " ");
                return element == 9 ? true : false;
            }
        });
        System.out.println();
    }

    static void test10(){
        Integer data[] = new Integer[]{
                7,4,9,2,5
                //7,4,2,1,3,5,9,8,11,10,12
        };
        BinarySearchTree<Integer> bst = new BinarySearchTree<>();

        for (int i = 0; i < data.length; i++) {
            bst.add(data[i]);
        }
        BinaryTrees.println(bst);

        System.out.println(bst.isComplete());
    }

    static void test11(){
        Integer data[] = new Integer[]{
                7,4,9,2,5,8,11,3,12,1
        };
        BinarySearchTree<Integer> bst = new BinarySearchTree<>();

        for (int i = 0; i < data.length; i++) {
            bst.add(data[i]);
        }

        BinaryTrees.println(bst);
        System.out.println(bst.height());
        bst.remove(7);
       // bst.remove(3);
        //bst.remove(12);

        System.out.println("---------------");
        BinaryTrees.println(bst);
        System.out.println("-----------------");
    }

    static void test12(){
        Integer data[] = new Integer[]{
                7,4,9,2,5,8,11,3,12,1
        };
        BST<Integer> bst = new BST<>();

        for (int i = 0; i < data.length; i++) {
            bst.add(data[i]);
        }
        System.out.println();
        BinaryTrees.println(bst);
    }

    static void test13(){
        Integer data[] = new Integer[]{
                //7,4,9,2,5,1
                7,4,2,1,3,5,9,8,11,10,12
        };
        BinarySearchTree<Integer> bst = new BinarySearchTree<>();

        for (int i = 0; i < data.length; i++) {
            bst.add(data[i]);
        }
        BinaryTrees.println(bst);
        // bst.levelOrderTraversal();
        System.out.print("前序遍历       ");
        bst.preorderTraversal(new BinarySearchTree.Visitor<Integer>(){
            public boolean visit(Integer element){
                System.out.print(element + " ");
                return false;
            }
        });
        System.out.println();
        System.out.print("中序遍历       ");
        bst.inorderTraversal(new BinarySearchTree.Visitor<Integer>(){
            public boolean visit(Integer element){
                System.out.print(element + " ");
                return false;
            }
        });
        System.out.println();
        System.out.print("后序遍历       ");
        bst.postorderTraversal(new BinarySearchTree.Visitor<Integer>(){
            public boolean visit(Integer element){
                System.out.print(element + " ");
                return false;
            }
        });
        System.out.println();
        System.out.print("层序遍历       ");
        bst.levelOrder(new BinarySearchTree.Visitor<Integer>(){
            public boolean visit(Integer element){
                if(element == 4){
                    System.out.print("D " );
                }
                else if (element == 10){
                    System.out.print("A ");
                }
                else if (element == 5){
                    System.out.print("C ");
                }
                else if (element == 7){
                    System.out.print("E " );
                }
                else if (element == 6){
                    System.out.print("B ");
                }
                else if (element == 8){
                    System.out.print("H ");
                }
                else if (element == 13){
                    System.out.print("F ");
                }
                else if (element == 11){
                    System.out.print("G ");
                }else{
                    System.out.print("I ");
                }
                //System.out.print(element + " ");
                return false;
            }
        });
        System.out.println();
        bst.add(13);
    }
     static void test14(){
        Integer data[] = new Integer[]{
                //7,4,9,2,5,1
                4,10,5,7,6,8,13,11,12
        };
        BinarySearchTree<Integer> bst;
         bst = new BinarySearchTree<>();

         for (int i = 0; i < data.length; i++) {
            bst.add(data[i]);
        }
        BinaryTrees.println(bst);
        // bst.levelOrderTraversal();
        System.out.print("前序遍历       ");
        bst.preorderTraversal(new BinarySearchTree.Visitor<Integer>(){
            public boolean visit(Integer element){
                if(element == 4){
                    System.out.print("D " );
                }
                else if (element == 10){
                    System.out.print("A ");
                }
                else if (element == 5){
                    System.out.print("C ");
                }
                else if (element == 7){
                    System.out.print("E " );
                }
                else if (element == 6){
                    System.out.print("B ");
                }
                else if (element == 8){
                    System.out.print("H ");
                }
                else if (element == 13){
                    System.out.print("F ");
                }
                else if (element == 11){
                    System.out.print("G ");
                }else{
                    System.out.print("I ");
                }
                //System.out.print(element + " ");
                return false;
            }
        });
        System.out.println();
        System.out.print("中序遍历       ");
        bst.inorderTraversal(new BinarySearchTree.Visitor<Integer>(){
            public boolean visit(Integer element){
                if(element == 4){
                    System.out.print("D " );
                }
                else if (element == 10){
                    System.out.print("A ");
                }
                else if (element == 5){
                    System.out.print("C ");
                }
                else if (element == 7){
                    System.out.print("E " );
                }
                else if (element == 6){
                    System.out.print("B ");
                }
                else if (element == 8){
                    System.out.print("H ");
                }
                else if (element == 13){
                    System.out.print("F ");
                }
                else if (element == 11){
                    System.out.print("G ");
                }else{
                    System.out.print("I ");
                }
                //System.out.print(element + " ");
                return false;
            }
        });
        System.out.println();
        System.out.print("后序遍历       ");
        bst.postorderTraversal(new BinarySearchTree.Visitor<Integer>(){
            public boolean visit(Integer element){
                if(element == 4){
                    System.out.print("D " );
                }
                else if (element == 10){
                    System.out.print("A ");
                }
                else if (element == 5){
                    System.out.print("C ");
                }
                else if (element == 7){
                    System.out.print("E " );
                }
                else if (element == 6){
                    System.out.print("B ");
                }
                else if (element == 8){
                    System.out.print("H ");
                }
                else if (element == 13){
                    System.out.print("F ");
                }
                else if (element == 11){
                    System.out.print("G ");
                }else{
                    System.out.print("I ");
                }
                //System.out.print(element + " ");
                return false;
            }
        });
        System.out.println();
         System.out.print("层序遍历       ");
         bst.levelOrder(new BinarySearchTree.Visitor<Integer>(){
             public boolean visit(Integer element){
                 if(element == 4){
                     System.out.print("D " );
                 }
                 else if (element == 10){
                     System.out.print("A ");
                 }
                 else if (element == 5){
                     System.out.print("C ");
                 }
                 else if (element == 7){
                     System.out.print("E " );
                 }
                 else if (element == 6){
                     System.out.print("B ");
                 }
                 else if (element == 8){
                     System.out.print("H ");
                 }
                 else if (element == 13){
                     System.out.print("F ");
                 }
                 else if (element == 11){
                     System.out.print("G ");
                 }else{
                     System.out.print("I ");
                 }
                 //System.out.print(element + " ");
                 return false;
             }
         });
        System.out.println();
    }

    public static void main(String[] args) {
        Integer data[] = new Integer[]{
                7,4,9,2,5,8,11,3,12,1
        };
        BinarySearchTree<Integer> bst = new BinarySearchTree<>();

        for (int i = 0; i < data.length; i++) {
            bst.add(data[i]);
        }

        BinaryTrees.println(bst);
        bst.remove(9);
        // bst.remove(3);
        //bst.remove(12);

        System.out.println("---------------");
        BinaryTrees.println(bst);
        System.out.println("-----------------");

    }
    
}
