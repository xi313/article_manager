package com.example;

import org.junit.jupiter.api.Test;

public class test1 {
    @Test
    public void SubString(){
        String s = "[19,21]";
        s = s.substring(1,s.length()-1);
        System.out.println(s);
    }
}
