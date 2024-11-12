package com.graduates.test.resposity;

import com.graduates.test.model.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserResposity extends JpaRepository<UserEntity,Integer> {
    Optional<UserEntity> findByUsername(String username);
    @Query(value = "SELECT u FROM UserEntity u " +
            "JOIN u.roles r " +
            "JOIN u.address a " + // Thực hiện phép nối với Address
            "WHERE (:username IS NULL OR u.username LIKE %:username%) AND " +
            "(:email IS NULL OR u.email LIKE %:email%) AND " +
            "(:fullname IS NULL OR u.fullname LIKE %:fullname%) AND " +
            "(:dob IS NULL OR u.dob = :dob) AND " +
            "(:phone IS NULL OR u.phone LIKE %:phone%) AND " +
            "(:street IS NULL OR a.street LIKE %:street%) AND " + // Thêm điều kiện cho street
            "(:city IS NULL OR a.city LIKE %:city%) AND" +
            "(r.name = 'ROLE_USER')") // Thêm điều kiện cho city
    Page<UserEntity> searchUser(@Param("username") String username,
                               @Param("email") String email,
                               @Param("fullname") String fullname,
                               @Param("dob") String dob,
                               @Param("phone") String phone,
                                @Param("street") String street,
                                @Param("city") String city,
                               Pageable pageable);

    List<UserEntity> findByRoles_Name(String roleName);
   // @Query("SELECT COUNT(o) FROM user_roles o where role_id=1 ")
   @Query("SELECT COUNT(u) FROM UserEntity u " +
           "JOIN u.roles r " +
           "WHERE r.name = 'ROLE_USER'")
    long countTotalUser();

    UserEntity findAllByUsername(String username);
}
