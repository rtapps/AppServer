package com.rtapps.security;


import com.rtapps.db.mongo.data.AdminUser;
import com.rtapps.db.mongo.repository.AdminUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class AdminUserDetailsService implements UserDetailsService {

    @Autowired
    private AdminUserRepository adminUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        /*Here add user data layer fetching from the MongoDB.
          I have used userRepository*/
        AdminUser adminUser = adminUserRepository.findByUsername(username);
        if(adminUser == null){
            throw new UsernameNotFoundException(username);
        }else{
            UserDetails details = new AdminUserDetails(adminUser);
            return details;
        }
    }
}
