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
 * @author pramoth
 */
public class HospitalEdNedPK implements Serializable {

    public static final String SUPPORT_CASSIFIER = "UC";
    private String hcode;
    private Date dateIn;
    private String classifier = SUPPORT_CASSIFIER;//support only UC in current version.
    private String hospDrugCode;

    public HospitalEdNedPK() {
    }

    public HospitalEdNedPK(String hcode, String hospDrugCode, Date dateIn) {
        this.hcode = hcode;
        this.dateIn = dateIn;
        this.classifier = SUPPORT_CASSIFIER;
        this.hospDrugCode = hospDrugCode;
    }

    public Date getDateIn() {
        return dateIn;
    }

    public String getClassifier() {
        return classifier;
    }

    public String getHospDrugCode() {
        return hospDrugCode;
    }

    public String getHcode() {
        return hcode;
    }

    public void setHcode(String hcode) {
        this.hcode = hcode;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 79 * hash + Objects.hashCode(this.hcode);
        hash = 79 * hash + Objects.hashCode(this.dateIn);
        hash = 79 * hash + Objects.hashCode(this.classifier);
        hash = 79 * hash + Objects.hashCode(this.hospDrugCode);
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
        final HospitalEdNedPK other = (HospitalEdNedPK) obj;
        if (!Objects.equals(this.hcode, other.hcode)) {
            return false;
        }
        if (!Objects.equals(this.dateIn, other.dateIn)) {
            return false;
        }
        if (!Objects.equals(this.classifier, other.classifier)) {
            return false;
        }
        if (!Objects.equals(this.hospDrugCode, other.hospDrugCode)) {
            return false;
        }
        return true;
    }


}
