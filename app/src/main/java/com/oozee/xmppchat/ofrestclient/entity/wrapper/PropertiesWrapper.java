package com.oozee.xmppchat.ofrestclient.entity.wrapper;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.oozee.xmppchat.ofrestclient.entity.Property;

import java.io.Serializable;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PropertiesWrapper implements Serializable {

    private List<Property> property;

    public PropertiesWrapper() {

    }

    public PropertiesWrapper(List<Property> property) {
        this.property = property;
    }

    public List<Property> getProperty() {
        return this.property;
    }

    public void setProperty(List<Property> property) {
        this.property = property;
    }
}
