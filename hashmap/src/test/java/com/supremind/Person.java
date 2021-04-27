package com.supremind;

import java.util.Objects;

public class Person {
    private int age;
    private float height;
    private String name;

    public Person(int age, float height, String name) {
        this.age = age;
        this.height = height;
        this.name = name;
    }

    @Override
    public boolean equals(Object obj) {
        //内存地址都相等了，那铁定是一个对象啊，直接返回true就行了。
        if(this == obj) return true;
        //obj必定不能为null，我能调用equals，我本身肯定不是null啊。
        if(obj == null || obj.getClass() != getClass()) return false;
        //比较成员变量
        Person person = (Person)obj;
        //这里没有getAge之类的get方法，因为这里是强转的
        return person.age == age
                && person.height == height
                && Objects.equals(person.name,name);
    }

//    @Override
//    public int hashCode() {

    @Override
    public int hashCode() {
        return Objects.hash(age, height, name);
    }
//        int hashCode = Integer.hashCode(age);
//        hashCode = hashCode * 31 + Float.hashCode(height);
//        hashCode = hashCode * 31 + Objects.hashCode(name);
//        return hashCode;
//    }

    public static void main(String[] args) {
        Person p1 = new Person(10,1.67f,"jack");
        Person p2 = new Person(10,1.67f,"jack");
        System.out.println(p1.hashCode());
        System.out.println(p2.hashCode());
    }
}
