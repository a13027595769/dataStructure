package com.supremind;

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

}
