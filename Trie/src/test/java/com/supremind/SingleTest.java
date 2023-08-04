package com.supremind;

public class SingleTest {
    public static void main(String[] args) {
        Person instance1 = Person.getInstance();
        Person instance2 = Person.getInstance();
        Person instance3 = Person.getInstance();
        System.out.println(instance1 == instance2);
        System.out.println(instance1 == instance3);
    }
}
