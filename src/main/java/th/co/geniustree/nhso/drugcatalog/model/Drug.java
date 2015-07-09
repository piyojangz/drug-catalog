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
@Table(name = "TMT_DRUG")
public class Drug implements Serializable, TMT {

    @Id
    @Column(name = "TMTID")
    private String id;

    @Column(name = "FSN")
    private String fsn;
    
    @Version
    private Integer version;
    
    @ManyToMany(mappedBy = "tmtDrugs")
    private List<SpecialProject> specialProjects;

    public String getFsn() {
        return fsn;
    }

    public void setFsn(String fsn) {
        this.fsn = fsn;
    }

    @Override
    public String getTmtId() {
        return id;
    }

    @Override
    public void setTmtId(String tmtId) {
        this.id = tmtId;
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
        hash = 31 * hash + Objects.hashCode(this.id);
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
        final Drug other = (Drug) obj;
        return Objects.equals(this.id, other.id);
    }

    
}
