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
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import th.co.geniustree.nhso.basicmodel.readonly.ICD10;

/**
 *
 * @author Thanthathon
 */
@Entity
@Table(name = "TMT_REIMBURSE_GROUP_ITEM")
public class ReimburseGroupItem implements Serializable {

    public enum ED {

        N, E, EX
    }
    @EmbeddedId
    private ReimburseGroupItemPK pk;

    @ManyToOne
    @JoinColumn(name = "TMTID", insertable = false, updatable = false, nullable = false)
    private TMTDrug tmtDrug;

    @ManyToOne
    @JoinColumn(name = "FUND_CODE", insertable = false, updatable = false, nullable = false)
    private Fund fund;

    @ManyToOne
    @JoinColumn(name = "ICD10_CODE", insertable = false, updatable = false, nullable = false)
    private ICD10 icd10;

    @ManyToOne
    @JoinColumn(name = "REIMBURSE_GROUP_ID", insertable = false, updatable = false, nullable = false)
    private ReimburseGroup reimburseGroup;

    @Enumerated(EnumType.STRING)
    @Column(name = "ED_STATUS")
    private ED statusEd;

    public ReimburseGroupItem() {
    }

    public ReimburseGroupItem(TMTDrug tmtDrug, Fund fund, ICD10 icd10, ReimburseGroup reimburseGroup, ED statusEd, Integer budgetYear) {
        this.tmtDrug = tmtDrug;
        this.fund = fund;
        this.icd10 = icd10;
        this.reimburseGroup = reimburseGroup;
        this.statusEd = statusEd;
        this.pk = new ReimburseGroupItemPK(tmtDrug, fund, icd10, reimburseGroup, budgetYear);
    }
    
    public ED getStatusEd() {
        return statusEd;
    }

    public void setStatusEd(ED statusEd) {
        this.statusEd = statusEd;
    }

    public Fund getFund() {
        return fund;
    }

    public void setFund(Fund fund) {
        this.fund = fund;
    }

    public ReimburseGroupItemPK getPk() {
        return pk;
    }

    public void setPk(ReimburseGroupItemPK pk) {
        this.pk = pk;
    }

    public TMTDrug getTmtDrug() {
        return tmtDrug;
    }

    public void setTmtDrug(TMTDrug tmtDrug) {
        this.tmtDrug = tmtDrug;
    }

    public ICD10 getIcd10() {
        return icd10;
    }

    public void setIcd10(ICD10 icd10) {
        this.icd10 = icd10;
    }

    public ReimburseGroup getReimburseGroup() {
        return reimburseGroup;
    }

    public void setReimburseGroup(ReimburseGroup reimburseGroup) {
        this.reimburseGroup = reimburseGroup;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + Objects.hashCode(this.pk);
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
        if (!Objects.equals(this.pk, other.pk)) {
            return false;
        }
        return true;
    }

}
