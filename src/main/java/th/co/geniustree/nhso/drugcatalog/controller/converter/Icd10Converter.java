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
import th.co.geniustree.nhso.basicmodel.readonly.ICD10;
import th.co.geniustree.nhso.drugcatalog.repo.ICD10Repo;

/**
 *
 * @author Thanthathon
 */
@FacesConverter("icd10Converter")
public class Icd10Converter implements Converter, Serializable {

    private final ICD10Repo icd10Repo;

    public Icd10Converter() {
        icd10Repo = SpringUtils.getBean(ICD10Repo.class);
    }

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        ICD10 icd10 = icd10Repo.findOne(value);
        return icd10;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        return (value == null) ? null : value.toString();
    }

}
