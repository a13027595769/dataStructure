package com.supremind.map.RBTree;

import java.math.BigInteger;
import java.util.BitSet;

public class Person implements Comparable<Person>{
    private int age;
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Person(int age, String name) {
        this.age = age;
        this.name = name;
    }

    public Person(int age) {
        this.age = age;
    }

    public int getAge() {
        return age;
    }

    @Override
    public String toString() {
        return ""+age + "" + name;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public int compareTo(Person e) {
        return age - e.age;
    }

    public static void main(String[] args) {
        //flipBit就是对第几位取反 8 4 2 1 码，15-> 1111
        BigInteger bigInteger = BigInteger.valueOf(15);
        System.out.println("origion = "+bigInteger);
        //1110 - > 14
        bigInteger = bigInteger.flipBit(0);
        System.out.println("first = "+bigInteger);
        // 1100  -> 12
        bigInteger = bigInteger.flipBit(1);
        System.out.println("second = "+bigInteger);
        bigInteger = bigInteger.flipBit(2);
        // 1000 -> 8
        System.out.println("third = "+bigInteger);

        BitSet bitSet = new BitSet();
        System.out.println("bit set - >" + bitSet);
        bitSet.set(10);
        bitSet.set(20);
        System.out.println("bit set - >" + bitSet);

    }
}
