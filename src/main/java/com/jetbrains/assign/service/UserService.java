package com.jetbrains.assign.service;

import com.jetbrains.assign.model.Users;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class UserService {

    private final RestTemplate restTemplate = new RestTemplate();

    // ❗ Bỏ .json ở đây
    private static final String FIREBASE_USERS_URL = "https://assignment-34f27-default-rtdb.asia-southeast1.firebasedatabase.app/users";

    public List<Users> getAll() {
        String url = FIREBASE_USERS_URL + ".json";
        ResponseEntity<Map<String, Users>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Map<String, Users>>() {}
        );
        List<Users> list = new ArrayList<>();
        if (response.getBody() != null) {
            response.getBody().forEach((id, users) -> {
                users.setId(id);
                list.add(users);
            });
        }
        return list;
    }

    public void save(Users users) {
        String url = FIREBASE_USERS_URL + ".json";
        HttpEntity<Users> request = new HttpEntity<>(users);
        restTemplate.postForObject(url, request, Users.class);
    }

    public void update(String id, Users users) {
        String url = FIREBASE_USERS_URL + "/" + id + ".json";
        restTemplate.put(url, users);
    }

    public void delete(String id) {
        String url = FIREBASE_USERS_URL + "/" + id + ".json";
        restTemplate.delete(url);
    }

    public Users getById(String id) {
        String url = FIREBASE_USERS_URL + "/" + id + ".json";
        Users user = restTemplate.getForObject(url, Users.class);
        if (user == null) {
            throw new RuntimeException("User not found for id: " + id);
        }
        return user;
    }
}
