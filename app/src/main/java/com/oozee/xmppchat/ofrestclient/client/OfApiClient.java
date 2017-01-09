package com.oozee.xmppchat.ofrestclient.client;

import android.content.Context;

public class OfApiClient {
    public static RestApiClient with(Context context) {
        return RestApiClient.instance(context);
    }
}
