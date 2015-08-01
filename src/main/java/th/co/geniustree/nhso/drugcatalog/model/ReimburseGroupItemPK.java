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
    private String tmtid;

    @Column(name = "FUND_CODE")
    private String fundCode;

    @Column(name = "ICD10_CODE")
    private String icd10Code;
    
    @Column(name = "REIMBURSE_GROUP_ID")
    private String reimburseGroupId;
    
    @Column(name = "BUDGET_YEAR")
    private Integer budgetYear;

    public ReimburseGroupItemPK() {

    }

    public ReimburseGroupItemPK(String tmtid, String fundCode, String icd10Id,String reimburseGroupId ,Integer budgetYear) {
        this.tmtid = tmtid;
        this.fundCode = fundCode;
        this.icd10Code = icd10Id;
        this.budgetYear = budgetYear;
        this.reimburseGroupId = reimburseGroupId;
    }

    public ReimburseGroupItemPK(TMTDrug drug, Fund fund, ICD10 icd10, ReimburseGroup reimburseGroup ,Integer budgetYear) {
        this.tmtid = drug.getTmtId();
        this.fundCode = fund.getCode();
        this.icd10Code = icd10.getCode();
        this.reimburseGroupId = reimburseGroup.getId();
        this.budgetYear = budgetYear;
    }

    public String getTmtid() {
        return tmtid;
    }

    public void setTmtid(String tmtid) {
        this.tmtid = tmtid;
    }

    public String getFundCode() {
        return fundCode;
    }

    public void setFundCode(String fundCode) {
        this.fundCode = fundCode;
    }

    public String getIcd10Code() {
        return icd10Code;
    }

    public void setIcd10Code(String icd10Code) {
        this.icd10Code = icd10Code;
    }

    public Integer getBudgetYear() {
        return budgetYear;
    }

    public void setBudgetYear(Integer budgetYear) {
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
        hash = 11 * hash + Objects.hashCode(this.tmtid);
        hash = 11 * hash + Objects.hashCode(this.fundCode);
        hash = 11 * hash + Objects.hashCode(this.icd10Code);
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
        if (!Objects.equals(this.tmtid, other.tmtid)) {
            return false;
        }
        if (!Objects.equals(this.fundCode, other.fundCode)) {
            return false;
        }
        if (!Objects.equals(this.icd10Code, other.icd10Code)) {
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
