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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Version;

/**
 *
 * @author Thanthathon
 */
@Entity
@Table(name = "TMT_ED_STATUS")
public class EDNED implements Serializable {

    @Id
    @TableGenerator(name = "TMT_ED_STATUS_GEN",
            table = "TMT_SEQUENCE",
            pkColumnName = "name",
            valueColumnName = "value",
            allocationSize = 100,
            pkColumnValue = "TMT_ED_STATUS")
    @GeneratedValue(generator = "TMT_ED_STATUS_GEN", strategy = GenerationType.TABLE)
    private Integer id;

    @Column(name = "ED_STATUS")
    private String status;

    @ManyToMany(mappedBy = "edNeds")
    private List<ReimburseGroup> reimburseGroups;

    @ManyToOne
    @JoinColumn(name = "TMTID", referencedColumnName = "TMTID", nullable = false)
    private Drug drug;

    @ManyToOne
    @JoinColumn(name = "FUND_ID", referencedColumnName = "ID", nullable = false)
    private Fund fund;

    @Version
    private Integer version;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<ReimburseGroup> getReimburseGroups() {
        return reimburseGroups;
    }

    public void setReimburseGroups(List<ReimburseGroup> reimburseGroups) {
        this.reimburseGroups = reimburseGroups;
    }

    public Drug getDrug() {
        return drug;
    }

    public void setDrug(Drug drug) {
        this.drug = drug;
    }

    public Fund getFund() {
        return fund;
    }

    public void setFund(Fund fund) {
        this.fund = fund;
    }

    @Override
    public int hashCode() {
        int hash = 7;
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
        final EDNED other = (EDNED) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

}
