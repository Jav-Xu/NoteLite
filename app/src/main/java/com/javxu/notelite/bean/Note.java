package com.javxu.notelite.bean;

import org.litepal.crud.DataSupport;

import java.util.Date;

/**
 * Created by Jav-Xu on 2016/12/18.
 */

public class Note extends DataSupport {

    private int id;

    private String noteTitle;
    private String noteContent;
    private String noteImagePath;
    private Date noteDate;
    private boolean noteSolved;

    public Note() {
        noteTitle = new String();
        noteContent = new String();
        noteImagePath = new String();
        noteDate = new Date();
        noteSolved = false;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNoteTitle() {
        return noteTitle;
    }

    public void setNoteTitle(String noteTitle) {
        this.noteTitle = noteTitle;
    }

    public String getNoteContent() {
        return noteContent;
    }

    public void setNoteContent(String noteContent) {
        this.noteContent = noteContent;
    }

    public String getNoteImagePath() {
        return noteImagePath;
    }

    public void setNoteImagePath(String noteImagePath) {
        this.noteImagePath = noteImagePath;
    }

    public Date getNoteDate() {
        return noteDate;
    }

    public void setNoteDate(Date noteDate) {
        this.noteDate = noteDate;
    }

    public boolean isNoteSolved() {
        return noteSolved;
    }

    public void setNoteSolved(boolean noteSolved) {
        this.noteSolved = noteSolved;
    }

}
