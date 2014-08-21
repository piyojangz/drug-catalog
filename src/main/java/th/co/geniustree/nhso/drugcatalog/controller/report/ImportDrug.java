/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.controller.report;

import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import th.co.geniustree.nhso.basicmodel.readonly.Hospital;
import th.co.geniustree.nhso.basicmodel.readonly.Province;
import th.co.geniustree.nhso.basicmodel.readonly.Zone;
import th.co.geniustree.nhso.drugcatalog.repo.UploadHospitalDrugRepo;
import th.co.geniustree.nhso.drugcatalog.service.NhsoZoneService;

/**
 *
 * @author moth
 */
@Component
@Scope("view")
public class ImportDrug extends ReportConditionBase {

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
        model = uploadHospitalDrugRepo.sumUploadItemGroupByHcode(getSelectZone(), getSelectProvince(),getSelectHospital());
    }
}
