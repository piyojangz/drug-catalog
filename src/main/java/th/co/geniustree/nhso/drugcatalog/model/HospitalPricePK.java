/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 *
 * @author moth
 */
public class HospitalPricePK implements Serializable {

    private String hcode;
    private Date dateEffectInclusive;
    private String hospDrugCode;
    private String tmtId;

    public HospitalPricePK() {
    }

    public HospitalPricePK(String hcode, String hospDrugCode, Date dateEffectInclusive,String tmtId) {
        this.hcode = hcode;
        this.dateEffectInclusive = dateEffectInclusive;
        this.hospDrugCode = hospDrugCode;
        this.tmtId = tmtId;
    }

    public String getHcode() {
        return hcode;
    }

    public void setHcode(String hcode) {
        this.hcode = hcode;
    }

    public Date getDateEffectInclusive() {
        return dateEffectInclusive;
    }

    public void setDateEffectInclusive(Date dateEffectInclusive) {
        this.dateEffectInclusive = dateEffectInclusive;
    }

    public String getHospDrugCode() {
        return hospDrugCode;
    }

    public void setHospDrugCode(String hospDrugCode) {
        this.hospDrugCode = hospDrugCode;
    }

    public String getTmtId() {
        return tmtId;
    }

    public void setTmtId(String tmtId) {
        this.tmtId = tmtId;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 53 * hash + Objects.hashCode(this.hcode);
        hash = 53 * hash + Objects.hashCode(this.dateEffectInclusive);
        hash = 53 * hash + Objects.hashCode(this.hospDrugCode);
        hash = 53 * hash + Objects.hashCode(this.tmtId);
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
        final HospitalPricePK other = (HospitalPricePK) obj;
        if (!Objects.equals(this.hcode, other.hcode)) {
            return false;
        }
        if (!Objects.equals(this.dateEffectInclusive, other.dateEffectInclusive)) {
            return false;
        }
        if (!Objects.equals(this.hospDrugCode, other.hospDrugCode)) {
            return false;
        }
        if (!Objects.equals(this.tmtId, other.tmtId)) {
            return false;
        }
        return true;
    }


}
