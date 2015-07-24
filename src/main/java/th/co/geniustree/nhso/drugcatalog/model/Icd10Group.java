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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;

/**
 *
 * @author Thanthathon
 */
@Entity
@Table(name = "ICD10_GROUP", uniqueConstraints = @UniqueConstraint(columnNames = {"icd10Id", "reimburseGroupId"}))
public class Icd10Group implements Serializable {

    @Id
    @TableGenerator(name = "ICD10_GROUP_GEN",
            table = "TMT_SEQUENCE",
            pkColumnName = "name",
            valueColumnName = "value",
            pkColumnValue = "ICD10_GROUP", allocationSize = 1)
    @GeneratedValue(generator = "ICD10_GROUP_GEN", strategy = GenerationType.TABLE)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "ICD10_ID", insertable = false, updatable = false, nullable = false)
    private ICD10 icd10Id;

    @ManyToOne
    @JoinColumn(name = "REIMBURSE_GROUP_ID", insertable = false, updatable = false, nullable = false)
    private ReimburseGroup reimburseGroupId;

    @Version
    private Integer version;

    public ICD10 getIcd10Id() {
        return icd10Id;
    }

    public void setIcd10Id(ICD10 icd10Id) {
        this.icd10Id = icd10Id;
    }

    public ReimburseGroup getReimburseGroupId() {
        return reimburseGroupId;
    }

    public void setReimburseGroupId(ReimburseGroup reimburseGroupId) {
        this.reimburseGroupId = reimburseGroupId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + Objects.hashCode(this.id);
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
        if (!Objects.equals(this.icd10Id, other.icd10Id)) {
            return false;
        }
        if (!Objects.equals(this.reimburseGroupId, other.reimburseGroupId)) {
            return false;
        }
        return true;
    }

}
