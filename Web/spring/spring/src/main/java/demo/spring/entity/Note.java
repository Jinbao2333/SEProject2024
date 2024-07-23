package demo.spring.entity;

import java.io.Serializable;

public class Note implements Serializable {
    private static final long serialVersionUID = 1L;
    private int note_id;
    private String content;

    public int getNote_id() {
        return note_id;
    }

    public void setNote_id(int note_id) {
        this.note_id = note_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
