/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.controller.utils;

import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 *
 * @author Thanthathon
 */
public class SpringUtils {
    
    private static ServletContext getServletContext(){
        return ((HttpServletRequest)FacesContext.getCurrentInstance()
                .getExternalContext()
                .getRequest())
                .getServletContext();
    }
 
    public static <T> T getBean(Class<T> clazz){
        return WebApplicationContextUtils.getWebApplicationContext(getServletContext())
                .getBean(clazz);
    }
}
