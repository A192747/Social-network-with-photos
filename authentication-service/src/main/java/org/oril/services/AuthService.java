package org.oril.services;

import lombok.AllArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.oril.dto.UserDTO;
import org.oril.entities.AuthResponse;
import org.oril.entities.User;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@AllArgsConstructor
public class AuthService {

    private final RestTemplate restTemplate;
    private final JwtUtil jwtUtil;


    public AuthResponse register(UserDTO request) {
        try {
            request.setPassword(BCrypt.hashpw(request.getPassword(), BCrypt.gensalt()));
            User registeredUser = restTemplate.postForObject("http://user-service/users/register", request, User.class);
            return generateTokens(registeredUser);
        } catch (Exception e) {
            return new AuthResponse();
        }
    }

    public AuthResponse login(UserDTO request) {
        try {
            User loginUser = restTemplate.postForObject("http://user-service/users/login", request, User.class);
            return generateTokens(loginUser);
        } catch (Exception e) {
            return new AuthResponse();
        }
    }

    public AuthResponse generateTokens(User user) {
        String accessToken = jwtUtil.generate(user.getId(), user.getName(), user.getRole().toString(), "ACCESS");
        String refreshToken = jwtUtil.generate(user.getId(), user.getName(), user.getRole().toString(), "REFRESH");

        return new AuthResponse(accessToken, refreshToken);
    }

}
