/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.controller.admin;

import th.co.geniustree.nhso.drugcatalog.model.HospitalDrugType;
import th.co.geniustree.nhso.drugcatalog.dao.EclaimDAO;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import th.co.geniustree.nhso.drugcatalog.model.UploadHospitalDrugItem;
import th.co.geniustree.nhso.drugcatalog.model.UploadHospitalDrugItemTemp;
import th.co.geniustree.nhso.drugcatalog.repo.UploadHospitalDrugItemRepo;

/**
 *
 * @author Thanthathon
 */
@Component
@Scope("view")
public class EclaimController implements Serializable {

    private static final Logger LOG = LoggerFactory.getLogger(EclaimController.class);
    
    @Autowired
    private UploadHospitalDrugItemRepo uploadHospitalDrugItemRepo;
    
    @Autowired
    private EclaimDAO eclaimDAO;
    
    private HospitalDrugType drug;
    private String hcode;
    private String hospDrugCode;
    private String tmtid;
    private Date dateEffective;
    private UploadHospitalDrugItemTemp uploadItem;
    
    List<UploadHospitalDrugItem> uploadHospitalDrugItems;

    private List<String> druggroups;

    @PostConstruct
    public void postConstruct() {
    }

    

    public void search() {
//        hospDrugCode = "5010000532";
//        hcode = "10662";
//        tmtid = "147692";
//        dateEffective = new GregorianCalendar(2015, 2, 11).getTime();
        this.drug = eclaimDAO.loadDrugInfo(hospDrugCode, hcode, tmtid, dateEffective);
        this.druggroups = eclaimDAO.getDrugGroupsFrom(this.drug);
        uploadHospitalDrugItems = uploadHospitalDrugItemRepo.findByHospDrugCodeAndUploadDrugHcodeAndDateEffectiveAfter(hospDrugCode, hcode, dateEffective);
        LOG.info("output -> {} " , uploadHospitalDrugItems.size());
        for(UploadHospitalDrugItem u : uploadHospitalDrugItems){
            LOG.info("tmt  -> {}" , u.getTmtId());
            LOG.info("flag -> {}" , u.getUpdateFlag());
            LOG.info("dEff -> {}" , u.getDateEffective());
        }
    }
    
    public void reset() {
        hospDrugCode = "";
        hcode = "";
        tmtid = "";
        drug = null;
        druggroups.clear();
        dateEffective = null;
    }

    public List<String> getDruggroups() {
        return druggroups;
    }

    public void setDruggroups(List<String> druggroups) {
        this.druggroups = druggroups;
    }

    public HospitalDrugType getTmtDrug() {
        return drug;
    }

    public void setTmtDrug(HospitalDrugType tmtDrug) {
        this.drug = tmtDrug;
    }

    public String getHcode() {
        return hcode;
    }

    public void setHcode(String hcode) {
        this.hcode = hcode;
    }

    public String getHospDrugCode() {
        return hospDrugCode;
    }

    public void setHospDrugCode(String hospDrugCode) {
        this.hospDrugCode = hospDrugCode;
    }

    public String getTmtid() {
        return tmtid;
    }

    public void setTmtid(String tmtid) {
        this.tmtid = tmtid;
    }

    public Date getDateEffective() {
        return dateEffective;
    }

    public void setDateEffective(Date dateEffective) {
        this.dateEffective = dateEffective;
    }

}
