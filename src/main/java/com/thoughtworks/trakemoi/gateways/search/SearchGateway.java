package com.thoughtworks.trakemoi.gateways.search;

import com.google.gson.Gson;
import com.thoughtworks.trakemoi.gateways.search.authentication.YelpOAuthApi;
import com.thoughtworks.trakemoi.gateways.search.support.OAuthRequestFactory;
import com.thoughtworks.trakemoi.gateways.search.wiretypes.SearchWireType;
import org.scribe.builder.ServiceBuilder;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;

public class SearchGateway {
    private final OAuthService oAuthService;
    private final Token token;
    private final Gson gson;
    private final OAuthRequestFactory oAuthRequestFactory;

    public SearchGateway(
            String consumerKey,
            String consumerSecret,
            String token,
            String tokenSecret) {
        this(
                new ServiceBuilder()
                        .provider(YelpOAuthApi.class)
                        .apiKey(consumerKey)
                        .apiSecret(consumerSecret)
                        .build(),
                new OAuthRequestFactory(),
                new Token(token, tokenSecret),
                new Gson()
        );
    }

    public SearchGateway(
            OAuthService oAuthService,
            OAuthRequestFactory oAuthRequestFactory,
            Token token,
            Gson gson) {
        this.oAuthService = oAuthService;
        this.oAuthRequestFactory = oAuthRequestFactory;
        this.token = token;
        this.gson = gson;
    }

    public SearchWireType search(String term, double latitude, double longitude) {
        OAuthRequest request = oAuthRequestFactory.create(Verb.GET, "http://api.yelp.com/v2/search");
        request.addQuerystringParameter("term", term);
        request.addQuerystringParameter("ll", latitude + "," + longitude);

        oAuthService.signRequest(token, request);

        Response response = request.send();

        return gson.fromJson(response.getBody(), SearchWireType.class);
    }
}
