package com.multi_thread_sort;

import java.util.Arrays;

public class StringSort implements SortActuator {

    @Override
    public String[] sort(String[] originArray) {
        Arrays.sort(originArray);
        return originArray;
    }
}
