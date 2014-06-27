package th.co.geniustree.nhso.drugcatalog.authen;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;



public class SecurityUtil {

    public static WSUserDetails getUserDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        WSUserDetails userDetails = (WSUserDetails) authentication.getPrincipal();
        return userDetails;
    }

    public static boolean isLoggedIn() {
        return SecurityContextHolder.getContext().getAuthentication().isAuthenticated();
    }
}
