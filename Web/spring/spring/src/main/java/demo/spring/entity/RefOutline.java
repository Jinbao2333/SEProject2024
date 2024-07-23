package demo.spring.entity;

import java.io.Serializable;

public class RefOutline implements Serializable {
    private static final long serialVersionUID = 1L;
    int paper_id;
    String title;


    public int getPaper_id() {
        return paper_id;
    }

    public void setPaper_id(int paper_id) {
        this.paper_id = paper_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
