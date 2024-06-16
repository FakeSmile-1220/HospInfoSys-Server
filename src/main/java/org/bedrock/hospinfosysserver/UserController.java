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
    final private UserRepository userRepository;
    final private TokenService tokenService;
    final private Logger logger = LoggerFactory.getLogger(UserController.class);

    public UserController(final UserRepository doctorRepository, TokenService tokenService) {
        this.userRepository = doctorRepository;
        this.tokenService = tokenService;
    }

    @GetMapping("/api/admin/users")
    public ResponseEntity<Collection<User>> getUsers() {
        return ResponseEntity
                .ok()
                .body(userRepository.getUsers().values());
    }

    @GetMapping("api/getUser")
    public ResponseEntity<User> getUser(@RequestParam String username) {
        User user = userRepository.getUser(username);

        return ResponseEntity.ok(user);
    }

    @PostMapping("api/register/user")
    public ResponseEntity<String> addUser(@RequestBody final String userJsonString) {

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

        if (type.equals("admin")) {
            return ResponseEntity
                    .badRequest()
                    .body("admin accounts cannot be created");
        }

        User user = new User(id, realName, password, type);


        userRepository.putUser(user);
        logger.info("Added user: {}, type: {}, realName: {}", id, type, realName);
        return ResponseEntity
                .ok()
                .body("Successfully added user");
    }

    @PostMapping("api/register/doctor")
    public ResponseEntity<String> addDoctor(@RequestBody String doctorJsonString) {

        logger.info("Add doctor: {}", doctorJsonString);

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

        userRepository.putUser(doctor);
        return ResponseEntity
                .ok()
                .body("Successfully added user");
    }
}
