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
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Thanthathon
 */
@Entity
@Table(name = "TMT_REIMBURSE_GROUP_ITEM")
public class ReimburseGroupItem implements Serializable {

    @Id
    @TableGenerator(name = "TMT_REIMBURSE_GROUP_ITEM_GEN",
            table = "TMT_SEQUENCE",
            pkColumnName = "name",
            valueColumnName = "value",
            pkColumnValue = "TMT_REIMBURSE_GROUP_ITEM")
    @GeneratedValue(generator = "TMT_REIMBURSE_GROUP_ITEM_GEN", strategy = GenerationType.TABLE)
    private Integer id;

    @Column(name = "ED_STATUS")
    private String statusEd;

    @ManyToOne
    @JoinColumn(name = "TMTID", referencedColumnName = "TMTID", insertable = false, updatable = false, nullable = false)
    private Drug tmtDrug;

    @ManyToOne
    @JoinColumn(name = "FUND_CODE", referencedColumnName = "FUND_CODE", insertable = false, updatable = false, nullable = false)
    private Fund fund;

    @ManyToOne
    @JoinColumns({
        @JoinColumn(name = "ICD10_CODE", referencedColumnName = "ICD10_CODE", insertable = false, updatable = false),
        @JoinColumn(name = "REIMBURSE_GROUP_ID", referencedColumnName = "REIMBURSE_GROUP_ID", insertable = false, updatable = false)
    })
    private ICD10Group icd10Group;

    @Temporal(TemporalType.DATE)
    private Date budgetYear;

    public ReimburseGroupItem() {
    }

    public ReimburseGroupItem(String statusEd, Drug drug, Fund fund, ICD10Group icd10Group, Date budgetYear) {
        this.statusEd = statusEd;
        this.tmtDrug = drug;
        this.fund = fund;
        this.icd10Group = icd10Group;
        this.budgetYear = budgetYear;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStatusEd() {
        return statusEd;
    }

    public void setStatusEd(String statusEd) {
        this.statusEd = statusEd;
    }

    public Drug getTmtDrug() {
        return tmtDrug;
    }

    public void setTmtDrug(Drug tmtDrug) {
        this.tmtDrug = tmtDrug;
    }

    public Fund getFund() {
        return fund;
    }

    public void setFund(Fund fund) {
        this.fund = fund;
    }

    public ICD10Group getIcd10Group() {
        return icd10Group;
    }

    public void setIcd10Group(ICD10Group icd10Group) {
        this.icd10Group = icd10Group;
    }

    public Date getBudgetYear() {
        return budgetYear;
    }

    public void setBudgetYear(Date budgetYear) {
        this.budgetYear = budgetYear;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 79 * hash + Objects.hashCode(this.id);
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
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

}
