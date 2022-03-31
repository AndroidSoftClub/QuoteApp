package com.kessi.quotey.textquotes.model;

public class QuotesModel {

    private int id;
    private String quotes;
    private String category;
    private String favourite;

    public QuotesModel(int id, String category, String quotes, String favourite) {
        this.id = id;
        this.quotes = quotes;
        this.category = category;
        this.favourite = favourite;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQuotes() {
        return quotes;
    }

    public void setQuotes(String quotes) {
        this.quotes = quotes;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getFavourite() {
        return favourite;
    }

    public void setFavourite(String favourite) {
        this.favourite = favourite;
    }
}
