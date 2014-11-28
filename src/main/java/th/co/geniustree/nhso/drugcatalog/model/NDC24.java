/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.model;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author moth
 */
@Entity
@Table(name="TMT_NDC24")
public class NDC24 implements Serializable{
    @Id
    @Column(name="NDC24")
    private String ndc24;

    public String getNdc24() {
        return ndc24;
    }

    public void setNdc24(String ndc24) {
        this.ndc24 = ndc24;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + Objects.hashCode(this.ndc24);
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
        final NDC24 other = (NDC24) obj;
        if (!Objects.equals(this.ndc24, other.ndc24)) {
            return false;
        }
        return true;
    }
    
}
