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

/**
 *
 * @author Thanthathon
 */
@Entity
public class ISEDApprove implements Serializable {

    @Id
    private Integer id;
    private String isedApprove;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getIsedApprove() {
        return isedApprove;
    }

    public void setIsedApprove(String isedApprove) {
        this.isedApprove = isedApprove;
    }

    @Override
    public int hashCode() {
        int hash = 3;
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
        final ISEDApprove other = (ISEDApprove) obj;
        return Objects.equals(this.id, other.id);
    }
}