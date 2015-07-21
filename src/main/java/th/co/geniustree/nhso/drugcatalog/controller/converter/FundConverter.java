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
import th.co.geniustree.nhso.drugcatalog.model.Fund;
import th.co.geniustree.nhso.drugcatalog.repo.FundRepo;

/**
 *
 * @author Thanthathon
 */
@FacesConverter("fundConverter")
public class FundConverter implements Converter, Serializable {

    private final FundRepo fundRepo;

    public FundConverter() {
        fundRepo = SpringUtils.getBean(FundRepo.class);
    }

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        System.out.println("Value -> " + value);
        Fund fund = fundRepo.findOne(value);
        System.out.println("fund.getId() -> " + fund.getId());
        return fund;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        return (value == null) ? null : value.toString();
    }

}
