package com.supremind;

import java.util.Objects;
import java.util.HashMap;
import java.util.Map;

public class TestBinCount {
    public static void main(String[] args) {
        Map<SimpleBO, String> stringMap = new HashMap<>();
        for (int i = 0; i < 100; i++) {
            stringMap.put(new SimpleBO("test" + i), "test" + i);
        }
    }
    static class SimpleBO {
        private String name;
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
        public SimpleBO(String name) {
            this.name = name;
        }
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            SimpleBO simpleBO = (SimpleBO) o;
            return Objects.equals(name, simpleBO.getName());
        }
        @Override
        public int hashCode() {
            return 11241;
        }
    }
}
