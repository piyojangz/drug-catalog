/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.controller.report;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import th.co.geniustree.nhso.basicmodel.readonly.Hospital;
import th.co.geniustree.nhso.drugcatalog.repo.UploadHospitalDrugRepo;
import th.co.geniustree.nhso.drugcatalog.service.DeleteUploadDrugService;

/**
 *
 * @author moth
 */
@Component
@Scope("view")
public class ImportDrug extends ReportConditionBase {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(ImportDrug.class);
    @Autowired
    private UploadHospitalDrugRepo uploadHospitalDrugRepo;
    private Object[] model;

    public Object[] getModel() {
        return model;
    }

    public void setModel(Object[] model) {
        this.model = model;
    }

    public void view() {
        model = uploadHospitalDrugRepo.sumUploadItemGroupByHcode(getSelectZone(), getSelectProvince(), getSelectHospital());
    }

    public void showSearchHospitalDialog() {
        Map<String, Object> options = new HashMap<String, Object>();
        options.put("modal", true);
        options.put("draggable", true);
        options.put("resizable", true);
        options.put("contentHeight", 500);
        options.put("contentWidth", 800);
        RequestContext.getCurrentInstance().openDialog("/private/common/searchHospitalDialog", options, Collections.EMPTY_MAP);
    }
    public void searchHospitalDialogReturn(SelectEvent event){
        Hospital hospital = (Hospital) event.getObject();
        log.info("selected hospital from search dialog is => {}",hospital);
        setSelectZone(hospital.getProvince().getNhsoZone().getNhsoZone());
        setSelectProvince(hospital.getProvince().getId());
        setSelectHospital(hospital.getHcode());
    }
}
