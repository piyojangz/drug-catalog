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
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author moth
 */
@Entity
@Table(name = "TMT_EDNED")
@IdClass(HospitalEdNedPK.class)
public class HospitalEdNed implements Serializable {

    @Id
    @Column(name = "HCODE", nullable = false, length = 5)
    private String hcode;
    @Id
    @Temporal(TemporalType.DATE)
    @Column(name = "DATEIN", nullable = false)
    private Date dateIn;
    @Id
    @Column(name = "CLASSIFIER", nullable = false, length = 3)
    private String classifier = HospitalEdNedPK.SUPPORT_CASSIFIER;//support only UC in current version.
    @Id
    @Column(name = "HOSPDRUGCODE", length = 30, nullable = false)
    private String hospDrugCode;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATEDATE", nullable = false)
    private Date createDate;
    @Column(name = "STATUS_ED", length = 2, nullable = false)
    private String statusEd;

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

    public String getHospDrugCode() {
        return hospDrugCode;
    }

    public void setHospDrugCode(String hospDrugCode) {
        this.hospDrugCode = hospDrugCode;
    }

    public String getHcode() {
        return hcode;
    }

    public void setHcode(String hcode) {
        this.hcode = hcode;
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
    

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 23 * hash + Objects.hashCode(this.hcode);
        hash = 23 * hash + Objects.hashCode(this.dateIn);
        hash = 23 * hash + Objects.hashCode(this.classifier);
        hash = 23 * hash + Objects.hashCode(this.hospDrugCode);
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
        final HospitalEdNed other = (HospitalEdNed) obj;
        if (!Objects.equals(this.hcode, other.hcode)) {
            return false;
        }
        if (!Objects.equals(this.dateIn, other.dateIn)) {
            return false;
        }
        if (!Objects.equals(this.classifier, other.classifier)) {
            return false;
        }
        if (!Objects.equals(this.hospDrugCode, other.hospDrugCode)) {
            return false;
        }
        return true;
    }

}
