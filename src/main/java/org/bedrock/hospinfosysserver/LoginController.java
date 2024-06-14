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
    private final TokenRepository tokenRepository;
    private final Logger logger = LoggerFactory.getLogger(LoggerFactory.class);

    public LoginController(UserRepository userRepository, TokenRepository tokenRepository) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
    }

    @GetMapping("api/login")
    public ResponseEntity<Token> login(@RequestParam final String username, @RequestParam final String password) {
        logger.info("User " + username + " trying to login with password " + password);
        User user = userRepository.getUser(username);
        if (user == null || !userRepository.verifyUser(username, password)) {
            return ResponseEntity.status(403).body(null);
        }
        tokenRepository.generateToken(user);
        Token token = tokenRepository.getToken(username);
        return ResponseEntity.ok(token);
    }
}
