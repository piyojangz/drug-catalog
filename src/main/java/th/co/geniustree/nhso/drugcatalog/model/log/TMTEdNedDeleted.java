/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.model.log;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author thanthathon.b
 */
@Entity
@Table(name = "TMT_TMTEDNED_DELETED")
public class TMTEdNedDeleted implements Serializable {

    @Id
    @TableGenerator(name = "TMT_TMTEDNED_DELETED_GEN",
            table = "TMT_SEQUENCE",
            pkColumnName = "name",
            valueColumnName = "value",
            pkColumnValue = "TMT_TMTEDNED_DELETED")
    @GeneratedValue(generator = "TMT_TMTEDNED_DELETED_GEN", strategy = GenerationType.TABLE)
    private Integer id;
    
    @Column(name = "TMTID", length = 6)
    private String tmtId;
    
    @Temporal(TemporalType.DATE)
    @Column(name = "DATEIN")
    private Date dateIn;

    @Column(name = "CLASSIFIER", length = 3)
    private String classifier;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATEDATE")
    private Date createDate;
    
    @Column(name = "STATUS_ED", length = 2)
    private String statusEd;
    
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DELETED_DATE", nullable = false)
    private Date deletedDate;

    @PrePersist
    public void prePersist(){
        this.deletedDate = new Date();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTmtId() {
        return tmtId;
    }

    public void setTmtId(String tmtId) {
        this.tmtId = tmtId;
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

    public Date getDeletedDate() {
        return deletedDate;
    }

    public void setDeletedDate(Date deletedDate) {
        this.deletedDate = deletedDate;
    }

    @Override
    public int hashCode() {
        int hash = 7;
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
        final TMTEdNedDeleted other = (TMTEdNedDeleted) obj;
        return Objects.equals(this.id, other.id);
    }
    
}
