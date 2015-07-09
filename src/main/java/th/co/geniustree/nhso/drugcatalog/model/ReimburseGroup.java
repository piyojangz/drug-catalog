/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.model;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
    
    @ManyToOne
    @JoinColumn(name = "EDNED", referencedColumnName = "ED_NED")
    private EDNED edNed;
    
    @ManyToOne
    @JoinColumn(name = "ICD10", referencedColumnName = "ICD10")
    private ICD10 icd10;

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

    public EDNED getEdNed() {
        return edNed;
    }

    public void setEdNed(EDNED edNed) {
        this.edNed = edNed;
    }

    public ICD10 getIcd10() {
        return icd10;
    }

    public void setIcd10(ICD10 icd10) {
        this.icd10 = icd10;
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
