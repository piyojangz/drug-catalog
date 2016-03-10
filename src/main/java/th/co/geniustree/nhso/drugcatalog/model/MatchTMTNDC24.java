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
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author Thanthathon
 */
@Entity
@Table(name = "TMT_TMTDRUG_NDC24")
@IdClass(MatchTMTNDC24ID.class)
public class MatchTMTNDC24 implements Serializable {

    @Id
    @Column(name = "TMTID", length = 6, nullable = false)
    private String tmtid;

    @Id
    @Column(name = "NDC24", length = 24, nullable = false)
    private String ndc24;
    
    @ManyToOne
    @JoinColumn(name = "TMTID", insertable = false,updatable = false,nullable = false)
    private TMTDrug tmtDrug;

    public String getTmtid() {
        return tmtid;
    }

    public void setTmtid(String tmtid) {
        this.tmtid = tmtid;
    }

    public String getNdc24() {
        return ndc24;
    }

    public void setNdc24(String ndc24) {
        this.ndc24 = ndc24;
    }

    public TMTDrug getTmtDrug() {
        return tmtDrug;
    }

    public void setTmtDrug(TMTDrug tmtDrug) {
        this.tmtDrug = tmtDrug;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 61 * hash + Objects.hashCode(this.tmtid);
        hash = 61 * hash + Objects.hashCode(this.ndc24);
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
        final MatchTMTNDC24 other = (MatchTMTNDC24) obj;
        if (!Objects.equals(this.tmtid, other.tmtid)) {
            return false;
        }
        if (!Objects.equals(this.ndc24, other.ndc24)) {
            return false;
        }
        return true;
    }

}
