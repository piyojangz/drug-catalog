/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.authen;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import th.co.geniustree.nhso.basicmodel.repository.HospitalRepository;
import th.co.geniustree.nhso.drugcatalog.service.NhsoZoneService;
import th.co.geniustree.nhso.ws.authen.api.AuthenResultDto;
import th.co.geniustree.nhso.ws.authen.api.MenuDto;
import th.co.geniustree.nhso.ws.authen.api.UCDCAuthenService;
import th.co.geniustree.nhso.ws.authen.api.UserDto;

/**
 *
 * @author pramoth
 */
public abstract class MyUserDetailsAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider implements Role {

    private static final Logger log = LoggerFactory.getLogger(MyUserDetailsAuthenticationProvider.class);
    @Autowired
    private UCDCAuthenService dcService;
    @Autowired
    private NhsoZoneService nhsoZoneService;
    @Autowired
    private HospitalRepository hospitalRepo;

    protected final String categoryId = "51";
    protected String functionIdSuperAdmin;
    protected String functionIdAdmin;
    protected String functionIdHospital;
    protected String functionIdEmco;
    protected String functionIdEclaim;

    protected abstract void defineFunctionId();

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
    }

    @Override
    protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        defineFunctionId();
        AuthenResultDto authenResultDto = dcService.getUserWithPermissions(username, authentication.getCredentials().toString(), "51");
        log.info("Login DC {}", ToStringBuilder.reflectionToString(authenResultDto));
        if (authenResultDto.getUserDto() != null) {
            UserDto userDto = authenResultDto.getUserDto();
            WSUserDetails wsUserDetails = new WSUserDetails(userDto, authentication.getCredentials().toString());
            if (categoryContains(authenResultDto, "51") && hasFunction(authenResultDto, functionIdSuperAdmin, "GC2")) {
                wsUserDetails.getAuthorities().addAll(Arrays.asList(Role.SUPER_ADMIN, Role.ADMIN));
            } else if (categoryContains(authenResultDto, "51") && hasFunction(authenResultDto, functionIdAdmin, "GC2")) {
                wsUserDetails.getAuthorities().add(Role.ADMIN);
            } else if ("Z".equalsIgnoreCase(userDto.getFromType()) && categoryContains(authenResultDto, "51")) {
                wsUserDetails.getAuthorities().add(Role.ZONE);
                wsUserDetails.setZone(nhsoZoneService.findZoneByOrgId(userDto.getOrgId()));
            } else if ("P".equalsIgnoreCase(userDto.getFromType()) && categoryContains(authenResultDto, "51")) {
                wsUserDetails.getAuthorities().add(Role.PROVINCE);
                wsUserDetails.setHospital(hospitalRepo.findByHcode(userDto.getOrgId()));
            } else if (categoryContains(authenResultDto, "51") && hasFunction(authenResultDto, functionIdEmco, "GC2")) {
                wsUserDetails.getAuthorities().addAll(Arrays.asList(Role.EMCO, Role.HOSPITAL));
            } else if ("H".equalsIgnoreCase(userDto.getFromType()) && categoryContains(authenResultDto, "51") && hasFunction(authenResultDto, functionIdHospital, "GC2")) {
                wsUserDetails.getAuthorities().add(Role.HOSPITAL);
                wsUserDetails.setHospital(hospitalRepo.findByHcode(userDto.getOrgId()));
            } else if ("H".equalsIgnoreCase(userDto.getFromType()) && categoryContains(authenResultDto, "51") && hasFunction(authenResultDto, functionIdEclaim, "GC2")) {
                wsUserDetails.getAuthorities().add(Role.ECLAIM);
                wsUserDetails.setHospital(hospitalRepo.findByHcode(userDto.getOrgId()));
            }
            wsUserDetails.setPid(userDto.getPid());
            log.debug(ToStringBuilder.reflectionToString(wsUserDetails));
            return wsUserDetails;
        } else {
            throw new AuthenticationCredentialsNotFoundException("Not found user. " + username);
        }
    }

    private boolean categoryContains(AuthenResultDto authenResultDto, String checkCategory) {
        Set<String> categorys = new HashSet<String>();
        List<MenuDto> menus = authenResultDto.getMenus();
        addSubMenu(menus, categorys);
        return categorys.contains(checkCategory);
    }

    private void addSubMenu(List<MenuDto> menus, Set<String> categorys) {
        for (MenuDto menu : menus) {
            addSubMenu(menu.getSubMenus(), categorys);
            categorys.add(menu.getCategory());
        }
    }

    private boolean hasFunction(AuthenResultDto authenResultDto, String functionGroupId, String functionGroupType) {
        Set<String> functionGroupIds = new HashSet<String>();
        Set<String> functionGroupTypes = new HashSet<String>();
        List<MenuDto> menus = authenResultDto.getMenus();
        addFunctionGroup(menus, functionGroupIds, functionGroupTypes);
        return functionGroupIds.contains(functionGroupId) && functionGroupTypes.contains(functionGroupType);
    }

    private void addFunctionGroup(List<MenuDto> menus, Set<String> functionGroupIds, Set<String> functionGroupTypes) {
        for (MenuDto menu : menus) {
            addFunctionGroup(menu.getSubMenus(), functionGroupIds, functionGroupTypes);
            if (menu.getFunctGroupId() != null) {
                functionGroupIds.add(menu.getFunctGroupId().toString());
            }
            if (menu.getFunctGroupType() != null) {
                functionGroupTypes.add(menu.getFunctGroupType());
            }
        }
    }

}
