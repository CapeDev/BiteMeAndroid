package com.thoughtworks.bitemoi.models;

public class Restaurant {
    private String name;
    private String imageURL;
    private String rating;
    private String distance;
    private String price;

    private Restaurant(Builder restaurantBuilder) {
        this.name = restaurantBuilder.name;
        this.imageURL = restaurantBuilder.imageURL;
        this.rating = restaurantBuilder.rating;
        this.distance = restaurantBuilder.distance;
        this.price = restaurantBuilder.price;
    }

    public String getName() {
        return name;
    }

    public String getImageURL() {
        return imageURL;
    }

    public String getRating() {
        return rating;
    }

    public String getDistance() {
        return distance;
    }

    public String getPrice() {
        return price;
    }

    public static class Builder {
        private String name;
        private String imageURL;
        private String rating;
        private String distance;
        private String price;

        public Builder withName(String name) {
            this.name = setBlankIfNull(name);
            return this;
        }

        public Builder withImageURL(String imageURL) {
            this.imageURL = setBlankIfNull(imageURL);
            return this;
        }

        public Builder withDistance(String distance) {
            this.distance = setBlankIfNull(distance);
            return this;
        }

        public Builder withRating(String rating) {
            this.rating = setBlankIfNull(rating);
            return this;
        }

        public Builder withPrice(String price) {
            this.price = setBlankIfNull(price);
            return this;
        }

        public Restaurant build() {
            return new Restaurant(this);
        }

        private String setBlankIfNull(String name) {
            return (name == null) ? "" : name;
        }
    }
}

