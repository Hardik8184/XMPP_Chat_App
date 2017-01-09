package com.oozee.xmppchat.ofrestclient.entity.wrapper;

import java.util.List;

public class UserGroups {
    private List<String> groupNames;

    public UserGroups(List<String> groupNames) {
        this.groupNames = groupNames;
    }

    public List<String> getGroupNames() {
        return this.groupNames;
    }

    public void setGroupNames(List<String> groupNames) {
        this.groupNames = groupNames;
    }
}
