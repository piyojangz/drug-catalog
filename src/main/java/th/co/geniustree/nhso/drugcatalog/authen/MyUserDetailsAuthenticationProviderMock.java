/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.authen;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import th.co.geniustree.nhso.basicmodel.readonly.Hospital;
import th.co.geniustree.nhso.basicmodel.repository.HospitalRepository;
import th.co.geniustree.nhso.drugcatalog.service.NhsoZoneService;

/**
 *
 * @author pramoth
 */
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class MyUserDetailsAuthenticationProviderMock extends AbstractUserDetailsAuthenticationProvider implements Role {

    private static final Logger log = LoggerFactory.getLogger(MyUserDetailsAuthenticationProviderMock.class);
    @Autowired
    private HospitalRepository hospitalRepo;
    @Autowired
    private NhsoZoneService nhsoZoneService;

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
    }

    @Override
    protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        log.debug("{} try to login ",username);
        if (username == null || username.isEmpty()) {
            throw new UsernameNotFoundException("Check username.");
        }
        if (authentication.getCredentials() == null) {
            throw new UsernameNotFoundException("Check password.");
        }
        
        WSUserDetails wsUserDetails;
        if ("nhso".equalsIgnoreCase(username)) {
            wsUserDetails = new WSUserDetails(username, username, new GrantedAuthority[]{Role.ADMIN});
            wsUserDetails.setOrgName("สำนักงานหลักประกันสุขภาพแห่งชาติ");
            wsUserDetails.setFromType("O");
        } else if ("z".equalsIgnoreCase(username)) {
            wsUserDetails = new WSUserDetails(username, username, new GrantedAuthority[]{Role.ZONE});
            wsUserDetails.setOrgName("สำนักงานหลักประกันสุขภาพแห่งชาติ (เขต)");
            wsUserDetails.setFromType("Z");
            wsUserDetails.setZone(nhsoZoneService.findZoneByOrgId("1"));
        } else if ("p".equalsIgnoreCase(username)) {
            wsUserDetails = new WSUserDetails(username, username, new GrantedAuthority[]{Role.PROVINCE});
            wsUserDetails.setFromType("P");
            Hospital hospital = hospitalRepo.findByHcode("00037");
            if (hospital == null) {
                throw new UsernameNotFoundException("Not found HCODE " + username);
            }
            wsUserDetails.setHospital(hospital);
            wsUserDetails.setOrgName(hospital.getHname());
        } else {
            wsUserDetails = new WSUserDetails(username, username, new GrantedAuthority[]{Role.HOSPITAL});
            Hospital hospital = hospitalRepo.findByHcode(username);
            if (hospital == null) {
                throw new UsernameNotFoundException("Not found HCODE " + username);
            }
            wsUserDetails.setHospital(hospital);
            wsUserDetails.setOrgName(hospital.getHname());
            wsUserDetails.setFromType("H");
        }
        wsUserDetails.setPid(username);
        wsUserDetails.setStaffName(username + " " + username);
        wsUserDetails.setOrgId(username);
        return wsUserDetails;
    }
}
