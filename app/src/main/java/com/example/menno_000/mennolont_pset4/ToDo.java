package com.example.menno_000.mennolont_pset4;

/**
 * Created by menno_000 on 27-9-2017.
 */

import java.io.Serializable;

public class ToDo implements Serializable {
    private String title;
    private String completed;
    private int _id;

    // Constructor for todoes from database
    public ToDo(String todoTitle, String todoCompleted) {
        title = todoTitle;
        completed = todoCompleted;
    }

    public ToDo(String todoTitle, String todoCompleted, int todoID) {
        title = todoTitle;
        completed = todoCompleted;
        _id = todoID;
    }

    // Get the to do title
    public String getTitle() { return title; }

    // Get the to do completed
    public String getCompleted() { return completed; }

    // Get the to do ID
    public int getID() { return _id; }

    // Set the to do title
    public void setTitle(String newTitle) { title = newTitle; }

    // Set the to do completed
    public void setCompleted(String newCompleted) { completed = newCompleted; }

    // Set the to do ID
    public void setID(int iD) { _id = iD; }
}
