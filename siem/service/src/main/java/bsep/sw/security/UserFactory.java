package bsep.sw.security;

import bsep.sw.domain.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

import java.util.Collection;

/**
 * A user factory which creates instances of SecurityUser object.
 */
public class UserFactory {

    private UserFactory() {
    }

    /**
     * Method that creates new SecurityUser.
     *
     * @param user user to be converted
     * @return Security User
     */
    public static SecurityUser create(User user) {
        return new SecurityUser(
                user.getId(),
                user.getUsername(),
                user.getPassword(),
                user.getRole().name(),
                AuthorityUtils.commaSeparatedStringToAuthorityList(user.getRole().name())
        );
    }

    public static SecurityUser create(final User user, final Collection<? extends GrantedAuthority> authorities) {
        return new SecurityUser(
                user.getId(),
                user.getUsername(),
                user.getPassword(),
                user.getRole().name(),
                authorities
        );
    }
}
