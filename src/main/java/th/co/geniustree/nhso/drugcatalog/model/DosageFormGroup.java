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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

/**
 *
 * @author Thanthathon
 */
@Entity
@Table(name = "TMT_DOSAGEFORM_GROUP")
public class DosageFormGroup implements Serializable {
    
    @Id
    @Column(name = "DOSAGEFORM_GROUP_ID")
    private String idGroup;
    
    @Column(name = "DOSAGEFORM_GROUP_DESCRIPTION")
    private String description;
    
    @Temporal(TemporalType.DATE)
    @Column(name = "CREATE_DATE")
    private Date createDate;
    
    @Version
    private Integer version;

    public DosageFormGroup() {
    }

    public DosageFormGroup(String idGroup, String description) {
        this.idGroup = idGroup;
        this.description = description;
    }
    
    public String getIdGroup() {
        return idGroup;
    }

    public void setIdGroup(String idGroup) {
        this.idGroup = idGroup;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 59 * hash + Objects.hashCode(this.idGroup);
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
        final DosageFormGroup other = (DosageFormGroup) obj;
        if (!Objects.equals(this.idGroup, other.idGroup)) {
            return false;
        }
        return true;
    }
    
    
}
