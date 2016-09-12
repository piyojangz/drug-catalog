package th.co.geniustree.nhso.drugcatalog.authen;

/**
 *
 * @author pramoth
 */
public class MyUserDetailsAuthenticationProviderTest extends MyUserDetailsAuthenticationProvider {

    @Override
    protected void defineFunctionId() {
        this.FUNCTION_ADMIN = "1291";
        this.FUNCTION_SUPERADMIN = "1294";
        this.FUNCTION_HOSPITAL = "1292";
        this.FUNCTION_EMCO = "1293";
        this.FUNCTION_ECLAIM = "1295";
    }

}
