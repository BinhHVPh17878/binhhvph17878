package com.vubq.joyboystore.services;

import com.vubq.joyboystore.entities.User;
import com.vubq.joyboystore.utils.DataTableRequest;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface UserService {

    public Optional<User> findByUserName(String userName);

    Boolean existsByUserName(String userName);

    Boolean existsByEmail(String email);

    User getById(String id);

    Page<User> getAllPage(DataTableRequest request, String type, String status);

    User save(User user);

    User findById(String id);
}
