package com.supremind.栈;

import java.util.HashMap;
import java.util.Stack;

public class _20_有效的括号 {
    public boolean isValid (String s){
        Stack<Character> stack = new Stack<>();
        int len = s.length();
        for (int i = 0; i < len; i++) {
            char c = s.charAt(i);
            if(c == '(' || c == '{' || c == '['){
                stack.push(c);
            }else {
                //这就是直接右括号之类的，没有左边的
                if(stack.isEmpty()) return false;
                //进到这里就说明栈里面有数据，而且必须是左边的
                char left = stack.pop();
                if(left == '(' && c != ')') return false;
                if(left == '{' && c != '}') return false;
                if(left == '[' && c != ']') return false;

            }
        }
        return stack.isEmpty();
    }
    private HashMap<Character,Character> map = new HashMap<>();

    public _20_有效的括号() {
       map.put('(',')');
       map.put('[',']');
       map.put('{','}');
    }

    public boolean isValid2 (String s){
        Stack<Character> stack = new Stack<>();
        int len = s.length();
        for (int i = 0; i < len; i++) {
            char c = s.charAt(i);
            if(map.containsKey(c)){
                stack.push(c);
            }else {
                //这就是直接右括号之类的，没有左边的
                if(stack.isEmpty()) return false;
                //进到这里就说明栈里面有数据，而且必须是左边的
                if(c != map.get(stack.pop())) return false;
            }
        }
        return stack.isEmpty();
    }
}
