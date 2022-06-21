package com.multi_thread_sort;

public class MergeSort {
    /**
     * 合并结果
     *
     * @param arrayLeft
     * @param arrayRight
     */
    public String[] merge(String[] arrayLeft, String[] arrayRight) {
        String[] result = new String[arrayLeft.length + arrayRight.length];
        for (int index = 0, lIndex = 0, rIndex = 0; index < result.length; index++) {
            if (lIndex > arrayLeft.length - 1) {
                // 左边数组已经取完，直接将右边数组的数放进result数组中
                result[index] = arrayRight[rIndex++];
            } else if (rIndex > arrayRight.length - 1) {
                // 右边数组已经取完，直接将左边数组的数放进result数组中
                result[index] = arrayLeft[lIndex++];
            } else if (arrayLeft[lIndex].compareTo(arrayRight[rIndex])<0) {
                // 左边数组中index位置的值更小，则取左边数组中的数放入result数组中，并且将左数组指针+1
                result[index] = arrayLeft[lIndex++];
            } else {
                result[index] = arrayRight[rIndex++];
            }
        }
        return result;
    }
}