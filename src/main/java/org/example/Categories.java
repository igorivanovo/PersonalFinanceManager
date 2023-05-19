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

    public static String[] getCategoryes() {
        return categoryes;
    }

    public int[][][][] getCategoryProduct() {
        return categoryProduct;
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

    protected JSONObject max(String date, String time) {
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
        return jsonObject;
    }

    protected String maxCategory(String date) {
        JSONObject jsonObjectDay = max(date, "day");
        JSONObject jsonObjectMonth = max(date, "month");
        JSONObject jsonObjectYear = max(date, "years");
        JSONObject jsonObjectAll = max(date, "all");

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("maxCategory", jsonObjectAll);
        jsonObject.put("maxYearCategory", jsonObjectYear);
        jsonObject.put("maxMonthCategory", jsonObjectMonth);
        jsonObject.put("maxDayCategory", jsonObjectDay);
        String json = jsonObject.toJSONString();
        return json;
    }

    protected Integer summa(int[][][][] category, String date, String time, int cetegoryProduct) {
        int sum = 0;
        int endYear = 5;
        int endMonth = 11;
        int endDay = 30;
        int startYear = 0;
        int startMonth = 0;
        int startDay = 0;
        String[] dataInt = date.split("\\.");
        int years = Integer.parseInt(dataInt[0]) - 2020;
        int month = Integer.parseInt(dataInt[1]) - 1;
        int day = Integer.parseInt(dataInt[2]) - 1;
        switch (time) {
            case "day":
                startYear = endYear = years;
                startMonth = endMonth = month;
                startDay = endDay = day;
                break;
            case "month":
                endYear = startYear = years;
                startMonth = endMonth = month;
                break;
            case "years":
                endYear = startYear = years;
                break;
            case "all":
                break;
        }
        for (int iii = cetegoryProduct; iii <= cetegoryProduct; iii++) {
            for (int ii = startYear; ii <= endYear; ii++) {
                for (int i = startMonth; i <= endMonth; i++) {
                    for (int j = startDay; j <= endDay; j++) {
                        sum = sum + category[iii][ii][i][j];
                    }
                }
            }
        }
        return sum;
    }

    protected void saveBin(File file) throws IOException {
        try (ObjectOutputStream objectInputStream = new ObjectOutputStream(new FileOutputStream(file))) {
            objectInputStream.writeObject(this);
        }
    }

    protected static Categories loadFromBinFile(File file) throws IOException, ClassNotFoundException {

        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(file))) {
            return (Categories) inputStream.readObject();
        }

    }
}

