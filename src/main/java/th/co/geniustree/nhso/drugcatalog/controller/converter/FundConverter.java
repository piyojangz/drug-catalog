/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.controller.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import org.springframework.beans.factory.annotation.Autowired;
import th.co.geniustree.nhso.drugcatalog.model.Fund;
import th.co.geniustree.nhso.drugcatalog.repo.FundRepo;

/**
 *
 * @author Thanthathon
 */
@FacesConverter("fundConverter")
public class FundConverter implements Converter {

    @Autowired
    private FundRepo fundRepo;

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        Fund fund = fundRepo.findOne(value);
        return fund;

    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        return (value == null) ? null : value.toString();
    }

}
