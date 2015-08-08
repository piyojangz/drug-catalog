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
import javax.persistence.Version;

/**
 *
 * @author Thanthathon
 */
@Entity
@Table(name = "TMT_FUND")
public class Fund implements Serializable {

    @Id
    @Column(name = "FUND_CODE")
    private String code;

    @Column(name = "DESCRIPTION")
    private String name;

    @OneToMany(mappedBy = "fund")
    private List<ReimburseGroupItem> reimburseGroups;

    @OneToMany(mappedBy = "fund")
    private List<TMTEdNed> edNeds;

    @Version
    private Integer version;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ReimburseGroupItem> getReimburseGroups() {
        return reimburseGroups;
    }

    public void setReimburseGroups(List<ReimburseGroupItem> reimburseGroups) {
        this.reimburseGroups = reimburseGroups;
    }

    public List<TMTEdNed> getEdNeds() {
        return edNeds;
    }

    public void setEdNeds(List<TMTEdNed> edNeds) {
        this.edNeds = edNeds;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + Objects.hashCode(this.code);
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
        return Objects.equals(this.code, other.code);
    }

    @Override
    public String toString() {
        return this.code;
    }

}
