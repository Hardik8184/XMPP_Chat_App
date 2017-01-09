package com.oozee.xmppchat.ofrestclient.client;

import com.android.volley.VolleyError;

public class Reason {
    private VolleyError error;

    public Reason(VolleyError error) {
        this.error = error;
    }

    public VolleyError getError() {
        return this.error;
    }

    public void setError(VolleyError error) {
        this.error = error;
    }
}
