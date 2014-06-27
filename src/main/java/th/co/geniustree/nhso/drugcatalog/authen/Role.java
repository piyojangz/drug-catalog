/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package th.co.geniustree.nhso.drugcatalog.authen;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

/**
 *
 * @author pramoth
 */
public interface Role {
    public static final GrantedAuthority ADMIN = new SimpleGrantedAuthority("ADMIN");
    public static final GrantedAuthority ZONE = new SimpleGrantedAuthority("ZONE");
    public static final GrantedAuthority PROVINCE = new SimpleGrantedAuthority("PROVINCE");
    public static final GrantedAuthority HOSPITAL = new SimpleGrantedAuthority("HOSPITAL");
}
