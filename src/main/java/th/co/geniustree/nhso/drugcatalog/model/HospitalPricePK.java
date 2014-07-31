/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.model;

import java.io.Serializable;
import java.math.BigDecimal;
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

    public HospitalPricePK() {
    }

    public HospitalPricePK(String hcode, String hospDrugCode, Date dateEffectInclusive) {
        this.hcode = hcode;
        this.dateEffectInclusive = dateEffectInclusive;
        this.hospDrugCode = hospDrugCode;
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

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + Objects.hashCode(this.hcode);
        hash = 17 * hash + Objects.hashCode(this.dateEffectInclusive);
        hash = 17 * hash + Objects.hashCode(this.hospDrugCode);
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
        return true;
    }

}
