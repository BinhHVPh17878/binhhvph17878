package com.vubq.joyboystore.controllers;

import com.vubq.joyboystore.config.security.services.UserDetailsImpl;
import com.vubq.joyboystore.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class BaseController {

    @Autowired
    private UserService userService;

    public String getTheCurrentlyLoggedInUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return userDetails.getId();
    }

    public String getUserNameByUserId(String userId) {
        return this.userService.getById(userId).getUserName();
    }
}
