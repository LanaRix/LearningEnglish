package com.example.myapplication;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void addition1_isCorrect() throws Exception {

        CategoryAdapter categoryAdapter = new CategoryAdapter();
        long result =categoryAdapter.getItemId(4);
        long expected = 4;
        assertEquals(expected, result);
    }
    @Test
    public void addition2_isCorrect() throws Exception {

        UserStruct userStruct = new UserStruct("1", "Пользователь 1");
        String result = userStruct.getId();
        String expected = "1";
        assertEquals(expected, result);
    }
    @Test
    public void addition3_isCorrect() throws Exception {

        UserStruct userStruct = new UserStruct("1", "Пользователь 1");
        String result = userStruct.getNane();
        String expected = "Пользователь 1";
        assertEquals(expected, result);
    }
    @Test
    public void addition4_isCorrect() throws Exception {

        ArrayList<CategoryStruct> categoryStruct = new ArrayList<CategoryStruct>();

        categoryStruct.add(new CategoryStruct("1", "Категория 1", "Перевод 1", "1"));
        categoryStruct.add(new CategoryStruct("2", "Категория 2", "Перевод 2", "2"));
        categoryStruct.add(new CategoryStruct("3", "Категория 3", "Перевод 3", "3"));

        CategoryAdapter categoryAdapter = new CategoryAdapter(categoryStruct);

        int result = categoryAdapter.getCount();
        int expected = 3;
        assertEquals(expected, result);
    }
    @Test
    public void addition5_isCorrect() throws Exception {

        ArrayList<CategoryStruct> categoryStruct = new ArrayList<CategoryStruct>();

        categoryStruct.add(new CategoryStruct("1", "Категория 1", "Перевод 1", "1"));
        categoryStruct.add(new CategoryStruct("2", "Категория 2", "Перевод 2", "2"));
        categoryStruct.add(new CategoryStruct("3", "Категория 3", "Перевод 3", "3"));

        CategoryAdapter categoryAdapter = new CategoryAdapter(categoryStruct);

        Object result = categoryAdapter.getItem(1);
        Object expected = categoryStruct.get(1);
        assertEquals(expected, result);
    }
}