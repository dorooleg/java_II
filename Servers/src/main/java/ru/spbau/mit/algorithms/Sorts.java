package ru.spbau.mit.algorithms;

import java.util.ArrayList;
import java.util.List;

public class Sorts {
    public static List<Integer> bubbleSort(List<Integer> values) {
        values = new ArrayList<>(values);
        for (int i = 0; i < values.size(); i++) {
            for (int j = 0; j < values.size() - i - 1; j++) {
                if (values.get(j) > values.get(j + 1)) {
                    Integer tmp = values.get(j);
                    values.set(j, values.get(j + 1));
                    values.set(j + 1, tmp);
                }
            }
        }
        return values;
    }
}
