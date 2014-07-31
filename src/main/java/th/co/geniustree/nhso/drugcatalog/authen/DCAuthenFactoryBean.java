/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.authen;

import java.net.URL;
import org.springframework.beans.factory.FactoryBean; 
import th.co.geniustree.nhso.ws.authen.api.UCDCAuthenService;

/**
 *
 * @author pramoth
 */
public class DCAuthenFactoryBean implements FactoryBean<UCDCAuthenService> {

    private boolean lookupServiceOnStartup;
    private String endpointAddress;
    private String portName;
    private String serviceName;
    private URL namespaceUri;
    private String wsdlDocumentResource;
    private String serviceInterface;

    @Override
    public UCDCAuthenService getObject() throws Exception {
        return new UCDCAuthenServiceMock();
    }

    @Override
    public Class<?> getObjectType() {
        return UCDCAuthenService.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    public boolean isLookupServiceOnStartup() {
        return lookupServiceOnStartup;
    }

    public void setLookupServiceOnStartup(boolean lookupServiceOnStartup) {
        this.lookupServiceOnStartup = lookupServiceOnStartup;
    }

    public String getEndpointAddress() {
        return endpointAddress;
    }

    public void setEndpointAddress(String endpointAddress) {
        this.endpointAddress = endpointAddress;
    }

    public String getPortName() {
        return portName;
    }

    public void setPortName(String portName) {
        this.portName = portName;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public URL getNamespaceUri() {
        return namespaceUri;
    }

    public void setNamespaceUri(URL namespaceUri) {
        this.namespaceUri = namespaceUri;
    }

    public String getWsdlDocumentResource() {
        return wsdlDocumentResource;
    }

    public void setWsdlDocumentResource(String wsdlDocumentResource) {
        this.wsdlDocumentResource = wsdlDocumentResource;
    }

    public String getServiceInterface() {
        return serviceInterface;
    }

    public void setServiceInterface(String serviceInterface) {
        this.serviceInterface = serviceInterface;
    }
    
}
