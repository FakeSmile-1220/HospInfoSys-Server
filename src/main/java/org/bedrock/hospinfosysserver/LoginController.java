package org.bedrock.hospinfosysserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

    private final UserRepository userRepository;
    private final TokenService tokenService;
    private final Logger logger = LoggerFactory.getLogger(LoggerFactory.class);

    public LoginController(UserRepository userRepository, TokenService tokenService) {
        this.userRepository = userRepository;
        this.tokenService = tokenService;
    }

    @GetMapping("api/login")
    public ResponseEntity<String> login(@RequestParam final String username, @RequestParam final String password) {
        logger.info("Log in request: User {} trying to login with password {}", username, password);
        User user = userRepository.getUser(username);
        if (user == null || !userRepository.verifyUser(username, password)) {
            return ResponseEntity.status(403).body(null);
        }

        String token = tokenService.generateToken(user.getId(), user.getType());
        return ResponseEntity.ok(token);
    }
}
