/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.model;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author Thanthathon
 */
@Entity
@Table(name = "TMT_PARENT_CHILD")
public class TMTRelation implements Serializable {

    @EmbeddedId
    private TMTRelationID id;

    @ManyToOne
    @JoinColumn(name = "TMT_PARENT", referencedColumnName = "TMTID", insertable = false, updatable = false)
    private TMTDrug parent;

    @ManyToOne
    @JoinColumn(name = "TMT_CHILD", referencedColumnName = "TMTID", insertable = false, updatable = false)
    private TMTDrug child;


    public TMTRelation() {
        this.id = new TMTRelationID();
    }
    
    public TMTRelation(TMTRelationID id) {
        this.id = id;
    }
    
    public TMTRelation(TMTDrug tp, TMTDrug tpu) {
        this.id = new TMTRelationID();
        this.id.setParentId(tp.getTmtId());
        this.id.setChildId(tpu.getTmtId());
    }

    public TMTDrug getParent() {
        return parent;
    }

    public void setParent(TMTDrug Parent) {
        this.parent = Parent;
    }

    public TMTDrug getChild() {
        return child;
    }

    public void setChild(TMTDrug child) {
        this.child = child;
    }

    public TMTRelationID getId() {
        return id;
    }

    public void setId(TMTRelationID id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + Objects.hashCode(this.id);
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
        final TMTRelation other = (TMTRelation) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }
    
}
