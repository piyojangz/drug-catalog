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
        this.FUNCTION_ADMIN = "1150";
        this.FUNCTION_SUPERADMIN = "1434";
        this.FUNCTION_HOSPITAL = "1149";
        this.FUNCTION_EMCO = "1468";
        this.FUNCTION_ECLAIM = "1245";
    }
    
}
