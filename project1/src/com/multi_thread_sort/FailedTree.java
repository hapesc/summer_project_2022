package com.multi_thread_sort;

/*
 * 败者树的实现
 * */

import java.util.ArrayList;

public class FailedTree<T extends Comparable> {
    private Integer[] tree = null;
    private int size = 0;
    private ArrayList<T> leaves = null;
    public FailedTree(ArrayList<T> initValues) {
        this.leaves = initValues;
        this.size = initValues.size();
        this.tree = new Integer[size];
        for (int i = 0; i < size; i++) {
            tree[i] = -1;
        }
        for (int i = size - 1; i >= 0; i--) {
            adjust(i);
        }
    }

    private void adjust(int s) {
        int t = (s + size) / 2;
        while (t > 0) {
            if (s >= 0 && (tree[t] == -1 || leaves.get(s).compareTo(leaves.get(tree[t])) > 0)) {
                int temp = s;
                s = tree[t];
                tree[t] = temp;
            }
            t /= 2;
        }
        tree[0] = s;
    }

    public void add(String leaf, int s) {
        leaves.set(s, (T) leaf);
        adjust(s);
    }

    public void del(int s) {
        leaves.remove(s);
        size--;
        tree = new Integer[size];
        for (int i = 0; i < size; i++) {
            tree[i] = -1;
        }
        for (int i = size - 1; i >= 0; i--) {
            adjust(i);
        }
    }

    public T getLeaf(int s) {
        return leaves.get(s);
    }

    public Integer getWinner() {
        return tree.length > 0 ? tree[0] : null;
    }
}
