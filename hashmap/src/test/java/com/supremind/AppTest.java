package com.supremind;


import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.LinkedHashMultimap;
import org.junit.Test;

import java.util.*;
import java.util.HashMap;
import java.util.function.Supplier;
import java.util.LinkedHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue()
    {
        //assertTrue( true );
        System.out.println(4);
        System.out.println(Integer.toBinaryString(4));
        System.out.println(4 >>> 1);
        System.out.println(-4);
        System.out.println(Integer.toBinaryString(-4));
        System.out.println(4 >>> 1);
    }
    public static int hashCode(float value){
        return Float.floatToIntBits(value);
    }
    @Test
    public void test1() throws Exception{
        /*
        hash值，整数：
        整数值就当做hash值
        比如10的hash值就是10
        浮点数，
        将存储的二进制格式转换为整数值
        public static int hashCode(float value){
            return Float.floatToIntBits(value);
        }
         */
        System.out.println(Integer.toBinaryString(8));
        System.out.println(Float.floatToIntBits(8.0f));
        System.out.println(Integer.toBinaryString(Float.floatToIntBits(8.0f)));
    }
    @Test
    public void test2() throws Exception{
        String str = "jack";
        int length = str.length();

        int hashCode = 0;
        for (int i = 0; i < length; i++) {
            char c = str.charAt(i);
           // hashCode = hashCode * 31 + c;
            hashCode = (hashCode << 5) - hashCode + c;
        }
        System.out.println(hashCode);
        // hashcode = 0 * 31 + j;
        // hashCode = j * 31 + a;
        // hashCode = (j * 31 + c) * 31 + c;
    }
    public class User{
        private String name;

        @Override
        public String toString() {
            return "User{" +
                    "name='" + name + '\'' +
                    ", score=" + score +
                    '}';
        }

        private int score;

        public User(String name, int score) {
            this.name = name;
            this.score = score;
        }

    }
    @Test
    public void test3() throws Exception{
        User[] us = new User[]{new User("zhangsan9",90)
                ,new User("lisi",80)};
        Arrays.sort(us, new Comparator<User>() {
            @Override
            public int compare(User o1, User o2) {
                return o2.score - o1.score;
            }
        });
        Arrays.sort(us,(o1, o2) -> Integer.compare(o1.score,o2.score));
        Arrays.sort(us,(o1, o2) -> Integer.compare(o2.score,o1.score));
        Arrays.sort(us, Comparator.comparingInt(o -> o.score));
        //Arrays.sort(us,Comparator.comparingInt(o -> o.score));
        System.out.println(Arrays.toString(us));
    }
    @Test
    public void test4() throws Exception{
      Integer[] is = new Integer[]{2,3,4,6,4,3,5};
      //Arrays.sort(is,(x,y) ->Integer.compare(x,y));
        /*
        这里的参数，(x,y)，直接原封不动的给传到后面了。
        那我还用写吗？
        能不能直接写Integer.compare(x,y)，得换个样子嘛。
        Integer::compare
        一个点，换成两个冒号，就是方法引用。而且(x,y)都不用写了，方法引用
        方法引用可以使用的地方:
        类 :: 静态方法
        对象 :: 方法
        对象 :: 静态方法

         */
     // Arrays.sort(is, Comparator.comparingInt(x -> x));
      Arrays.sort(is, Integer::compare);
        System.out.println(Arrays.toString(is));
    }
    public int compare(int x,int y){
        return Integer.compare(x,y);
    }
    @Test
    public void test5() throws Exception{
        //对象：方法
        AppTest test = new AppTest();
        Integer[] is = new Integer[]{2,3,4,6,4,3,5};
        Arrays.sort(is, test::compare);
        System.out.println(Arrays.toString(is));
    }
    @Test
    public void test6() throws Exception{
        //那我还用初始化这个对象么，我直接用this不就好了
        Integer[] is = new Integer[]{2,3,4,6,4,3,5};
        Arrays.sort(is, this::compare);
        System.out.println(Arrays.toString(is));
    }
    @Test
    public void test7() throws Exception{
        //那我还用初始化这个对象么，我直接用this不就好了
        Integer[] is = new Integer[]{2,3,4,6,4,3,5};
       // System.out.println(Arrays.toString(is));
        List<Integer> la = Arrays.asList(2,3,4,6,4,3,5);
        la.forEach(x-> System.out.println(x));
        la.forEach(System.out::println);
    }
    public <T> List<T> asList(Supplier<List<T>> supplier, T... a){
        List<T> list = supplier.get();
//        for(T t:a){
//            list.add(t);
//        }
        Collections.addAll(list, a);
        return list;
    }
    @Test
    public void test8() throws Exception{
        List<Integer> la = this.asList(ArrayList::new,2,3,4,6,4,3,5);
       // List<Integer> la = Arrays.asList(2,3,4,6,4,3,5);
        for (Integer integer : la) {
            System.out.println(integer);
        }
        System.out.println(la.getClass());
    }
    @Test
    public void test9() throws Exception{
        List<Integer> la = this.asList(LinkedList::new,2,3,4,6,4,3,5);
         List<Integer> la2 = Arrays.asList(2,3,4,6,4,3,5);
        System.out.println(la.getClass());

       // T1.print1();
//        Stream.of("one", "two", "three", "four")
//                .peek(e -> System.out.println("peek value: " + e))
//                .forEach(e -> System.out.println("forEach value: " + e));
//        Stream.of(5, 2, 3, 4,1)
//                .peek(e -> System.out.println("peek value: " + e))
//                .forEach(e -> System.out.println("forEach value: " + e));
//        System.out.println("------------------------");
        Stream.of(5, 2, 3, 4,1)
                .peek(e -> System.out.println("peek value: " + e))
                .sorted()
                .forEach(e -> System.out.println("forEach value: " + e));

    }
    public interface T1{
        default void print(){
            System.out.println("hello");
        }
        static void print1(){
            System.out.println("hello2");
        }
    }
    @Test
    public void test10() throws Exception{
//       String s1 = new String("aa");
//       String s2 = new String("aa");
//       String s3 = s1.intern();
//       String s4 = s2.intern();
//       String s5 = "aa";
//       System.out.println(s1 == s2);//
//       System.out.println(s1 == s3);//
//       System.out.println(s1 == s4);//
//       System.out.println(s1 == s5);//

        int a = 1, b = 2, c = 3;
        String s1 = String.format("%d%d%d",a,b,c);
        String s2 = String.format("%d%d%d",a,b,c);
        String s3 = s1.intern();
        String s4 = s2.intern();
        String s5 = "123";
        System.out.println(s1 == s2);
        System.out.println(s1 == s3);
        System.out.println(s1 == s4);
        System.out.println(s1 == s5);


    }

//
//    private static boolean isSkippedStackElement(StackTraceElement element) {
//        for (String skipped : SKIPPED_STACK_ELEMENTS) {
//            if (element.getClassName().startsWith(skipped)) {
//                return true;
//            }
//        }
//        return false;
//    }


    @Test
    public void test15() throws Exception{
        HashMap<String,Integer> map = new HashMap<>();
        map.put("k1",1);
        map.put("k2",1);
        map.put("k3",1);
        map.put("k4",1);
//     The bin count threshold for using a tree rather than list for a
//     bin.  Bins are converted to trees when adding an element to a
//     bin with at least this many nodes. The value must be greater
//     than 2 and should be at least 8 to mesh with assumptions in
//     tree removal about conversion back to plain bins upon
//     shrinkage
    }

    @Test
    public void test16() throws Exception{
        Optional<Object> empty = Optional.empty();
        ImmutableList.of(1,2,3);
        ImmutableSet.builder().add(1).add();
    }
}
