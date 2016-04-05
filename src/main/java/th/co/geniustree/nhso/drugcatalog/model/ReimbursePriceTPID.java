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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author thanthathon.b
 */
@Embeddable
public class ReimbursePriceTPID implements Serializable {

    @Column(name = "TMTID",length = 6)
    private String tmtId;

    @Column(name = "HOSPDRUGCODE",length = 30)
    private String hospDrugCode;

    @Column(name = "HCODE",length = 5)
    private String hcode;

    @Temporal(TemporalType.DATE)
    @Column(name = "EFFECTIVE_DATE")
    private Date dateEffective;

    public String getTmtId() {
        return tmtId;
    }

    public void setTmtId(String tmtId) {
        this.tmtId = tmtId;
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

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + Objects.hashCode(this.tmtId);
        hash = 97 * hash + Objects.hashCode(this.hospDrugCode);
        hash = 97 * hash + Objects.hashCode(this.hcode);
        hash = 97 * hash + Objects.hashCode(this.dateEffective);
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
        final ReimbursePriceTPID other = (ReimbursePriceTPID) obj;
        if (!Objects.equals(this.tmtId, other.tmtId)) {
            return false;
        }
        if (!Objects.equals(this.hospDrugCode, other.hospDrugCode)) {
            return false;
        }
        if (!Objects.equals(this.hcode, other.hcode)) {
            return false;
        }
        if (!Objects.equals(this.dateEffective, other.dateEffective)) {
            return false;
        }
        return true;
    }
    
}
