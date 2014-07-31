/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.authen;

import java.math.BigDecimal;
import java.util.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import th.co.geniustree.nhso.basicmodel.readonly.Hospital;
import th.co.geniustree.nhso.basicmodel.readonly.Zone;
import th.co.geniustree.nhso.ws.authen.api.UserDto;

/**
 *
 * @author pramoth
 */
public class WSUserDetails implements UserDetails {

    private BigDecimal userId;
    private String username;
    private String email;
    private String password;
    private String position;
    private String staffName;
    private String fromType;
    private String orgName;
    private Hospital hospital;
    private Set<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();
    private String pid;
    private Zone zone;
    private String orgId;

    public WSUserDetails() {
    }

    public WSUserDetails(String userName, String password, GrantedAuthority... _authorities) {
        this.username = userName;
        this.password = password;
        if (this.authorities != null) {
            this.authorities.addAll(Arrays.asList(_authorities));
        }
    }

    public WSUserDetails(UserDto dto, String clearPassword) {
        this.email = dto.getEmail();
        this.userId = dto.getUsersId();
        this.username = dto.getLoginId();
        this.password = clearPassword;
        this.position = dto.getPosition();
        this.fromType = dto.getFromType();
        this.orgId = dto.getOrgId();
        if (this.fromType.equals("H")) {
            this.orgName = this.orgId+" - "+dto.getOrgName();
        } else {
            this.orgName = dto.getOrgName();
        }
        staffName = dto.getFname() + " " + dto.getLname();
        this.pid = dto.getPid();
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    public String getStaffName() {
        return staffName;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public String getPosition() {
        return position;
    }

    public BigDecimal getUserId() {
        return userId;
    }

    public String getFormType() {
        return fromType;
    }

    public String getEmail() {
        return email;
    }

    public String getOrgName() {
        return orgName;
    }

    public Hospital getHospital() {
        return hospital;
    }

    public void setHospital(Hospital hospital) {
        this.hospital = hospital;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }


    public Zone getZone() {
        return zone;
    }

    public void setZone(Zone zone) {
        this.zone = zone;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public void setFromType(String fromType) {
        this.fromType = fromType;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    @Override
    public String toString() {
        return staffName;
    }

}
