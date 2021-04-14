package com.supremind.队列;

public class Main {
    public static void main(String[] args) {
        sum(1,7);

    }
    public static void sum(int m,int n){
        int s = 0; 
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++)
                s++;

            System.out.println(s);
        }
    }
}
