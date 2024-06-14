package org.bedrock.hospinfosysserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Repository;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

@Repository
public class UserRepository {
    private Map<String, User> users;
    private final Logger logger = LoggerFactory.getLogger(UserRepository.class);

    public UserRepository() {
        logger.info("Initializing UserRepository");
        users = new HashMap<>();

        loadUsers();

        User admin = new User("kalen", "kalen", "123", "admin");
        users.put("kalen", admin);
    }

    public User getUser(final String id) {
        return users.get(id);
    }

    public void putUser(final User user) {
        users.put(user.getId(), user);
    }

    public Map<String, User> getUsers() {
        return users;
    }

    public boolean verifyUser(final String id, final String password) {
        final User user = getUser(id);
        if (user == null) {
            return false;
        }
        return user.password().equals(password);
    }

    @Scheduled(fixedRate = 60000)
    public void saveUsers() {
        try {
            logger.info("Saving users...");
            FileOutputStream fos = new FileOutputStream("user.dat");
            ObjectOutputStream oos = new ObjectOutputStream(fos);

            oos.writeObject(users);
            oos.close();
            fos.close();
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void loadUsers() {
        try {
            logger.info("Loading users...");
            FileInputStream fis = new FileInputStream("user.dat");
            ObjectInputStream ois = new ObjectInputStream(fis);

            Object o = ois.readObject();
            //noinspection unchecked
            users = (Map<String, User>) o;
            ois.close();
            fis.close();
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
