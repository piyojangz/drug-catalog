/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.controller.converter;

import java.io.Serializable;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import th.co.geniustree.nhso.drugcatalog.controller.utils.SpringUtils;
import th.co.geniustree.nhso.drugcatalog.model.NDC24;
import th.co.geniustree.nhso.drugcatalog.repo.NDC24Repo;

/**
 *
 * @author Thanthathon
 */
@FacesConverter("ndc24Converter")
public class NDC24Converter implements Converter, Serializable {

    private final NDC24Repo ndc24Repo;

    public NDC24Converter() {
        ndc24Repo = SpringUtils.getBean(NDC24Repo.class);
    }

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        NDC24 ndc24 = ndc24Repo.findOne(value);
        return ndc24;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        return (value == null) ? null : value.toString();
    }

}
