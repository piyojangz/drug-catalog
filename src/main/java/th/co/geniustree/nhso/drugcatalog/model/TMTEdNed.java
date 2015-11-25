/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
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

/**
 *
 * @author moth
 */
@Entity
@Table(name = "TMT_TMTEDNED")
@IdClass(TMTEdNedPK.class)
public class TMTEdNed implements Serializable {

    @Id
    @Column(name = "TMTID", nullable = false, length = 6)
    private String tmtId;
    @Id
    @Temporal(TemporalType.DATE)
    @Column(name = "DATEIN", nullable = false)
    private Date dateIn;
    @Id
    @Column(name = "CLASSIFIER", nullable = false, length = 3)
    private String classifier;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATEDATE", nullable = false)
    private Date createDate;
    
    @Column(name = "STATUS_ED", length = 2, nullable = false)
    private String statusEd;
    
    @ManyToOne(optional = false)
    @JoinColumn(name = "TMTID", insertable = false, updatable = false)
    private TMTDrug tmtDrug;
    
    @ManyToOne
    @JoinColumn(name = "CLASSIFIER", insertable = false, updatable = false)
    private Fund fund;

    @PrePersist
    public void prePersist() {
        createDate = new Date();
    }

    public Date getDateIn() {
        return dateIn;
    }

    public void setDateIn(Date dateIn) {
        this.dateIn = dateIn;
    }

    public String getClassifier() {
        return classifier;
    }

    public void setClassifier(String classifier) {
        this.classifier = classifier;
    }

    public String getTmtId() {
        return tmtId;
    }

    public void setTmtId(String tmtId) {
        this.tmtId = tmtId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getStatusEd() {
        return statusEd;
    }

    public void setStatusEd(String statusEd) {
        this.statusEd = statusEd;
    }

    public TMTDrug getTmtDrug() {
        return tmtDrug;
    }

    public void setTmtDrug(TMTDrug tmtDrug) {
        this.tmtDrug = tmtDrug;
    }

    public Fund getFund() {
        return fund;
    }

    public void setFund(Fund fund) {
        this.fund = fund;
    }
    
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 59 * hash + Objects.hashCode(this.tmtId);
        hash = 59 * hash + Objects.hashCode(this.dateIn);
        hash = 59 * hash + Objects.hashCode(this.classifier);
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
        final TMTEdNed other = (TMTEdNed) obj;
        if (!Objects.equals(this.tmtId, other.tmtId)) {
            return false;
        }
        if (!Objects.equals(this.dateIn, other.dateIn)) {
            return false;
        }
        if (!Objects.equals(this.classifier, other.classifier)) {
            return false;
        }
        return true;
    }

}
