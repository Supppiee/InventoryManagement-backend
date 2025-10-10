package com.im.inventory.service;

import com.im.inventory.constants.Constant;
import com.im.inventory.dto.responses.UserCreatedResponse;
import com.im.inventory.entity.User;
import com.im.inventory.repository.UserRepository;
import com.im.inventory.util.AES;
import org.apache.logging.log4j.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public ResponseEntity<?> createUser(User user) {
            user.setPassword(AES.encrypt(user.getPassword()));
            userRepository.save(user);

            String employeeCode = Constant.EMPCODE +
                    ((int)(Math.random() * (999 - 111 + 1)) + 111) +
                    ((user.getUsername()).substring(0,3)).toUpperCase();

            UserCreatedResponse response = new UserCreatedResponse();
            response.setId(user.getId());
            response.setUsername(user.getUsername());
            response.setRole(user.getRole());
            response.setEmail(user.getEmail());
            response.setEmployeeCode(employeeCode);

            HashMap<String, Object> map = new HashMap<>();
            map.put("message","User created successfully");
            map.put("user",response);
            return new ResponseEntity<>(map, HttpStatus.CREATED);
    }

    public ResponseEntity<List<User>> getAllUsers() {
        return new ResponseEntity<>(userRepository.findAll(), HttpStatus.OK);
    }
}
