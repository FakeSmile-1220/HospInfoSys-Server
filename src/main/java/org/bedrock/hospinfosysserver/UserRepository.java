package org.bedrock.hospinfosysserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

@Repository
public class UserRepository {
    private final Logger logger = LoggerFactory.getLogger(UserRepository.class);
    private Map<String, User> users;

    public UserRepository() {
        logger.info("Initializing UserRepository");
        users = new HashMap<>();

        try {
            loadUsers();
        } catch (FileNotFoundException e) {
            logger.info("Could not find users file, using default users");
        } catch (Exception e) {
            logger.error("Could not load users file, using default users");
        }

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
//            logger.info("Saving users...");
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

    public void loadUsers() throws IOException, ClassNotFoundException {
        logger.info("Loading users...");
        File file = new File("user.dat");
        if (!file.exists()) {
            throw new FileNotFoundException("user.dat not found");
        }
        FileInputStream fis = new FileInputStream("user.dat");
        ObjectInputStream ois = new ObjectInputStream(fis);

        Object o = ois.readObject();
        //noinspection unchecked
        users = (Map<String, User>) o;
        ois.close();
        fis.close();
    }
}
