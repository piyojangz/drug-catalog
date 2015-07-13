/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.model;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
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
@Table(name = "TMT_REIMBURSE_GROUP")
public class ReimburseGroup implements Serializable {

    @Id
    private String id;

    private String name;

    @ManyToMany
    @JoinColumn(name = "TMTID", referencedColumnName = "TMTID")
    private List<Drug> drugs;

    @ManyToMany
    @JoinColumn(name = "FUND_ID", referencedColumnName = "ID")
    private List<Fund> funds;

    @ManyToMany
    @JoinColumn(name = "ICD10_ID", referencedColumnName = "ICD10_ID")
    private List<ICD10> icd10s;

    @ManyToMany
    @JoinColumn(name = "ED_STATUS" , referencedColumnName = "ID")
    private List<EDNED> edNeds;

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

    public List<Drug> getDrugs() {
        return drugs;
    }

    public void setDrugs(List<Drug> drugs) {
        this.drugs = drugs;
    }

    public List<Fund> getFunds() {
        return funds;
    }

    public void setFunds(List<Fund> funds) {
        this.funds = funds;
    }

    public List<ICD10> getIcd10ss() {
        return icd10s;
    }

    public void setIcd10ss(List<ICD10> icd10ss) {
        this.icd10s = icd10ss;
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
        hash = 47 * hash + Objects.hashCode(this.id);
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
