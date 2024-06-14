package org.bedrock.hospinfosysserver;

import java.io.Serializable;

public class User implements Serializable {
    private final String id;
    private final String realName;
    private final String password;
    private final String type;


    public User(String id, String realName, String password, String type) {
        this.id = id;
        this.realName = realName;
        this.password = password;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public String getRealName() {
        return realName;
    }

    public String password() {
        return password;
    }

    public String getType() {
        return type;
    }
}
