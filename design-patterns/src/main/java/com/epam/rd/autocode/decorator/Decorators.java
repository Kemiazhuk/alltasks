package com.epam.rd.autocode.decorator;

import java.util.LinkedList;
import java.util.List;

public class Decorators {

    public static List<String> evenIndexElementsSubList(List<String> sourceList) {
        List<String> evenSourceList = new LinkedList<>();
        for (int i = 0; i < sourceList.size(); i += 2) {
            evenSourceList.add(sourceList.get(i));
        }
        return evenSourceList;
    }
}
