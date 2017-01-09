package com.oozee.xmppchat.ofrestclient.client;

import android.util.Base64;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;
import com.fasterxml.jackson.core.util.MinimalPrettyPrinter;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oozee.xmppchat.ofrestclient.entity.AuthenticationMode;
import com.oozee.xmppchat.ofrestclient.entity.AuthenticationToken;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

class ApiRequest<T> extends JsonRequest<T> {
    private Class<T> clazz;
    private int method;
    private AuthenticationToken token;

    ApiRequest(int method, String url, String jsonRequest, Listener<T> listener, ErrorListener errorListener, Class<T> clazz, AuthenticationToken token) {
        super(method, url.replace(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR, "%20"), jsonRequest, listener, errorListener);
        this.clazz = clazz;
        this.token = token;
        this.method = method;
    }

    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> headers = new HashMap<>();
        if (this.token.getAuthMode() == AuthenticationMode.SHARED_SECRET_KEY) {
            headers.put("Authorization", this.token.getSharedSecretKey());
        } else if (this.token.getAuthMode() == AuthenticationMode.BASIC_AUTH) {
            headers.put("Authorization", "Basic " + Base64.encodeToString((this.token.getUsername() + ":" +
                    this.token.getPassword()).getBytes(), 0));
        }
        headers.put("Accept", "application/json");
        return headers;
    }

    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString;
            if (this.method == 0) {
                jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers, "utf-8"));
            } else {
                JSONObject responseJson = new JSONObject();
                responseJson.put("code", response.statusCode);
                responseJson.put("message", response.headers.toString());
                jsonString = responseJson.toString();
            }
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT, true);
            mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
            return Response.success(mapper.readValue(jsonString, this.clazz), HttpHeaderParser.parseCacheHeaders(response));
        } catch (Throwable e) {
            return Response.error(new ParseError(e));
        }
    }
}
