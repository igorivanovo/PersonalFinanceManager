package org.example;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

class CategoriesTest {

    @Test
    void test_readFile() throws IOException {
        String actualcategoryes[] = {"одежда", "еда", "финансы", "быт", "другие"};
        Categories.readFile();
        String expectedcategoryes[] = Categories.getCategoryes();

        Assertions.assertArrayEquals(expectedcategoryes, actualcategoryes);

    }

    @Test
    void test_add() throws IOException {
        Categories.readFile();
        Categories categories = new Categories();

        int[][][][] actualcategoryProduct = new int[5][6][12][31];
        actualcategoryProduct[1][2][1][7] = 120;
        categories.add("булка", "2022.02.08", 120);
        int[][][][] expectedcategoryProduct = categories.getCategoryProduct();

        Assertions.assertArrayEquals(expectedcategoryProduct, actualcategoryProduct);
    }

    @Test
    void test_maxCategory() throws IOException {
        Categories.readFile();
        Categories categories = new Categories();
        categories.add("булка", "2022.01.01", 100);
        categories.add("тапки", "2022.01.04", 180);
        categories.add("мыло", "2022.02.01", 150);
        categories.add("акции", "2022.03.01", 500);
        categories.add("игрушки", "2023.04.11", 590);
        String json = categories.maxCategory("2022.03.01");

        JSONParser parser = new JSONParser();
        JSONObject coderollsJSONObject = new JSONObject();
        try {
            coderollsJSONObject = (JSONObject) parser.parse(json);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        JSONObject jsonObject = (JSONObject) coderollsJSONObject.get("maxDayCategory");
        String ecpectedcategory = (String) jsonObject.get("category");
        String actualcategory = "финансы";

        Assertions.assertEquals(ecpectedcategory, actualcategory);

        long ecpectedsum = (Long) jsonObject.get("sum");
        long actualsum = 500;

        Assertions.assertEquals(ecpectedsum, actualsum);

    }

}