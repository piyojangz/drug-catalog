package th.co.geniustree.nhso.drugcatalog.authen;

/**
 *
 * @author pramoth
 */
public class MyUserDetailsAuthenticationProviderTest extends MyUserDetailsAuthenticationProvider {

    @Override
    protected void defineFunctionId() {
        this.functionIdAdmin = "1291";
        this.functionIdSuperAdmin = "1294";
        this.functionIdHospital = "1292";
        this.functionIdEmco = "1293";
        this.functionIdEclaim = "1295";
    }

}
