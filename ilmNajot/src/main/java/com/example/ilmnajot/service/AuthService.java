package com.example.ilmnajot.service;

import com.example.ilmnajot.entity.User;
import com.example.ilmnajot.entity.api.ApiResponse;
import com.example.ilmnajot.entity.enums.RoleName;
import com.example.ilmnajot.entity.payload.RegisterDTO;
import com.example.ilmnajot.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Service
public class AuthService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JavaMailSender javaMailSender;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email).orElseThrow(()
                -> new UsernameNotFoundException(email));

    }

    public ApiResponse registerUser(RegisterDTO registerDTO) {
        if (userRepository.existsByEmail(registerDTO.getEmail())) {
            return new ApiResponse("User already exists", false);
        }
        User user = new User(
                registerDTO.getFullName(),
                registerDTO.getEmail(),
                passwordEncoder.encode(registerDTO.getPassword()),
                RoleName.ROLE_USER
        );
        int randomNumber = new Random().nextInt(999999);
        user.setEmailCode(String.valueOf(randomNumber).substring(0, 4));
    userRepository.save(user);
return new ApiResponse("User registered successfully", true);
    }

    public Boolean sendMail(String sendingMail, String emailCode) {
        try {
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setTo(sendingMail);
            simpleMailMessage.setFrom("noReply@gmail.com");
            simpleMailMessage.setSubject("Email Verification");
            simpleMailMessage.setText(emailCode);
            javaMailSender.send(simpleMailMessage);
            return true;
        } catch (Exception e) {
            return false;

        }
    }
    public ApiResponse verifyEmail(String email, String emailCode) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
                if (optionalUser.isPresent()){
                    User user = optionalUser.get();
                    if (user.getEmailCode().equals(emailCode)) {
                        user.setEnabled(true);
                        userRepository.save(user);
                        return new ApiResponse("Email verified successfully", true);
                    } else {
                        return new ApiResponse("Email verification failed", false);
                    }
                }
                return new ApiResponse("Email verification failed",false);
    }
}
