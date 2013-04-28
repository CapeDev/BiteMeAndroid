package com.thoughtworks.bitemoi.gateways.search.wiretypes;

import com.google.gson.annotations.SerializedName;

public class BusinessWireType {
    @SerializedName("name") private final String name;
    @SerializedName("image_url") private final String imageUrl;
    @SerializedName("distance") private final Double distance;
    @SerializedName("rating") private final Double rating;

    public BusinessWireType() { this(null, null, null, null); }

    public BusinessWireType(
            String name,
            String imageUrl,
            Double distance,
            Double rating) {
        this.distance = distance;
        this.rating = rating;
        this.name = name;
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Double getDistance() {
        return distance;
    }

    public Double getRating() {
        return rating;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }

        BusinessWireType that = (BusinessWireType) o;

        if (distance != null ? !distance.equals(that.distance) : that.distance != null) { return false; }
        if (imageUrl != null ? !imageUrl.equals(that.imageUrl) : that.imageUrl != null) { return false; }
        if (name != null ? !name.equals(that.name) : that.name != null) { return false; }
        if (rating != null ? !rating.equals(that.rating) : that.rating != null) { return false; }

        return true;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (imageUrl != null ? imageUrl.hashCode() : 0);
        result = 31 * result + (distance != null ? distance.hashCode() : 0);
        result = 31 * result + (rating != null ? rating.hashCode() : 0);
        return result;
    }

    @Override public String toString() {
        return String.format("%s(name: %s, image-url: %s, distance: %.2f, rating: %.0f)",
                getClass().getSimpleName(),
                name, imageUrl, distance, rating);
    }
}
