/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author moth
 */
@Embeddable
public class TMTDrugTxPK implements Serializable {

    @Column(name = "HCODE", nullable = false, length = 5)
    private String hcode;

    @Column(name = "HOSPDRUGCODE", nullable = false, length = 30)
    private String hospDrugCode;

    @Temporal(TemporalType.DATE)
    @Column(name = "DATE_EFFECTIVE", nullable = false)
    private Date dateEffective;
    
    @Column(name = "TMTID", nullable = false,length = 6)
    private String tmtId;

    public TMTDrugTxPK() {
    }

    public TMTDrugTxPK(String hospDrugCode, String hcode, Date dateEffective,String tmtId) {
        this.hospDrugCode = hospDrugCode;
        this.hcode = hcode;
        this.dateEffective = dateEffective;
        this.tmtId = tmtId;
    }
    public TMTDrugTxPK(HospitalDrug hospitalDrug) {
        this.hospDrugCode = hospitalDrug.getHospDrugCode();
        this.hcode = hospitalDrug.getHcode();
        this.dateEffective = hospitalDrug.getDateEffective();
        this.tmtId = hospitalDrug.getTmtId();
    }

    public String getHospDrugCode() {
        return hospDrugCode;
    }

    public void setHospDrugCode(String hospDrugCode) {
        this.hospDrugCode = hospDrugCode;
    }

    public String getHcode() {
        return hcode;
    }

    public void setHcode(String hcode) {
        this.hcode = hcode;
    }

    public Date getDateEffective() {
        return dateEffective;
    }

    public void setDateEffective(Date dateEffective) {
        this.dateEffective = dateEffective;
    }

    public String getTmtId() {
        return tmtId;
    }

    public void setTmtId(String tmtId) {
        this.tmtId = tmtId;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 47 * hash + Objects.hashCode(this.hcode);
        hash = 47 * hash + Objects.hashCode(this.hospDrugCode);
        hash = 47 * hash + Objects.hashCode(this.dateEffective);
        hash = 47 * hash + Objects.hashCode(this.tmtId);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final TMTDrugTxPK other = (TMTDrugTxPK) obj;
        if (!Objects.equals(this.hcode, other.hcode)) {
            return false;
        }
        if (!Objects.equals(this.hospDrugCode, other.hospDrugCode)) {
            return false;
        }
        if (!Objects.equals(this.dateEffective, other.dateEffective)) {
            return false;
        }
        if (!Objects.equals(this.tmtId, other.tmtId)) {
            return false;
        }
        return true;
    }

}
