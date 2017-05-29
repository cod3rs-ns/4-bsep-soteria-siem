package bsep.sw.security;

import bsep.sw.domain.User;
import bsep.sw.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;


@Component
public class UserSecurityUtil {

    private final UserService userService;

    @Autowired
    public UserSecurityUtil(final UserService userService){
        this.userService = userService;
    }

    /**
     * Method that get currently logged-in user from SecurityContext and retrieves full object from database.
     *
     * @return User that is currently logged-in
     */
    public User getLoggedUser() {

        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            return null;
        }

        return userService.getUserByUsername(authentication.getName());
    }

    /**
     * Retrieves only username of the logged-in user.
     *
     * @return username of the currently logged-in user.
     */
    public String getLoggedUserUsername() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

    /**
     * Method that checks if current user have provided Authority role.
     *
     * @param authType AuthorityRole
     * @return true if user have provided AuthorityRole, false otherwise
     */
    public boolean checkAuthType(String authType) {
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities().contains(new SimpleGrantedAuthority(authType));
    }

    /**
     * Check if user have permission to apply action.
     *
     * @param currentUsername original (owner) username
     * @param userRole        role of the user
     * @return true if user haver permission, false otherwise
     */
    public boolean checkPermission(String currentUsername, String userRole) {
        if (checkAuthType(userRole) && !currentUsername.equals(getLoggedUserUsername())) {
            return false;
        }
        return true;
    }

}
