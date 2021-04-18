package com.demo.config;

import com.demo.models.User;
import com.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service("myUserDetailService")
public class MyUserDetailService implements UserDetailsService {
    private final UserRepository userRepository;

    @Autowired
    public MyUserDetailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String iin) throws UsernameNotFoundException {
        User user = userRepository.findByIin(iin);
        if (user == null) {
            throw new UsernameNotFoundException("User with IIN: " + iin + " not found");
        }
        GrantedAuthority roleAuthority = new SimpleGrantedAuthority(user.getRole().getName());
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        grantedAuthorities.add(roleAuthority);
        return buildUserForAuthentication(user, grantedAuthorities);
    }

    private org.springframework.security.core.userdetails.User buildUserForAuthentication(User user, List<GrantedAuthority> grantedAuthorities) {
        return new org.springframework.security.core.userdetails.User(user.getIin(), user.getPassword(), true, true, true, true, grantedAuthorities);
    }
}
