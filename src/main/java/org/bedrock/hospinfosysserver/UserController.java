package org.bedrock.hospinfosysserver;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
public class UserController {

    private final boolean tokenRequired = false;

    final private UserRepository userRepository;
    final private TokenRepository tokenRepository;
    final private Logger logger = LoggerFactory.getLogger(UserController.class);

    public UserController(final UserRepository doctorRepository, TokenRepository tokenRepository) {
        this.userRepository = doctorRepository;
        this.tokenRepository = tokenRepository;
    }

    @GetMapping("/api/users")
    public ResponseEntity<Collection<User>> getUsers(@RequestParam String token) {
        Token tokenObj = tokenRepository.getTokenByContent(token);

        if (tokenObj == null) {
            return ResponseEntity.status(401).body(null);
        }

        return ResponseEntity
                .ok()
                .body(userRepository.getUsers().values());
    }

    @GetMapping("api/getUser")
    public ResponseEntity<User> getUser(@RequestParam String username,
                                        @RequestParam String token) {
        User user = userRepository.getUser(username);

        if (user == null || tokenRepository.getToken(username) == null) {
            return ResponseEntity.status(401).body(null);
        } else {
            return ResponseEntity.ok(user);
        }
    }

    @PostMapping("api/user")
    public ResponseEntity<String> addUser(@RequestBody final String userJsonString,
                                          @RequestParam(required = tokenRequired) String token) {

        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(userJsonString);
        } catch (JSONException e) {
            return ResponseEntity
                    .status(400)
                    .body("Json parsing error");
        }

        String id = jsonObject.optString("id");
        String realName = jsonObject.optString("realName");
        String password = jsonObject.optString("password");
        String type = jsonObject.optString("type");

        User user = new User(id, realName, password, type);

        if (tokenRequired && !tokenRepository.verifyTokenAdmin(token)) {
            return ResponseEntity
                    .status(401)
                    .body("Token does not exist or token is not admin");
        }

        userRepository.putUser(user);
        logger.info("Added user: {}, type: {}, realName: {}", id, type, realName);
        return ResponseEntity
                .ok()
                .body("Successfully added user");
    }

    @PostMapping("api/doctor")
    public ResponseEntity<String> addDoctor(@RequestBody String doctorJsonString,
                                            @RequestParam(required = tokenRequired) String token) {

        logger.info("Add doctor: {}, token: {}", doctorJsonString, token);

        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(doctorJsonString);
        } catch (JSONException e) {
            return ResponseEntity
                    .status(400)
                    .body("Json parsing error");
        }

        String id = jsonObject.optString("id");
        String realName = jsonObject.optString("realName");
        String password = jsonObject.optString("password");
        String type = jsonObject.optString("type");
        String deptName = jsonObject.optString("deptName");
        String registLevel = jsonObject.optString("registLevel");
        Double registFee = jsonObject.optDouble("registFee");

        Doctor doctor = new Doctor(id, realName, password, type, deptName, registLevel, registFee);

        if (tokenRequired && !tokenRepository.verifyTokenAdmin(token)) {
            return ResponseEntity.status(401).body("Token does not exist or token is not admin");
        }

        userRepository.putUser(doctor);
        return ResponseEntity
                .ok()
                .body("Successfully added user");
    }
}
