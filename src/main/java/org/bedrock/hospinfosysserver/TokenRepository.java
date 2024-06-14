package org.bedrock.hospinfosysserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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

//    @Scheduled(fixedRate = 60000)
    public void saveRegistInfos() {
        try {
            logger.info("Saving tokens...");
            FileOutputStream fos = new FileOutputStream("tokens.dat");
            ObjectOutputStream oos = new ObjectOutputStream(fos);

            oos.writeObject(tokens);
            oos.close();
            fos.close();
        } catch (Exception e) {
            logger.error("Error saving tokens", e);
            throw new RuntimeException(e);
        }
    }

    public void loadRegistInfos() {
        try {
            FileInputStream fis = new FileInputStream("tokens.dat");
            ObjectInputStream ois = new ObjectInputStream(fis);

            Object o = ois.readObject();
            //noinspection unchecked
            tokens = (Map<String, Token>) o;
            ois.close();
            fis.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
