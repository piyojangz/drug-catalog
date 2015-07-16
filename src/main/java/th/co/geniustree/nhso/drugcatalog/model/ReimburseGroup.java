/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.model;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author Thanthathon
 */
@Entity
@Table(name = "TMT_REIMBURSE_GROUP")
public class ReimburseGroup implements Serializable {
    
    @Id
    @Column(name = "REIMBURSE_GROUP_ID")
    private String id;

    @Column(name = "REIMBURSE_GROUP_DESCRIPTION")
    private String description;
    
    @Column(name = "IS_SPECIAL_PROJECT")
    private boolean specialProject;
    
    private Integer version;
    
    @OneToMany(mappedBy = "reimburseGroup")
    private List<ReimburseGroupItem> reimburseGroupItems;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isSpecialProject() {
        return specialProject;
    }

    public void setSpecialProject(boolean specialProject) {
        this.specialProject = specialProject;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public List<ReimburseGroupItem> getReimburseGroupItems() {
        return reimburseGroupItems;
    }

    public void setReimburseGroupItems(List<ReimburseGroupItem> reimburseGroupItems) {
        this.reimburseGroupItems = reimburseGroupItems;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + Objects.hashCode(this.id);
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
        final ReimburseGroup other = (ReimburseGroup) obj;
        return Objects.equals(this.id, other.id);
    }
    
    
}
