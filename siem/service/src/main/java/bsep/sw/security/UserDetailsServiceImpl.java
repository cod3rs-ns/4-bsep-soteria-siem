package bsep.sw.security;


import bsep.sw.domain.Privilege;
import bsep.sw.domain.Role;
import bsep.sw.domain.User;
import bsep.sw.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;


@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public UserDetailsServiceImpl(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userRepository.findOneByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException(String.format("No user found with username '%s'.", username));
        } else {
            return UserFactory.create(user, getAuthorities(user.getRoles()));
        }
    }

    private Collection<? extends GrantedAuthority> getAuthorities(final Collection<Role> roles) {
        return getGrantedAuthorities(getPrivileges(roles));
    }

    private Collection<String> getPrivileges(final Collection<Role> roles) {
        final Collection<String> privileges = new ArrayList<>();
        final Collection<Privilege> privilegeCollection = new ArrayList<>();

        for (final Role role : roles) {
            privilegeCollection.addAll(role.getPrivileges());
        }

        // removes duplicates and converts to strings
        for (final Privilege item : privilegeCollection) {
            privileges.add(item.getName());
        }

        return privileges;
    }

    private Collection<GrantedAuthority> getGrantedAuthorities(final Collection<String> privileges) {
        final Collection<GrantedAuthority> authorities = new ArrayList<>();
        for (final String privilege : privileges) {
            authorities.add(new SimpleGrantedAuthority(privilege));
        }
        return authorities;
    }
}
