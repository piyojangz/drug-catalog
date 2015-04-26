/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.controller.converter;

import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlInputText;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

/**
 *
 * @author moth
 */
public class EmptyToNullAndTrimConverter implements Converter {

    public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
        if (value == null || value.trim().length() == 0) {
            if (component instanceof EditableValueHolder) {
                ((EditableValueHolder) component).setSubmittedValue(null);
            }
            return null;
        } else {
            if (component instanceof HtmlInputText) {
                return value.trim();
            }
        }
        return value;
    }

    public String getAsString(FacesContext facesContext, UIComponent component, Object value) {
        return (value == null) ? null : value.toString();
    }

}
