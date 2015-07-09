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
import javax.persistence.Table;
import javax.persistence.Version;

/**
 *
 * @author Thanthathon
 */
@Entity
@Table(name = "TMT_SPECIALPROJECT")
public class SpecialProject implements Serializable {

    @Id
    private Integer id;

    @ManyToMany
    @JoinColumn(name = "TMT_FUND", referencedColumnName = "ID")
    private List<Fund> funds;

    @ManyToMany
    @JoinColumn(name = "TMT_ICD10", referencedColumnName = "ICD10")
    private List<ICD10> icd10s;

    @ManyToMany
    @JoinColumn(name = "TMT_DRUG", referencedColumnName = "TMTID")
    private List<Drug> tmtDrugs;
    
    @ManyToMany(mappedBy = "specialProjects")
    private List<EDNED> edNeds;

    @Column(name = "SPECIALPROJECT_NAME")
    private String name;

    @Version
    private Integer version;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<Fund> getFunds() {
        return funds;
    }

    public void setFunds(List<Fund> funds) {
        this.funds = funds;
    }

    public List<ICD10> getIcd10s() {
        return icd10s;
    }

    public void setIcd10s(List<ICD10> icd10s) {
        this.icd10s = icd10s;
    }

    public List<Drug> getTmtDrugs() {
        return tmtDrugs;
    }

    public void setTmtDrugs(List<Drug> tmtDrugs) {
        this.tmtDrugs = tmtDrugs;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<EDNED> getEdNeds() {
        return edNeds;
    }

    public void setEdNeds(List<EDNED> edNeds) {
        this.edNeds = edNeds;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 61 * hash + Objects.hashCode(this.id);
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
        final SpecialProject other = (SpecialProject) obj;
        return Objects.equals(this.id, other.id);
    }
    
    
    
}
