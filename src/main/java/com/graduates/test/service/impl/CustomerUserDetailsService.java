package com.graduates.test.service.impl;

import com.graduates.test.model.Role;
import com.graduates.test.model.UserEntity;
import com.graduates.test.resposity.UserResposity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerUserDetailsService implements UserDetailsService {
    @Autowired
  private UserResposity userResposity;


    public CustomerUserDetailsService(UserResposity userResposity) {
        this.userResposity = userResposity;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        UserEntity user=userResposity.findByUsername(username).orElseThrow(()
//                -> new UsernameNotFoundException("username not found"));
//   //  return  new User(user.getUsername(),user.getPassword(),mapRolesToAuthorities(user.getRoles()));
//        return new CustomUserDetails(user);
//  }
//    private Collection<GrantedAuthority> mapRolesToAuthorities(List<Role> roles) {
//        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
//    }
        UserEntity user = userResposity.findByUsername(username).orElseThrow(() ->
                new UsernameNotFoundException("Username not found: " + username));

        return new CustomUserDetails(user);
    }

    private Collection<GrantedAuthority> mapRolesToAuthorities(List<Role> roles) {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
    }
}
