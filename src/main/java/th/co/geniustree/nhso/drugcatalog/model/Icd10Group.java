/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.model;

import java.io.Serializable;
import java.util.Objects;
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
@Table(name = "ICD10_GROUP")
@IdClass(Icd10GroupPK.class)
public class Icd10Group implements Serializable {

    @Id
    @ManyToOne
    @JoinColumn(name = "ICD10_ID")
    private ICD10 icd10;
    
    @Id
    @ManyToOne
    @JoinColumn(name = "REIMBURSE_GROUP_ID")
    private ReimburseGroup reimburseGroup;

    @Version
    private Integer version;

    public Icd10Group() {
    }

    public Icd10Group(ICD10 icd10, ReimburseGroup reimburseGroup) {
        this.icd10 = icd10;
        this.reimburseGroup = reimburseGroup;
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
        int hash = 3;
        hash = 79 * hash + Objects.hashCode(this.icd10);
        hash = 79 * hash + Objects.hashCode(this.reimburseGroup);
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
        final Icd10Group other = (Icd10Group) obj;
        if (!Objects.equals(this.icd10, other.icd10)) {
            return false;
        }
        if (!Objects.equals(this.reimburseGroup, other.reimburseGroup)) {
            return false;
        }
        return true;
    }

}
