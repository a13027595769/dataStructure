package com.supremind.map.treemap;

import java.sql.Timestamp;
import java.util.*;

public class SQLConstructor {

    public static void main(String[] args) {
        System.out.println("阅读getCondition方法，找出其中的错误点并修复");
        System.out.println("要求不能使用第三方包，只能用JDK中的方法");
        System.out.println("要求对修复之后的方法进行测试，并打印结果");
        Map<String,Object> map = new HashMap<>();
        List<Object> list = new ArrayList<>();
        map.put("startTime","2022-07-20 11:11:11");
        map.put("DIC_TYPE",Arrays.asList(1,2,3));
        map.put("ITEM_ID",1);
        map.put("ORG_ID",1);
        map.put("DIC_CODE",1);
        String condition = getCondition(map, list);
        System.out.println("select * from xxx where "+condition);
    }

    /**
     * 构造查询SQL语句
     *
     * @param searchParams
     * @param values
     * @return 返回SQL
     */
    private static String getCondition(Map<String, Object> searchParams, List<Object> values)  {
        StringBuilder sb = new StringBuilder();
        for (String s : searchParams.keySet()) {
            if ("startTime".equals(s)) {
                if (!String.valueOf(searchParams.get("startTime")).contains(":")) {
                    throw new RuntimeException("请使用时间进行查询！");
                } else {
                    sb.append("startTime >= ? AND ");
                    values.add(paseDateFromLongStr(searchParams.get(s).toString()));
                }
            } else if (s.equals("DIC_TYPE")) {
                sb.append(" DIC_TYPE IN (?,?,?) AND ");
                String[] split = String.valueOf(searchParams.get(s)).split(",");
                if (split.length == 3) {
                    values.addAll(Arrays.asList(split));
                } else if (split.length < 3) {
                    // TODO 处理非3的情况
                    throw new RuntimeException("参数个数不合法");
                }
            } else if ("ITEM_ID".equals(s)) {
                sb.append("ITEM_ID =? AND ");
            } else if ("ORG_ID".equals(s) || "COMPANY_ID".equals(s)) {
                sb.append(s + " =?");
                values.add(searchParams.get(s));
            }
        }
        if (!searchParams.keySet().contains("DIC_CODE")) {
//            sb.append(" DIC_CODE IN ('JOB_DUTY','GROUP_JOB_DUTY','USER_POST') AND ");
            throw new RuntimeException("请选择对应的code");

        }
        int end = sb.lastIndexOf("AND");
        sb = new StringBuilder(sb.substring(0,end));
        return sb.toString();
    }

    public static Date paseDateFromLongStr(String dateStr) {
        Date date = null;
        try {
            Timestamp timestamp = Timestamp.valueOf(dateStr);
//            Long timestamp = Long.parseLong(dateStr);
//            date = new Timestamp(timestamp);
            date = timestamp;
        } catch (Exception e) {
            throw new RuntimeException(
                    "请检查日期格式(需使用时间戳格式的时间)");
        }
        return date;
    }
}