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
    private String edStatus;
    private String icd10;

    public ReimburseGroupItemPK() {

    }

    public ReimburseGroupItemPK(Drug drug, Fund fund, String status, String icd10) {
        this.drug = drug.getTmtId();
        this.fund = fund.getFundCode();
        this.icd10 = icd10;
        this.edStatus = status;
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

    public String getEdStatus() {
        return edStatus;
    }

    public void setEdStatus(String edStatus) {
        this.edStatus = edStatus;
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
        hash = 89 * hash + Objects.hashCode(this.drug);
        hash = 89 * hash + Objects.hashCode(this.fund);
        hash = 89 * hash + Objects.hashCode(this.edStatus);
        hash = 89 * hash + Objects.hashCode(this.icd10);
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
        if (!Objects.equals(this.edStatus, other.edStatus)) {
            return false;
        }
        if (!Objects.equals(this.icd10, other.icd10)) {
            return false;
        }
        return true;
    }

}
