package com.thoughtworks.bitemoi;

public class Restaurant {
    private final String name;
    private final Double rating;
    private final String imageUrl;

    public Restaurant(String name, Double rating, String imageUrl) {
        this.name = name;
        this.rating = rating;
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public Double getRating() {
        return rating;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
