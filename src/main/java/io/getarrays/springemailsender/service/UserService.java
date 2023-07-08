package io.getarrays.springemailsender.service;

import io.getarrays.springemailsender.domain.User;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    User saveUser(User user);
    Boolean verifyToken(String token);

}
