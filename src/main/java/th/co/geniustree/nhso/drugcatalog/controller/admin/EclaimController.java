/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.controller.admin;

import th.co.geniustree.nhso.drugcatalog.model.HospitalDrugType;
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

    private HospitalDrugType tmtDrugs;
    private String hcode;
    private String hospDrugCode;
    private String tmtid;
    private Date dateEffective;

    @PostConstruct
    public void postConstruct() {
        tmtDrugs = new HospitalDrugType();
    }

    @Autowired
    private EclaimDAO eclaimDAO;

    public void search() {
        tmtDrugs = eclaimDAO.loadDrugInfo("10919", "1480055", null, new Date());
        LOG.info("Drug TradeName --> ",tmtDrugs.getHosp_tradeName());
        
    }

    public HospitalDrugType getTmtDrug() {
        return tmtDrugs;
    }

    public void setTmtDrug(HospitalDrugType tmtDrug) {
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
