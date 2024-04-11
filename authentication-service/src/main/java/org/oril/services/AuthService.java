package org.oril.services;

import lombok.AllArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.oril.entities.AuthRequest;
import org.oril.entities.AuthResponse;
import org.oril.entities.UserVO;
import org.oril.util.NotValidException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.oril.util.ErrorResponse;

import java.util.Date;

@Service
@AllArgsConstructor
public class AuthService {

    private final RestTemplate restTemplate;
    private final JwtUtil jwtUtil;


    public AuthResponse register(AuthRequest request) {
        try {
            request.setPassword(BCrypt.hashpw(request.getPassword(), BCrypt.gensalt()));
            UserVO registeredUser = restTemplate.postForObject("http://user-service/users/register", request, UserVO.class);
            return generateTokens(registeredUser);
        } catch (Exception e) {
            return new AuthResponse();
        }
    }

    public AuthResponse login(AuthRequest request) {
        try {
            UserVO loginUser = restTemplate.postForObject("http://user-service/users/login", request, UserVO.class);
            return generateTokens(loginUser);
        } catch (Exception e) {
            return new AuthResponse();
        }
    }
    public AuthResponse generateTokens(UserVO user) {
        String accessToken = jwtUtil.generate(user.getName(), user.getRole().toString(), "ACCESS");
        String refreshToken = jwtUtil.generate(user.getName(), user.getRole().toString(), "REFRESH");

        return new AuthResponse(accessToken, refreshToken);
    }

}
