package com.kessi.quotey.textquotes.model;

public class CategoryModel {

    private String category;
    private String category_size;

    public CategoryModel(String category, String category_size) {
        this.category_size = category_size;
        this.category = category;
    }


    public String getCategory_size() {
        return category_size;
    }

    public void setCategory_size(String quotes) {
        this.category_size = category_size;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

}
