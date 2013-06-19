package com.thoughtworks.trakemoi.modules;

import android.content.Context;
import com.google.inject.AbstractModule;
import com.thoughtworks.trakemoi.R;
import com.thoughtworks.trakemoi.gateways.search.SearchGateway;

public class SearchGatewayModule extends AbstractModule {
    private final Context context;

    public SearchGatewayModule(Context context) {
        this.context = context;
    }

    @Override protected void configure() {
        bind(SearchGateway.class)
                .toInstance(new SearchGateway(
                        context.getString(R.string.consumer_key),
                        context.getString(R.string.consumer_secret),
                        context.getString(R.string.token_key),
                        context.getString(R.string.token_secret)));
    }
}
