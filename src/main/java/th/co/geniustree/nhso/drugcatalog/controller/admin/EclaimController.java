/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.controller.admin;

import th.co.geniustree.nhso.drugcatalog.model.HospitalDrugType;
import th.co.geniustree.nhso.drugcatalog.dao.EclaimDAO;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
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

    private HospitalDrugType drug;
    private String hcode;
    private String hospDrugCode;
    private String tmtid;
    private Date dateEffective;

    private List<String> druggroups;

    @PostConstruct
    public void postConstruct() {
        druggroups = new ArrayList<>();
        druggroups.add("J2");
        druggroups.add("CANCER");
    }

    @Autowired
    private EclaimDAO eclaimDAO;

    public void search() {
        hospDrugCode = "1530046";
        hcode = "10835";
        tmtid = "556824";
        dateEffective = new Date();
        if (dateEffective == null) {
            dateEffective = new Date();
        }
        LOG.info("Input HCODE : " + hcode);
        LOG.info("Input hospDrugType : " + hospDrugCode);
        LOG.info("Input TMT id : " + tmtid);
        LOG.info("Input Service date : " + dateEffective);

        this.drug = eclaimDAO.loadDrugInfo(hospDrugCode, hcode, tmtid, dateEffective);
        LOG.info("TMT id -> " + this.drug.getTmtid());
        LOG.info("TMT type -> " + this.drug.getTmt_type());
        LOG.info("FSN -> " + this.drug.getFsn());
        LOG.info("MANUFACTURER -> " + this.drug.getManufacturer());
        LOG.info("HOSP GENERIC NAME -> " + this.drug.getHosp_genericName());
        LOG.info("HOSP TRADE NAME -> " + this.drug.getHosp_tradeName());
        LOG.info("UNIT PRICE -> " + this.drug.getUnit_price());
        LOG.info("SPECREP -> " + this.drug.getSPECPREP());
        LOG.info("ED / NED -> " + this.drug.getIs_ed());
        LOG.info("NDC24 -> " + this.drug.getNdc24());
        LOG.info("DELETED -> " + this.drug.getDeleted());
        LOG.info("APPROVED -> " + this.drug.getApproved());
        LOG.info("PRODCTCAT -> " + this.drug.getProductcat());
        LOG.info("TMT DOSAGEFORM -> " + this.drug.getTMT_DOSAGEFORM());
        LOG.info("DOSAGEFORM GROUP-> " + this.drug.getDOSAGEFORM_GROUP());
        LOG.info("REIMB UNIT PRICE -> " + this.drug.getREIMB_UNIT_PRICE());
        LOG.info("Content -> " + this.drug.getContent());
        LOG.info("ISED_STATUS -> " + this.drug.getISED_STATUS());
        try {
            ResultSet rs = drug.getDrggroup().getResultSet();
            while (rs.next()) {
                String s = rs.getString(1);
                LOG.info("Druggroup -> " + s);
            }
        } catch (SQLException | NullPointerException ex) {
            LOG.error(null, ex);
        }
    }

    public List<String> getDruggroups() {
        druggroups.clear();
        druggroups.add("J2");
        druggroups.add("CANCER");
        druggroups.add("J2");
        druggroups.add("CANCER");
        druggroups.add("J2");
        druggroups.add("CANCER");
        druggroups.add("J2");
        druggroups.add("CANCER");
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
