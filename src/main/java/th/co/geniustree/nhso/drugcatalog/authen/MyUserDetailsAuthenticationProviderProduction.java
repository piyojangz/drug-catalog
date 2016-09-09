/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.authen;

/**
 *
 * @author pramoth
 */
public class MyUserDetailsAuthenticationProviderProduction extends MyUserDetailsAuthenticationProvider implements Role {
    
    @Override
    protected void defineFunctionId() {
        this.functionIdAdmin = "1150";
        this.functionIdSuperAdmin = "1434";
        this.functionIdHospital = "1149";
        this.functionIdEmco = "1468";
        this.functionIdEclaim = "1245";
    }
    
}
