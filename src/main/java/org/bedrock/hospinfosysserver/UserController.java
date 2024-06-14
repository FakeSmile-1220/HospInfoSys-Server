package org.bedrock.hospinfosysserver;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
public class UserController {

    final private UserRepository userRepository;
    final private TokenRepository tokenRepository;

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

        return ResponseEntity.ok().body(userRepository.getUsers().values());
    }

    @PostMapping("api/user")
    public ResponseEntity<String> addUser(@RequestParam String id, @RequestParam String realName,
                                          @RequestParam String password, @RequestParam String type,
                                          @RequestParam String token) {

        User user = new User(id, realName, password, type);

        if (!tokenRepository.verifyTokenAdmin(token)) {
            return ResponseEntity
                    .status(401)
                    .body("Token does not exist or token is not admin");
        }

        userRepository.putUser(user);
        return ResponseEntity
                .ok()
                .body("Successfully added user");
    }

    @PostMapping("api/doctor")
    public ResponseEntity<String> addDoctor(@RequestParam String id, @RequestParam String realName,
                                            @RequestParam String password, @RequestParam String type,
                                            @RequestParam String deptName, @RequestParam String registLevel,
                                            @RequestParam Double registFee, @RequestParam String token) {

        Doctor doctor = new Doctor(id, realName, password, type, deptName, registLevel, registFee);

        if (!tokenRepository.verifyTokenAdmin(token)) {
            return ResponseEntity.status(401).body("Token does not exist or token is not admin");
        }

        userRepository.putUser(doctor);
        return ResponseEntity
                .ok()
                .body("Successfully added user");
    }

}
