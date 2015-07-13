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
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Version;

/**
 *
 * @author Thanthathon
 */
@Entity
@Table(name = "ICD10")
public class ICD10 implements Serializable {
    
    @Id
    @Column(name = "ICD10_ID")
    private String id;
    
    @Column(name = "ICD10_NAME")
    private String name;
    
    @ManyToMany(mappedBy = "icd10s")
    private List<ReimburseGroup> reimBurseGroups;
    
    @Version
    private Integer version;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public List<ReimburseGroup> getReimBurseGroups() {
        return reimBurseGroups;
    }

    public void setReimBurseGroups(List<ReimburseGroup> reimBurseGroups) {
        this.reimBurseGroups = reimBurseGroups;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 67 * hash + Objects.hashCode(this.id);
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
        final ICD10 other = (ICD10) obj;
        return Objects.equals(this.id, other.id);
    }
    
}