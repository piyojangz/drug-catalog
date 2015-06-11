/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.controller.admin;

import java.io.Serializable;
import java.util.Date;
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

    private HospitalDrugWithTMT tmtDrug;
    private String hcode;
    private String hospDrugCode;
    private String tmtid;
    private Date dateEffective;

    @PostConstruct
    public void postConstruct() {
        tmtDrug = new HospitalDrugWithTMT();
    }
    
    @Autowired
    private EclaimDAO eclaimDAO;

    public void search() {
        tmtDrug = eclaimDAO.loadDrugInfo("10741", "6666666","66565", new Date());
    }

    public HospitalDrugWithTMT getTmtDrug() {
        return tmtDrug;
    }

    public void setTmtDrug(HospitalDrugWithTMT tmtDrug) {
        this.tmtDrug = tmtDrug;
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
