/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.model;

import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author moth
 */
public class HospitalDrugPK implements Serializable {

    private String hospDrugCode;
    private String hcode;

    public HospitalDrugPK() {
    }

    public HospitalDrugPK(String hospDrugCode, String hcode) {
        this.hospDrugCode = hospDrugCode;
        this.hcode = hcode;
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

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 23 * hash + Objects.hashCode(this.hospDrugCode);
        hash = 23 * hash + Objects.hashCode(this.hcode);
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
        final HospitalDrugPK other = (HospitalDrugPK) obj;
        if (!Objects.equals(this.hospDrugCode, other.hospDrugCode)) {
            return false;
        }
        if (!Objects.equals(this.hcode, other.hcode)) {
            return false;
        }
        return true;
    }

}
