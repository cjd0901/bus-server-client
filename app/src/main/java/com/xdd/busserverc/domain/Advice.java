package com.xdd.busserverc.domain;

public class Advice {
    private int id;
    private String createTime;
    private String content;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Advice{" +
                "id=" + id +
                ", createTime='" + createTime + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
