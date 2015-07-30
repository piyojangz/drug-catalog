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
import th.co.geniustree.nhso.basicmodel.readonly.ICD10;

/**
 *
 * @author Thanthathon
 */
@Embeddable
public class ReimburseGroupItemPK implements Serializable {

    @Column(name = "TMTID")
    private String drug;

    @Column(name = "FUND_CODE")
    private String fund;

    @Column(name = "ICD10_CODE")
    private String icd10;
    
    @Column(name = "REIMBURSE_GROUP_ID")
    private String reimburseGroupId;
    
    @Temporal(TemporalType.DATE)
    @Column(name = "BUDGET_YEAR")
    private Date budgetYear;

    public ReimburseGroupItemPK() {

    }

    public ReimburseGroupItemPK(String tmtid, String fundCode, String icd10Id,String reimburseGroupId ,Date budgetYear) {
        this.drug = tmtid;
        this.fund = fundCode;
        this.icd10 = icd10Id;
        this.budgetYear = budgetYear;
        this.reimburseGroupId = reimburseGroupId;
    }

    public ReimburseGroupItemPK(TMTDrug drug, Fund fund, ICD10 icd10, ReimburseGroup reimburseGroup ,Date budgetYear) {
        this.drug = drug.getTmtId();
        this.fund = fund.getCode();
        this.icd10 = icd10.getCode();
        this.reimburseGroupId = reimburseGroup.getId();
        this.budgetYear = budgetYear;
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

    public Date getBudgetYear() {
        return budgetYear;
    }

    public void setBudgetYear(Date budgetYear) {
        this.budgetYear = budgetYear;
    }

    public String getReimburseGroupId() {
        return reimburseGroupId;
    }

    public void setReimburseGroupId(String reimburseGroupId) {
        this.reimburseGroupId = reimburseGroupId;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 11 * hash + Objects.hashCode(this.drug);
        hash = 11 * hash + Objects.hashCode(this.fund);
        hash = 11 * hash + Objects.hashCode(this.icd10);
        hash = 11 * hash + Objects.hashCode(this.reimburseGroupId);
        hash = 11 * hash + Objects.hashCode(this.budgetYear);
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
        if (!Objects.equals(this.reimburseGroupId, other.reimburseGroupId)) {
            return false;
        }
        if (!Objects.equals(this.budgetYear, other.budgetYear)) {
            return false;
        }
        return true;
    }

}
