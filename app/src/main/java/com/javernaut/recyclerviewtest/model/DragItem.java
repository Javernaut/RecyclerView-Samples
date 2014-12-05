package com.javernaut.recyclerviewtest.model;

/**
 * Wrapper on a string for items to drag.
 */
public class DragItem {
    private String text;

    public DragItem(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
