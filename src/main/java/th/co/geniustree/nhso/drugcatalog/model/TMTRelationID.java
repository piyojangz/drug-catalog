/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.model;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author Thanthathon
 */
@Embeddable
public class TMTRelationID implements Serializable{
    
    @Column(name = "TMT_PARENT")
    private String parentId;
    
    @Column(name = "TMT_CHILD")
    private String childId;

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getChildId() {
        return childId;
    }

    public void setChildId(String childId) {
        this.childId = childId;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 53 * hash + Objects.hashCode(this.parentId);
        hash = 53 * hash + Objects.hashCode(this.childId);
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
        final TMTRelationID other = (TMTRelationID) obj;
        if (!Objects.equals(this.parentId, other.parentId)) {
            return false;
        }
        if (!Objects.equals(this.childId, other.childId)) {
            return false;
        }
        return true;
    }
    
}
