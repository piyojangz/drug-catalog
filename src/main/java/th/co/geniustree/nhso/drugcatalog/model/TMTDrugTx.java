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
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author moth
 */
@Entity
@Table(name = "TMT_DRUG_TX")
public class TMTDrugTx implements Serializable {

    @EmbeddedId
    private TMTDrugTxPK id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "TMTID", referencedColumnName = "TMTID", nullable = false)
    private TMTDrug tmtDrug;

    @ManyToOne
    @JoinColumns({
        @JoinColumn(name = "HCODE", referencedColumnName = "HCODE", nullable = false, insertable = false, updatable = false),
        @JoinColumn(name = "HOSPDRUGCODE", referencedColumnName = "HOSPDRUGCODE", nullable = false, insertable = false, updatable = false)
    })
    private HospitalDrug hospitalDrug;

    public TMTDrugTx() {
    }

    public TMTDrugTx(TMTDrug tmtDrug, HospitalDrug hospitalDrug) {
        this.tmtDrug = tmtDrug;
        this.hospitalDrug = hospitalDrug;
        id = new TMTDrugTxPK(hospitalDrug);
    }

    public TMTDrugTxPK getId() {
        return id;
    }

    public void setId(TMTDrugTxPK id) {
        this.id = id;
    }

    public TMTDrug getTmtDrug() {
        return tmtDrug;
    }

    public void setTmtDrug(TMTDrug tmtDrug) {
        this.tmtDrug = tmtDrug;
    }

    public HospitalDrug getHospitalDrug() {
        return hospitalDrug;
    }

    public void setHospitalDrug(HospitalDrug hospitalDrug) {
        this.hospitalDrug = hospitalDrug;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + Objects.hashCode(this.id);
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
        final TMTDrugTx other = (TMTDrugTx) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

}
