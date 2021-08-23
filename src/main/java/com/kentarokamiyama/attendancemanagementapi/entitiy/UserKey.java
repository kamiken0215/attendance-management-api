package com.kentarokamiyama.attendancemanagementapi.entitiy;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class UserKey implements Serializable {
    protected String username;
    protected String firstname;

    public UserKey() {}

    public UserKey(String username, String firstname) {
        this.username = username;
        this.firstname = firstname;
    }
    // equals, hashCode
}
