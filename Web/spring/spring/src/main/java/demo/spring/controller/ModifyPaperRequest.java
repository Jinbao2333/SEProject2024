package demo.spring.controller;

import demo.spring.entity.Paper;

import java.util.List;

public class ModifyPaperRequest {
    List<String> Authors;
    String conference;
    String link;
    String date;
    List<Integer> field;
    List<String> fileList;
    String title;
    String type;
    String summary;
    int user_id;
    String content;
    List<Integer> references;
    int origin_upload_id;
    int origin_paper_id;

    public Paper paper_merge(){
        Paper paper=new Paper();
        paper.setTitle(title);
        paper.setConference(conference);
        paper.setSummary(summary);
        paper.setDate(date);
        paper.setLink(link);
        paper.setType(type);
        return paper;
    }

    public List<String> getAuthors() {
        return Authors;
    }

    public void setAuthors(List<String> authors) {
        Authors = authors;
    }

    public String getConference() {
        return conference;
    }

    public void setConference(String conference) {
        this.conference = conference;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<Integer> getField() {
        return field;
    }

    public void setField(List<Integer> field) {
        this.field = field;
    }

    public List<String> getFileList() {
        return fileList;
    }

    public void setFileList(List<String> fileList) {
        this.fileList = fileList;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<Integer> getReferences() {
        return references;
    }

    public void setReferences(List<Integer> references) {
        this.references = references;
    }

    public int getOrigin_upload_id() {
        return origin_upload_id;
    }

    public void setOrigin_upload_id(int origin_upload_id) {
        this.origin_upload_id = origin_upload_id;
    }

    public int getOrigin_paper_id() {
        return origin_paper_id;
    }

    public void setOrigin_paper_id(int origin_paper_id) {
        this.origin_paper_id = origin_paper_id;
    }
}
