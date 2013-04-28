package com.thoughtworks.bitemoi.support;

import com.thoughtworks.bitemoi.gateways.search.wiretypes.BusinessWireType;

public class BusinessWireTypeBuilder {
    private String name = "Derek's Diner";
    private String imageUrl = "http://www.example.com/image.jpg";
    private Double distance = 1.2;
    private Double rating = 3.0;

    public static BusinessWireTypeBuilder businessWireTypeBuilder() {
        return new BusinessWireTypeBuilder();
    }

    public BusinessWireTypeBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public BusinessWireTypeBuilder withImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    public BusinessWireTypeBuilder withDistance(Double distance) {
        this.distance = distance;
        return this;
    }

    public BusinessWireTypeBuilder withRating(Double rating) {
        this.rating = rating;
        return this;
    }

    public BusinessWireType build() {
        return new BusinessWireType(name, imageUrl, distance, rating);
    }
}
