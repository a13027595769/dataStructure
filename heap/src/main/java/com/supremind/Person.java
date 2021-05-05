package com.supremind;

public class Person implements Comparable<Person>{
    private String name;
    private int bondBreak;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getBondBreak() {
        return bondBreak;
    }

    public Person(String name, int bondBreak) {
        this.name = name;
        this.bondBreak = bondBreak;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", bondBreak=" + bondBreak +
                '}';
    }

    public void setBondBreak(int bondBreak) {
        this.bondBreak = bondBreak;
    }

    @Override
    public int compareTo(Person person) {
        return this.bondBreak - person.bondBreak;
    }
}
