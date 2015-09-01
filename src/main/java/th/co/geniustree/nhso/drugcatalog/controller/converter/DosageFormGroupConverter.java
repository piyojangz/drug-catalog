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
import th.co.geniustree.nhso.drugcatalog.model.DosageFormGroup;
import th.co.geniustree.nhso.drugcatalog.repo.DosageFormGroupRepo;

/**
 *
 * @author Thanthathon
 */
@FacesConverter("dosageFormGroupConverter")
public class DosageFormGroupConverter implements Converter, Serializable {
 
    private final DosageFormGroupRepo repo;

    public DosageFormGroupConverter() {
        repo = SpringUtils.getBean(DosageFormGroupRepo.class);
    }

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        DosageFormGroup group = repo.findOne(value);
        return group;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        return (value == null) ? null : value.toString();
    }

}
