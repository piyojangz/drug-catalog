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
@Table(name = "TMT_FUND")
public class Fund implements Serializable {

    @Id
    @Column(name = "ID")
    private String id;

    @Column(name = "DESCRIPTION")
    private String name;
    
    @ManyToMany(mappedBy = "funds")
    private List<SpecialProject> specialProjects;

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

    public List<SpecialProject> getSpecialProjects() {
        return specialProjects;
    }

    public void setSpecialProjects(List<SpecialProject> specialProjects) {
        this.specialProjects = specialProjects;
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
        final Fund other = (Fund) obj;
        return Objects.equals(this.id, other.id);
    }
    
}
