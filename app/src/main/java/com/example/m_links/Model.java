package com.example.m_links;

public class Model {
    private int id;
    private String title;
    private String description;
    private String link;
    private byte[] logo;

    public Model(int id, String title, String description, String link, byte[] logo) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.link = link;
        this.logo = logo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public byte[] getLogo() {
        return logo;
    }

    public void setLogo(byte[] logo) {
        this.logo = logo;
    }
}
