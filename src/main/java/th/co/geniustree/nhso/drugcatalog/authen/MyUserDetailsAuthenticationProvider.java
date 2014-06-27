/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.authen;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import th.co.geniustree.drugserver.wsauthen.AuthenResultDto;
import th.co.geniustree.drugserver.wsauthen.UCDCAuthenService;
import th.co.geniustree.drugserver.wsauthen.UserDto;
import th.co.geniustree.nhso.basicmodel.repository.HospitalRepository;
import th.co.geniustree.nhso.drugcatalog.service.NhsoZoneService;


/**
 *
 * @author pramoth
 */
public class MyUserDetailsAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider implements Role {

    private static final Logger log = LoggerFactory.getLogger(MyUserDetailsAuthenticationProvider.class);
    @Autowired
    private UCDCAuthenService dcService;
    @Autowired
    private NhsoZoneService nhsoZoneService;
    @Autowired
    private HospitalRepository hospitalRepo;

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
    }

    @Override
    protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        AuthenResultDto authenResultDto = dcService.getUserWithPermissions(username, authentication.getCredentials().toString(), "40");
        log.info("Login DC {}", ToStringBuilder.reflectionToString(authenResultDto));
        if (authenResultDto.getUserDto() != null) {
            UserDto userDto = authenResultDto.getUserDto();
            WSUserDetails wsUserDetails = null;
            if ("O".equalsIgnoreCase(userDto.getFromType())) {
                wsUserDetails = new WSUserDetails(userDto, authentication.getCredentials().toString());
                wsUserDetails.getAuthorities().add(Role.ADMIN);
                
            } else if ("Z".equalsIgnoreCase(userDto.getFromType())) {
                wsUserDetails = new WSUserDetails(userDto, authentication.getCredentials().toString());
                wsUserDetails.getAuthorities().add(Role.ZONE);
                wsUserDetails.setZone(nhsoZoneService.findZoneByOrgId(userDto.getOrgId()));
            } else if ("P".equalsIgnoreCase(userDto.getFromType())) {
                wsUserDetails = new WSUserDetails(userDto, authentication.getCredentials().toString());
                wsUserDetails.getAuthorities().add(Role.PROVINCE);
                wsUserDetails.setHospital(hospitalRepo.findByHcode(userDto.getOrgId()));
            } else if ("H".equalsIgnoreCase(userDto.getFromType())) {
                wsUserDetails = new WSUserDetails(userDto, authentication.getCredentials().toString());
                wsUserDetails.getAuthorities().add(Role.HOSPITAL);
                wsUserDetails.setHospital(hospitalRepo.findByHcode(userDto.getOrgId()));
            } else {
                throw new AuthenticationCredentialsNotFoundException("Not found user. " + username);
            }
            wsUserDetails.setPid(userDto.getPid());
            return wsUserDetails;
        } else {
            throw new AuthenticationCredentialsNotFoundException("Not found user. " + username);
        }
    }

}
