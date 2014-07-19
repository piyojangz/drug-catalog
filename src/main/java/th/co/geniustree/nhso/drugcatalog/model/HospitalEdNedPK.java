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
    private Date dateIn;
    private String classifier = SUPPORT_CASSIFIER;//support only UC in current version.
    private String hospDrugCode;

    public HospitalEdNedPK() {
    }

    public HospitalEdNedPK(Date dateIn, String hospDrugCode) {
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

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + Objects.hashCode(this.dateIn);
        hash = 71 * hash + Objects.hashCode(this.classifier);
        hash = 71 * hash + Objects.hashCode(this.hospDrugCode);
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
