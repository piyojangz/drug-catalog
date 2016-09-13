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
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import th.co.geniustree.nhso.basicmodel.readonly.Province;
import th.co.geniustree.nhso.basicmodel.repository.ProvinceRepository;
import th.co.geniustree.nhso.drugcatalog.controller.utils.SpringUtils;

/**
 *
 * @author Thanthathon
 */
@Component
@Scope("view")
@FacesConverter("provinceConverter")
public class ProvinceConverter implements Converter ,Serializable{
    
    private final ProvinceRepository provinceRepository;

    public ProvinceConverter() {
        provinceRepository = SpringUtils.getBean(ProvinceRepository.class);
    }

    
    
    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        return (Province) provinceRepository.findOne(value);
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        return (value == null) ? null : value.toString();
    }
    
}
