package com.multi_thread_sort;

public class MergeSort {
    /**
     * 二路归并
     *
     * @param arrayLeft
     * @param arrayRight
     */
    public static String[] merge2way(String[] arrayLeft,String[] arrayRight) {
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

    /**
     * 三路归并
     * @param arrayLeft
     * @param arrayMid
     * @param arrayRight
     * @return
     */
    public static String[] merge3way(String[] arrayLeft, String[] arrayMid, String[] arrayRight) {
        String[] destArray = new String[arrayLeft.length + arrayMid.length + arrayRight.length];
        int i = 0, j = 0, k = 0, l = 0;
        int mid1 = arrayLeft.length, mid2 = arrayMid.length, high = arrayRight.length;
        // choose smaller of the smallest in the three ranges
        while ((i < mid1) && (j < mid2) && (k < high)) {
            if (arrayLeft[i].compareTo(arrayMid[j]) < 0) {
                if (arrayLeft[i].compareTo(arrayRight[k]) < 0)
                    destArray[l++] = arrayLeft[i++];

                else
                    destArray[l++] = arrayRight[k++];
            } else {
                if (arrayMid[j].compareTo(arrayRight[k]) < 0)
                    destArray[l++] = arrayMid[j++];
                else
                    destArray[l++] = arrayRight[k++];
            }
        }

        // case where first and second ranges have
        // remaining values
        while ((i < mid1) && (j < mid2)) {
            if (arrayLeft[i].compareTo(arrayMid[j]) < 0)
                destArray[l++] = arrayLeft[i++];
            else
                destArray[l++] = arrayMid[j++];
        }

        // case where second and third ranges have
        // remaining values
        while ((j < mid2) && (k < high)) {
            if (arrayMid[j].compareTo(arrayRight[k]) < 0)
                destArray[l++] = arrayMid[j++];

            else
                destArray[l++] = arrayRight[k++];
        }

        // case where first and third ranges have
        // remaining values
        while ((i < mid1) && (k < high)) {
            if (arrayLeft[i].compareTo(arrayRight[k]) < 0)
                destArray[l++] = arrayLeft[i++];
            else
                destArray[l++] = arrayRight[k++];
        }

        // copy remaining values from the first range
        while (i < mid1)
            destArray[l++] = arrayLeft[i++];

        // copy remaining values from the second range
        while (j < mid2)
            destArray[l++] = arrayMid[j++];

        // copy remaining values from the third range
        while (k < high)
            destArray[l++] = arrayRight[k++];

        return destArray;
    }


}
