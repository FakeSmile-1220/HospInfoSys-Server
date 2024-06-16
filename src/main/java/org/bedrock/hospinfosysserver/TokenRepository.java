package org.bedrock.hospinfosysserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class TokenRepository {
    private Map<String, Token> tokens;
    private Map<String, Token> tokensByContent;
    Logger logger = LoggerFactory.getLogger(TokenRepository.class);

    public TokenRepository() {
        tokens = new HashMap<>();
        tokensByContent = new HashMap<>();
    }

    public void generateToken(User user) {
        Calendar expiration = Calendar.getInstance();
        expiration.add(Calendar.HOUR, 12);
        Token token = new Token(UUID.randomUUID().toString(), user.getType(), expiration);
        tokens.put(user.getId(), token);
        tokensByContent.put(token.getContent(), token);
    }

    public Token getToken(String id) {
        return tokens.get(id);
    }

    public Token getTokenByContent(String content) {
        return tokensByContent.get(content);
    }

    public boolean verifyTokenAdmin(String content) {
        Token tokenObj = tokensByContent.get(content);
        return tokenObj != null && tokenObj.getType().equals("admin");
    }

    public String getTokenTypeByContent(String content) {
        Token tokenObj = tokensByContent.get(content);
        return tokenObj.getType();
    }
}
