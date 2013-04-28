package com.thoughtworks.bitemoi.gateways.search.wiretypes;

import com.google.gson.annotations.SerializedName;

import java.util.Iterator;
import java.util.Set;

public class SearchWireType {
    @SerializedName("businesses") private final Set<BusinessWireType> businesses;

    public SearchWireType() { this(null); }

    public SearchWireType(Set<BusinessWireType> businesses) {
        this.businesses = businesses;
    }

    public Set<BusinessWireType> getBusinesses() {
        return businesses;
    }

    @Override
    public boolean equals(Object that) {
        return true;
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Override public String toString() {
        StringBuilder businessesString = new StringBuilder("[");
        Iterator<BusinessWireType> businessWireTypeIterator = businesses.iterator();
        while (businessWireTypeIterator.hasNext()) {
            businessesString.append(businessWireTypeIterator.toString());
            if (businessWireTypeIterator.hasNext()) {
                businessesString.append(", ");
            }
        }
        return String.format("%s(businesses: %s)",
                getClass().getSimpleName(),
                businessesString);
    }
}
