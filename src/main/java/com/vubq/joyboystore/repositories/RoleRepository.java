package com.vubq.joyboystore.repositories;

import com.vubq.joyboystore.entities.Role;
import com.vubq.joyboystore.enums.ERole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {

    Optional<Role> findByName(ERole name);
}
