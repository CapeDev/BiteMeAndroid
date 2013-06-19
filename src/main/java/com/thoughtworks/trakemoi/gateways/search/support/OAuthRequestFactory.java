package com.thoughtworks.trakemoi.gateways.search.support;

import org.scribe.model.OAuthRequest;
import org.scribe.model.Verb;

public class OAuthRequestFactory {
    public OAuthRequest create(Verb verb, String url) {
        return new OAuthRequest(verb, url);
    }
}
