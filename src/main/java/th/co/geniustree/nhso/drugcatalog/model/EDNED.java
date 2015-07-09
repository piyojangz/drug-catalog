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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;

/**
 *
 * @author Thanthathon
 */
@Entity
@Table(name = "TMT_ED")
public class EDNED implements Serializable {
    
    @Id
    @Column(name = "ED_NED")
    private String id;
    
    @ManyToMany
    @JoinColumn(name = "SPECIAL_PROJECT" ,referencedColumnName = "EDNEDS")
    private List<SpecialProject> specialProjects;
    
    @OneToMany(mappedBy = "edNed")
    private List<ReimburseGroup> reimburseGroups;
    
    @Version
    private Integer version;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<ReimburseGroup> getReimburseGroups() {
        return reimburseGroups;
    }

    public void setReimburseGroups(List<ReimburseGroup> reimburseGroups) {
        this.reimburseGroups = reimburseGroups;
    }

    public List<SpecialProject> getSpecialProjects() {
        return specialProjects;
    }

    public void setSpecialProjects(List<SpecialProject> specialProjects) {
        this.specialProjects = specialProjects;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 19 * hash + Objects.hashCode(this.id);
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
        final EDNED other = (EDNED) obj;
        return Objects.equals(this.id, other.id);
    }
    
}
