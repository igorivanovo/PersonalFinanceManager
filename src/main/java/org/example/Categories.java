package org.example;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.*;
import java.util.*;

public class Categories implements Serializable {
    private static String[] categoryes;

    private int[][][][] categoryProduct = new int[categoryes.length][6][12][31];  //  с 2020 по 2025 годы

    public Categories() {

    }

    public static void readFile() throws IOException {
        Set<String> categor = new HashSet<>();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader("categories.tsv"))) {
            String s;
            while ((s = bufferedReader.readLine()) != null) {
                String[] parth = s.split("\t");
                String nameCategor = parth[1];
                if (!categor.contains(nameCategor)) {
                    categor.add(nameCategor);
                }
            }
        }
        categor.add("другие");
        categoryes = categor.toArray(new String[0]);
    }

    protected void add(String title, String date, long sum) throws IOException {
        String[] dataInt = date.split("\\.");
        String s = null;
        int years = Integer.parseInt(dataInt[0]) - 2020;
        int month = Integer.parseInt(dataInt[1]) - 1;
        int day = Integer.parseInt(dataInt[2]) - 1;
        long summa = sum;

        try (BufferedReader br = new BufferedReader(new FileReader("categories.tsv"))) {
            while ((s = br.readLine()) != null) {
                String[] ss = s.split("\t");
                String sss = ss[0];
                if (title.equals(sss)) {
                    String category = ss[1];
                    for (int i = 0; i < categoryes.length; i++) {
                        if (category.equals(categoryes[i])) {
                            categoryProduct[i][years][month][day] += summa;
                            break;
                        }
                    }
                    break;
                }
            }
            if ((s == null)) {
                for (int i = 0; i < categoryes.length; i++) {
                    if ("другие".equals(categoryes[i])) {
                        categoryProduct[i][years][month][day] += summa;
                        break;
                    }
                }
            }
        }
    }

    protected JSONArray max(String date, String time) {
        Map<String, Integer> maxCategory = new HashMap<>();
        for (int i = 0; i < categoryes.length; i++) {
            maxCategory.put(categoryes[i], summa(categoryProduct, date, time, i));
        }

        int max = Collections.max(maxCategory.values());
        String maxKey = null;
        for (String k : maxCategory.keySet()) {
            if (maxCategory.get(k).equals(max)) {
                maxKey = k;
                break;
            }
        }
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        jsonObject.put("category", maxKey);
        jsonObject.put("sum", max);
        jsonArray.add(jsonObject);
        return jsonArray;
    }

    protected String maxCategory(String date) {
        JSONArray jsonArrayDay = max(date, "day");
        JSONArray jsonArrayMonth = max(date, "month");
        JSONArray jsonArrayYear = max(date, "years");
        JSONArray jsonArrayAll = max(date, "all");

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("maxCategory", jsonArrayAll);
        jsonObject.put("maxYearCategory", jsonArrayYear);
        jsonObject.put("maxMonthCategory", jsonArrayMonth);
        jsonObject.put("maxDayCategory", jsonArrayDay);
        String json = jsonObject.toJSONString();
        return json;
    }

    protected Integer summa(int[][][][] category, String date, String time, int r) {
        int sum = 0;
        int b = 5;
        int c = 11;
        int d = 30;
        int a = 0;
        int e = 0;
        int o = 0;
        String[] dataInt = date.split("\\.");
        int years = Integer.parseInt(dataInt[0]) - 2020;
        int month = Integer.parseInt(dataInt[1]) - 1;
        int day = Integer.parseInt(dataInt[2]) - 1;
        switch (time) {
            case "day":
                b = a = years;
                e = c = month;
                o = d = day;
                break;
            case "month":
                b = a = years;
                e = c = month;
                break;
            case "years":
                b = a = years;
                break;
            case "all":
                break;
        }
        for (int iii = r; iii <= r; iii++) {
            for (int ii = a; ii <= b; ii++) {
                for (int i = e; i <= c; i++) {
                    for (int j = o; j <= d; j++) {
                        sum = sum + category[iii][ii][i][j];
                    }
                }
            }
        }
        return sum;
    }

}

