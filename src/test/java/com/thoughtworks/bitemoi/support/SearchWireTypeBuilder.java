package com.thoughtworks.bitemoi.support;

import com.thoughtworks.bitemoi.gateways.search.wiretypes.BusinessWireType;
import com.thoughtworks.bitemoi.gateways.search.wiretypes.SearchWireType;

import java.util.HashSet;
import java.util.Set;

import static com.thoughtworks.bitemoi.support.BusinessWireTypeBuilder.businessWireTypeBuilder;
import static java.util.Arrays.asList;

public class SearchWireTypeBuilder {
    private Set<BusinessWireType> businesses = new HashSet<BusinessWireType>(asList(businessWireTypeBuilder().build()));

    public static SearchWireTypeBuilder searchWireTypeBuilder() {
        return new SearchWireTypeBuilder();
    }

    public SearchWireTypeBuilder withBusinesses(Set<BusinessWireType> businesses) {
        this.businesses = businesses;
        return this;
    }

    public SearchWireTypeBuilder withBusinesses(BusinessWireType first, BusinessWireType... rest) {
        HashSet<BusinessWireType> businesses = new HashSet<BusinessWireType>();
        businesses.add(first);
        businesses.addAll(asList(rest));
        return withBusinesses(businesses);
    }

    public SearchWireType build() {
        return new SearchWireType(businesses);
    }
}
