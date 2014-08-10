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
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

/**
 *
 * @author moth
 */
@Entity
@Table(name = "TMT_DRUGGROUPITEM")
@IdClass(TMTDrugGroupItemPK.class)
public class TMTDrugGroupItem implements Serializable {

    public TMTDrugGroupItem() {
    }

    public TMTDrugGroupItem(TMTDrug tmtDrug, DrugGroup drugGroup, Date datein) {
        this.tmtDrug = tmtDrug;
        this.drugGroup = drugGroup;
        this.datein = datein;
    }

    @Id
    @ManyToOne
    @JoinColumn(name = "TMTID", nullable = false)
    private TMTDrug tmtDrug;

    @Id
    @ManyToOne
    @JoinColumn(name = "DRUGGROUP_NAME", nullable = false)
    private DrugGroup drugGroup;

    @Id
    @Temporal(TemporalType.DATE)
    private Date datein;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;

    @PrePersist
    public void prePersist() {
        createDate = new Date();
    }

    public TMTDrug getTmtDrug() {
        return tmtDrug;
    }

    public void setTmtDrug(TMTDrug tmtDrug) {
        this.tmtDrug = tmtDrug;
    }

    public DrugGroup getDrugGroup() {
        return drugGroup;
    }

    public void setDrugGroup(DrugGroup drugGroup) {
        this.drugGroup = drugGroup;
    }

    public Date getDatein() {
        return datein;
    }

    public void setDatein(Date datein) {
        this.datein = datein;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.tmtDrug);
        hash = 97 * hash + Objects.hashCode(this.drugGroup);
        hash = 97 * hash + Objects.hashCode(this.datein);
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
        final TMTDrugGroupItem other = (TMTDrugGroupItem) obj;
        if (!Objects.equals(this.tmtDrug, other.tmtDrug)) {
            return false;
        }
        if (!Objects.equals(this.drugGroup, other.drugGroup)) {
            return false;
        }
        if (!Objects.equals(this.datein, other.datein)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        if (drugGroup != null) {
            return drugGroup.toString();
        } else {
            return "";
        }
    }

}
