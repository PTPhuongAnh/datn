package com.graduates.test.resposity;

import com.graduates.test.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRespository extends JpaRepository<Role,Integer> {
    Optional<Role> findByName(String name);



}
