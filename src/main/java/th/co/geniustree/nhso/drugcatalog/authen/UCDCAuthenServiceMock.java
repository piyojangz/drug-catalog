/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.authen;

import th.co.geniustree.nhso.ws.authen.api.AuthenResultDto;
import th.co.geniustree.nhso.ws.authen.api.UCDCAuthenService;
import th.co.geniustree.nhso.ws.authen.api.UserDto;




/**
 *
 * @author pramoth
 */
public class UCDCAuthenServiceMock implements UCDCAuthenService {

    @Override
    public AuthenResultDto getUser(String string1, String string2) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public AuthenResultDto getUserWithPermissions(String user, String pass, String string3) {
        AuthenResultDto authenResultDto = new AuthenResultDto();
        UserDto userDto = authenResultDto.getUserDto();
        userDto.setLoginId(pass);
        userDto.setPassword(pass);
        userDto.setOrgId(user);
        return authenResultDto;
    }

    @Override
    public boolean updateMappingFirstLoginSubSystem(String string1, String string2) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
