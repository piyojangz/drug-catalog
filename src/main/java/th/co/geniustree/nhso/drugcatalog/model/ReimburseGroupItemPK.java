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
public class ReimburseGroupItemPK implements Serializable {

    private String drug;
    private String fund;
    private String icd10;
    private String edStatus;

    public ReimburseGroupItemPK() {
        
    }

    public ReimburseGroupItemPK(String drug, String fund, String icd10, String edStatus) {
        this.drug = drug;
        this.fund = fund;
        this.icd10 = icd10;
        this.edStatus = edStatus;
    }

    public String getDrug() {
        return drug;
    }

    public void setDrug(String drug) {
        this.drug = drug;
    }

    public String getFund() {
        return fund;
    }

    public void setFund(String fund) {
        this.fund = fund;
    }

    public String getIcd10() {
        return icd10;
    }

    public void setIcd10(String icd10) {
        this.icd10 = icd10;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 41 * hash + Objects.hashCode(this.drug);
        hash = 41 * hash + Objects.hashCode(this.fund);
        hash = 41 * hash + Objects.hashCode(this.icd10);
        hash = 41 * hash + Objects.hashCode(this.edStatus);
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
        final ReimburseGroupItemPK other = (ReimburseGroupItemPK) obj;
        if (!Objects.equals(this.drug, other.drug)) {
            return false;
        }
        if (!Objects.equals(this.fund, other.fund)) {
            return false;
        }
        if (!Objects.equals(this.icd10, other.icd10)) {
            return false;
        }
        if (!Objects.equals(this.edStatus, other.edStatus)) {
            return false;
        }
        return true;
    }

    

}
