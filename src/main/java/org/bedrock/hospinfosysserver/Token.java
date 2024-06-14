package org.bedrock.hospinfosysserver;

import java.util.Calendar;
import java.util.Date;

public class Token {
    private String content;
    private String type;
    private Calendar expiration;

    public Token(String content, String type, Calendar expiration) {
        this.content = content;
        this.type = type;
        this.expiration = expiration;
    }

    public String getContent() {
        return content;
    }

    public String getType() {
        return type;
    }
}
