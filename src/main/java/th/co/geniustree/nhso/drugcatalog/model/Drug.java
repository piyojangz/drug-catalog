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
@Table(name = "TMT_DRUG")
public class Drug implements Serializable, TMT {

    @Id
    @Column(name = "TMTID")
    private String tmtId;

    @Column(name = "FSN")
    private String fsn;
    
    @Version
    private Integer version;
    
    @OneToMany(mappedBy = "tmtDrug")
    private List<ReimburseGroupItem> reimburseGroups;
    
    @OneToMany(mappedBy = "tmtDrug")
    private List<EdNed> edNeds;

    public String getFsn() {
        return fsn;
    }

    public void setFsn(String fsn) {
        this.fsn = fsn;
    }

    @Override
    public String getTmtId() {
        return tmtId;
    }

    @Override
    public void setTmtId(String tmtId) {
        this.tmtId = tmtId;
    }

    public List<ReimburseGroupItem> getReimburseGroups() {
        return reimburseGroups;
    }

    public void setReimburseGroups(List<ReimburseGroupItem> reimburseGroups) {
        this.reimburseGroups = reimburseGroups;
    }

    public List<EdNed> getEdNeds() {
        return edNeds;
    }

    public void setEdNeds(List<EdNed> edNeds) {
        this.edNeds = edNeds;
    }
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + Objects.hashCode(this.tmtId);
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
        return Objects.equals(this.tmtId, other.tmtId);
    }

    
}
