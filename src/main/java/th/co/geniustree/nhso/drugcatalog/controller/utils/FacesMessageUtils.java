/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.controller.utils;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

/**
 *
 * @author moth
 */
public class FacesMessageUtils {

    public static void info(String msg) {
        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", msg);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public static void error(Exception e) {
        error(e.getMessage());
    }

    public static void error(String msg) {
        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", msg);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public static void warn(Exception e) {
        warn(e.getMessage());
    }

    public static void warn(String msg) {
        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Warnning", msg);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
}
