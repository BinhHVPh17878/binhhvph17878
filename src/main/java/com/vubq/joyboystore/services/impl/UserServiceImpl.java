package com.vubq.joyboystore.services.impl;

import com.vubq.joyboystore.entities.Role;
import com.vubq.joyboystore.entities.User;
import com.vubq.joyboystore.enums.ERole;
import com.vubq.joyboystore.repositories.UserRepository;
import com.vubq.joyboystore.services.UserService;
import com.vubq.joyboystore.utils.DataTableRequest;
import jakarta.persistence.criteria.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public Optional<User> findByUserName(String userName) {
        return this.userRepository.findByUserName(userName);
    }

    @Override
    public Boolean existsByUserName(String userName) {
        return this.userRepository.existsByUserName(userName);
    }

    @Override
    public Boolean existsByEmail(String email) {
        return this.userRepository.existsByEmail(email);
    }

    @Override
    public User getById(String id) {
        return this.userRepository.findById(id).orElse(null);
    }

    @Override
    public Page<User> getAllPage(DataTableRequest request, String type, String status) {
        PageRequest pageable = request.toPageable();
        Specification specification = new Specification() {
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder criteriaBuilder) {
                query.distinct(true);
                List<Predicate> predicates = new ArrayList<>();
                predicates.add(criteriaBuilder.like(root.get(User.Fields.fullName), "%" + request.getFilter().trim().toUpperCase() + "%"));
                Join<User, Role> userRoleJoin = root.join("roles", JoinType.LEFT);
                if(type.equals("ADMIN")) {
                    predicates.add(criteriaBuilder.and(userRoleJoin.get(Role.Fields.name).in(ERole.ROLE_CUSTOMER).not()));
                } else if(type.equals("CUSTOMER")) {
                    predicates.add(criteriaBuilder.and(userRoleJoin.get(Role.Fields.name).in(ERole.ROLE_CUSTOMER)));
                }
                if(!status.equals("ALL")) {
                    predicates.add(criteriaBuilder.equal(root.get(User.Fields.status), status));
                }
                query.where(predicates.toArray(new Predicate[]{}));
                return null;
            }
        };
        return this.userRepository.findAll(specification, pageable);
    }

    @Override
    public User save(User user) {
        return this.userRepository.save(user);
    }

    @Override
    public User findById(String id) {
        return this.userRepository.findById(id).get();
    }
}
