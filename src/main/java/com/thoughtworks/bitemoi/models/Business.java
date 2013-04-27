package com.thoughtworks.bitemoi.models;

public class Business {

    private String Name;
    private String ImageURL;
    private String Rating;
    private String Distance;
    private String Price;

    private Business(Builder businessBuilder) {
        this.Name = businessBuilder.Name;
        this.ImageURL = businessBuilder.ImageURL;
        this.Rating = businessBuilder.Rating;
        this.Distance = businessBuilder.Distance;
        this.Price = businessBuilder.Price;
    }

    public String getName() {
        return Name;
    }

    public String getImageURL() {
        return ImageURL;
    }

    public String getRating() {
        return Rating;
    }

    public String getDistance() {
        return Distance;
    }

    public String getPrice() {
        return Price;
    }


    public static class Builder {
        private String Name;
        private String ImageURL;
        private String Rating;
        private String Distance;
        public String Price;

        public Builder Name(String name) {
            this.Name = setBlankIfNull(name);
            return this;
        }

        public Builder ImageURL(String imageURL) {
            this.ImageURL = setBlankIfNull(imageURL);
            return this;
        }

        public Builder Distance(String distance) {
            this.Distance = setBlankIfNull(distance);
            return this;
        }

        public Builder Rating(String rating) {
            this.Rating = setBlankIfNull(rating);
            return this;
        }

        public Builder Price(String price) {
            this.Price = setBlankIfNull(price);
            return this;
        }


        private String setBlankIfNull(String name) {
            return (name == null) ? "" : name;
        }

        public Business build() {
            return new Business(this);
        }
    }
}

