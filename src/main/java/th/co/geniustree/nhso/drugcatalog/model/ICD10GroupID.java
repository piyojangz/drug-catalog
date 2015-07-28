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
 * @author Thanthathon
 */
public class ICD10GroupID implements Serializable{
    private String icd10;
    private String reimburseGroup;

    public String getIcd10() {
        return icd10;
    }

    public void setIcd10(String icd10) {
        this.icd10 = icd10;
    }

    public String getReimburseGroup() {
        return reimburseGroup;
    }

    public void setReimburseGroup(String reimburseGroup) {
        this.reimburseGroup = reimburseGroup;
    }

    public ICD10GroupID() {
    }

    public ICD10GroupID(String icd10, String reimburseGroup) {
        this.icd10 = icd10;
        this.reimburseGroup = reimburseGroup;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 71 * hash + Objects.hashCode(this.icd10);
        hash = 71 * hash + Objects.hashCode(this.reimburseGroup);
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
        final ICD10GroupID other = (ICD10GroupID) obj;
        if (!Objects.equals(this.icd10, other.icd10)) {
            return false;
        }
        if (!Objects.equals(this.reimburseGroup, other.reimburseGroup)) {
            return false;
        }
        return true;
    }
    
}
