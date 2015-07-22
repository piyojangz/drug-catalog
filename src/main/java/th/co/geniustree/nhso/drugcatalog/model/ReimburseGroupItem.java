/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.model;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;

/**
 *
 * @author Thanthathon
 */
@Entity
@Table(name = "TMT_REIMBURSE_GROUP_ITEM")
@IdClass(ReimburseGroupItemPK.class)
public class ReimburseGroupItem implements Serializable {

    @Id
    @Column(name = "ED_STATUS")
    private String edStatus;

    @Id
    @ManyToOne()
    @JoinColumn(name = "TMTID", referencedColumnName = "TMTID", nullable = false)
    private Drug drug;

    @Id
    @ManyToOne
    @JoinColumn(name = "FUND_CODE", referencedColumnName = "FUND_CODE", nullable = false)
    private Fund fund;

    @Id
    @ManyToOne
    @JoinColumn(name = "ICD10_ID", referencedColumnName = "ICD10_ID", nullable = false)
    private ICD10 icd10;

    @ManyToOne
    @JoinColumn(name = "REIMBURSE_GROUP_ID", referencedColumnName = "REIMBURSE_GROUP_ID")
    private ReimburseGroup reimburseGroup;

    public ReimburseGroupItem() {
    }

    public ReimburseGroupItem(String edStatus, Drug drug, Fund fund, ICD10 icd10, ReimburseGroup reimburseGroup) {
        this.edStatus = edStatus;
        this.drug = drug;
        this.fund = fund;
        this.icd10 = icd10;
        this.reimburseGroup = reimburseGroup;
    }

    @Version
    private Integer version;

    public String getEdStatus() {
        return edStatus;
    }

    public void setEdStatus(String edStatus) {
        this.edStatus = edStatus;
    }

    public ICD10 getIcd10() {
        return icd10;
    }

    public void setIcd10(ICD10 icd10) {
        this.icd10 = icd10;
    }

    public Drug getDrug() {
        return drug;
    }

    public void setDrug(Drug drug) {
        this.drug = drug;
    }

    public Fund getFund() {
        return fund;
    }

    public void setFund(Fund fund) {
        this.fund = fund;
    }

    public ReimburseGroup getReimburseGroup() {
        return reimburseGroup;
    }

    public void setReimburseGroup(ReimburseGroup reimburseGroup) {
        this.reimburseGroup = reimburseGroup;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 83 * hash + Objects.hashCode(this.edStatus);
        hash = 83 * hash + Objects.hashCode(this.drug);
        hash = 83 * hash + Objects.hashCode(this.fund);
        hash = 83 * hash + Objects.hashCode(this.icd10);
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
        final ReimburseGroupItem other = (ReimburseGroupItem) obj;
        if (!Objects.equals(this.edStatus, other.edStatus)) {
            return false;
        }
        if (!Objects.equals(this.drug, other.drug)) {
            return false;
        }
        if (!Objects.equals(this.fund, other.fund)) {
            return false;
        }
        if (!Objects.equals(this.icd10, other.icd10)) {
            return false;
        }
        return true;
    }

}
