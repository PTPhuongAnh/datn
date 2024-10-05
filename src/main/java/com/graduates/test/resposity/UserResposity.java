package com.graduates.test.resposity;

import com.graduates.test.model.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserResposity extends JpaRepository<UserEntity,Integer> {
    Optional<UserEntity> findByUsername(String username);
    Boolean existsByUsername(String username);

    @Query("SELECT u FROM UserEntity u WHERE " +
            "(:username IS NULL OR u.username LIKE %:username%) AND " +
            "(:email IS NULL OR u.email LIKE %:email%) AND " +
            "(:fullname IS NULL OR u.fullname LIKE %:fullname%) AND " +
            "(:dob IS NULL OR u.dob = :dob) AND " +
            "(:phone IS NULL OR u.phone LIKE %:phone%)  "
            )
    Page<UserEntity> searchUser(
            @Param("username") String username,
            @Param("email") String email,
            @Param("fullname") String fullname,
            @Param("dob") String dob,
            @Param("phone") String phone,
            Pageable pageable);
}
