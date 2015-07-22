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
import th.co.geniustree.nhso.drugcatalog.model.ReimburseGroup;
import th.co.geniustree.nhso.drugcatalog.repo.ReimburseGroupRepo;

/**
 *
 * @author Thanthathon
 */
@FacesConverter("reimburseGroupConverter")
public class ReimburseGroupConverter implements Converter, Serializable {

    private final ReimburseGroupRepo reimburseGroupRepo;

    public ReimburseGroupConverter() {
        reimburseGroupRepo = SpringUtils.getBean(ReimburseGroupRepo.class);
    }

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        ReimburseGroup reimburseGroup = reimburseGroupRepo.findOne(value);
        return reimburseGroup;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        return (value == null) ? null : value.toString();
    }

}
