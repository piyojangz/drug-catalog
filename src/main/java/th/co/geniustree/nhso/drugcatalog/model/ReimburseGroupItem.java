/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.model;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;
import javax.persistence.Version;

/**
 *
 * @author Thanthathon
 */
@Entity
@Table(name = "TMT_REIMBURSE_GROUP_ITEM")
public class ReimburseGroupItem implements Serializable {

    @EmbeddedId
    private ReimburseGroupItemPK pk;

    @ManyToOne
    @JoinColumn(name = "TMTID", referencedColumnName = "TMTID", insertable = false, updatable = false)
    private Drug drug;

    @ManyToOne
    @JoinColumn(name = "FUND_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    private Fund fund;

    @ManyToOne
    @JoinColumn(name = "ICD10_ID", referencedColumnName = "ICD10_ID", insertable = false, updatable = false)
    private ICD10 icd10;

    @MapsId("edNed")
    @ManyToOne
    @JoinColumns({
        @JoinColumn(name = "EDNED_TMTID", referencedColumnName = "TMTID"),
        @JoinColumn(name = "EDNED_FUNDID", referencedColumnName = "FUND_ID"),
        @JoinColumn(name = "EDNED_DATEIN", referencedColumnName = "DATE_IN")
    })
    private EdNed edNed;

    @ManyToOne
    @JoinColumn(name = "REIMBURSE_GROUP_ID", referencedColumnName = "REIMBURSE_GROUP_ID")
    private ReimburseGroup reimburseGroup;

    @Version
    private Integer version;

    public ReimburseGroupItemPK getPk() {
        return pk;
    }

    public void setPk(ReimburseGroupItemPK pk) {
        this.pk = pk;
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

    public EdNed getEdNed() {
        return edNed;
    }

    public void setEdNed(EdNed edNed) {
        this.edNed = edNed;
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
        hash = 53 * hash + Objects.hashCode(this.pk);
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
