/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author thanthathon.b
 */
@Entity
@Table(name = "TMT_REIMBURSE_PRICE_TP")
public class ReimbursePriceTP implements Serializable {

    @EmbeddedId
    private ReimbursePriceTPID id;

    @Column(name = "PRICE", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "CONTENT_CR", length = 50)
    private String content;

    @Column(name = "SPECPREP", length = 2)
    private String specprep;

    @ManyToOne
    @JoinColumn(name = "TMTID", insertable = false, updatable = false)
    private TMTDrug tmtDrug;

    @ManyToOne
    @JoinColumns({
        @JoinColumn(name = "HCODE", referencedColumnName = "HCODE", insertable = false, updatable = false),
        @JoinColumn(name = "HOSPDRUGCODE", referencedColumnName = "HOSPDRUGCODE", insertable = false, updatable = false)})
    private HospitalDrug hospitalDrug;

    public ReimbursePriceTP() {
    }

    public ReimbursePriceTPID getId() {
        return id;
    }

    public void setId(ReimbursePriceTPID id) {
        this.id = id;
    }

    public HospitalDrug getHospitalDrug() {
        return hospitalDrug;
    }

    public void setHospitalDrug(HospitalDrug hospitalDrug) {
        this.hospitalDrug = hospitalDrug;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSpecprep() {
        return specprep;
    }

    public void setSpecprep(String specprep) {
        this.specprep = specprep;
    }

    public TMTDrug getTmtDrug() {
        return tmtDrug;
    }

    public void setTmtDrug(TMTDrug tmtDrug) {
        this.tmtDrug = tmtDrug;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + Objects.hashCode(this.id);
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
        final ReimbursePriceTP other = (ReimbursePriceTP) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

}
