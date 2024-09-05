package com.vubq.joyboystore.controllers;

import com.vubq.joyboystore.config.security.jwt.JwtUtils;
import com.vubq.joyboystore.config.security.services.UserDetailsImpl;
import com.vubq.joyboystore.dtos.LoginDto;
import com.vubq.joyboystore.dtos.TokenDto;
import com.vubq.joyboystore.services.RoleService;
import com.vubq.joyboystore.services.UserService;
import com.vubq.joyboystore.utils.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/webapi/auth")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserService userService;

    @Autowired
    RoleService roleService;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/login")
    public Response authenticateUser(@RequestBody LoginDto loginDto) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getUserName(), loginDto.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());
//
//        User user = this.userService.findById(userDetails.getId()).orElse(null);

        return Response.build().ok().data(TokenDto.builder().token(jwt).roles(roles).build());
    }

}

