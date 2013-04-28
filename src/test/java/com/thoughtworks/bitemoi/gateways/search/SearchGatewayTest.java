package com.thoughtworks.bitemoi.gateways.search;

import com.google.gson.Gson;
import com.thoughtworks.bitemoi.gateways.search.support.OAuthRequestFactory;
import com.thoughtworks.bitemoi.gateways.search.wiretypes.SearchWireType;
import org.junit.Test;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;

import static com.thoughtworks.bitemoi.support.BusinessWireTypeBuilder.businessWireTypeBuilder;
import static com.thoughtworks.bitemoi.support.SearchJson.searchJson;
import static com.thoughtworks.bitemoi.support.SearchWireTypeBuilder.searchWireTypeBuilder;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

public class SearchGatewayTest {
    @Test
    public void addsTheSearchTermAndLatitudeAndLongitudeAsQueryStringParameters() {
        // Given
        String term = "sushi";
        Double latitude = 37.788022;
        Double longitude = -122.399797;

        OAuthService oAuthService = mock(OAuthService.class);
        OAuthRequestFactory oAuthRequestFactory = mock(OAuthRequestFactory.class);
        Token token = mock(Token.class);
        Gson gson = new Gson();

        OAuthRequest request = mock(OAuthRequest.class);
        Response response = mock(Response.class);

        when(oAuthRequestFactory.create(Verb.GET, "http://api.yelp.com/v2/search")).thenReturn(request);
        when(request.send()).thenReturn(response);

        SearchGateway searchGateway = new SearchGateway(oAuthService, oAuthRequestFactory, token, gson);

        // When
        searchGateway.search(term, latitude, longitude);

        // Then
        verify(request).addQuerystringParameter("term", term);
        verify(request).addQuerystringParameter("ll", latitude + "," + longitude);
    }

    @Test
    public void signsTheRequestUsingTheTokenProvidedAtInitialisationTime() {
        // Given
        String term = "sushi";
        Double latitude = 37.788022;
        Double longitude = -122.399797;

        OAuthService oAuthService = mock(OAuthService.class);
        OAuthRequestFactory oAuthRequestFactory = mock(OAuthRequestFactory.class);
        Token token = mock(Token.class);
        Gson gson = new Gson();

        OAuthRequest request = mock(OAuthRequest.class);
        Response response = mock(Response.class);

        when(oAuthRequestFactory.create(Verb.GET, "http://api.yelp.com/v2/search")).thenReturn(request);
        when(request.send()).thenReturn(response);

        SearchGateway searchGateway = new SearchGateway(oAuthService, oAuthRequestFactory, token, gson);

        // When
        searchGateway.search(term, latitude, longitude);

        // Then
        verify(oAuthService).signRequest(token, request);
    }

    @Test
    public void convertsTheResponseBodyIntoARestaurant() {
        // Given
        String term = "sushi";
        Double latitude = 37.788022;
        Double longitude = -122.399797;

        SearchWireType expectedSearchWireType = searchWireTypeBuilder()
                .withBusinesses(
                        businessWireTypeBuilder()
                                .withName("Derek's Diner")
                                .build(),
                        businessWireTypeBuilder()
                                .withName("Jane's Bakery")
                                .build())
                .build();

        String searchJson = searchJson()
                .from(expectedSearchWireType)
                .asString();

        OAuthService oAuthService = mock(OAuthService.class);
        OAuthRequestFactory oAuthRequestFactory = mock(OAuthRequestFactory.class);
        Token token = mock(Token.class);
        Gson gson = new Gson();

        OAuthRequest request = mock(OAuthRequest.class);
        Response response = mock(Response.class);

        when(oAuthRequestFactory.create(Verb.GET, "http://api.yelp.com/v2/search")).thenReturn(request);
        when(request.send()).thenReturn(response);
        when(response.getBody()).thenReturn(searchJson);

        SearchGateway searchGateway = new SearchGateway(oAuthService, oAuthRequestFactory, token, gson);

        // When
        SearchWireType actualSearchWireType = searchGateway.search(term, latitude, longitude);

        // Then
        assertThat(actualSearchWireType, is(expectedSearchWireType));
    }
}
