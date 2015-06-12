/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.controller.admin;

import th.co.geniustree.nhso.drugcatalog.model.HospitalDrugWithTMT;
import th.co.geniustree.nhso.drugcatalog.dao.EclaimDAO;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 *
 * @author Thanthathon
 */
@Component
@Scope("view")
public class EclaimController implements Serializable {

    private static final Logger LOG = LoggerFactory.getLogger(EclaimController.class);

    private List<HospitalDrugWithTMT> tmtDrugs;
    private String hcode;
    private String hospDrugCode;
    private String tmtid;
    private Date dateEffective;

    @PostConstruct
    public void postConstruct() {
        tmtDrugs = new ArrayList<>();
    }
    
    @Autowired
    private EclaimDAO eclaimDAO;

    public void search() {
        tmtDrugs = eclaimDAO.loadDrugInfo("10741", "6666666","66565", new Date());
    }

    public List<HospitalDrugWithTMT> getTmtDrug() {
        return tmtDrugs;
    }

    public void setTmtDrug(List<HospitalDrugWithTMT> tmtDrug) {
        this.tmtDrugs = tmtDrug;
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
