/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedSubgraph;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.Version;
import th.co.geniustree.xls.beans.XlsColumn;

/**
 *
 * @author moth
 */
@Entity
@Table(name = "TMT_DRUG", indexes = {
    @Index(name = "TMT_DRUG_TYPE_IDX", columnList = "TYPE")
})
@NamedEntityGraph(name = "TMTDrug.druggroup", includeAllAttributes = true,
        attributeNodes = {
            @NamedAttributeNode(value = "drugGroupItems", subgraph = "drugGroupItem")
        },
        subgraphs = {
            @NamedSubgraph(name = "drugGroupItem", attributeNodes = {
                @NamedAttributeNode(value = "tmtDrug"),
                @NamedAttributeNode(value = "drugGroup"),
                @NamedAttributeNode(value = "datein"),
                @NamedAttributeNode(value = "dateOut")
            })
        })
public class TMTDrug implements Serializable, TMT {

    public enum Type {
        SUB, VTM, GP, GPU, TP, TPU, X
    }
    @XlsColumn(columnNames = {"TPUCODE"})
    @Id
    @Column(name = "TMTID", length = 6, nullable = false)
    private String tmtId;
    @XlsColumn
    @Column(name = "ACTIVEINGREDIENT", length = 300)
    private String activeIngredient;
    @XlsColumn
    @Column(name = "STRENGTH", length = 255)
    private String strength;
    @XlsColumn
    @Column(name = "DOSAGEFORM", length = 255)
    private String dosageform;
    @XlsColumn
    @Column(name = "CONTVALUE", length = 255)
    private String contvalue;
    @XlsColumn
    @Column(name = "CONTUNIT", length = 255)
    private String contunit;
    @XlsColumn
    @Column(name = "DISPUNIT", length = 255)
    private String dispUnit;
    @XlsColumn
    @Column(name = "TRADENAME", length = 255, columnDefinition = "NVARCHAR2(255)")
    private String tradeName;
    @XlsColumn
    @Column(name = "MANUFACTURER", length = 255, columnDefinition = "NVARCHAR2(255)")
    private String manufacturer;
    @XlsColumn
    @Column(name = "FSN", length = 1000, columnDefinition = "NVARCHAR2(1000)")
    private String fsn;
    @XlsColumn
    @Column(name = "STATUS", length = 2)
    private String status;

    @Column(name = "CHANGEDATE", nullable = false)
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date changeDate;

    @Column(name = "CREATEDATE", nullable = false)
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date createDate;

    @Column(name = "LASTMODIFIEDDATE", nullable = false)
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date lastModifiedDate;
    @Column(name = "TYPE", nullable = false, length = 3)
    @Enumerated(EnumType.STRING)
    private Type type;
    @OneToMany(mappedBy = "tmtDrug")
    private List<TMTDrugGroupItem> drugGroupItems;

    @OneToMany(mappedBy = "tmtDrug")
    private List<TMTEdNed> edNeds;
    @ManyToMany
    @JoinTable(
            name = "TMT_TMTDRUG_NDC24",
            joinColumns = {
                @JoinColumn(name = "TMTID", referencedColumnName = "TMTID", nullable = false)},
            inverseJoinColumns = {
                @JoinColumn(name = "NDC24", referencedColumnName = "NDC24", nullable = false)}
    )
    private List<NDC24> ndc24s;

    @XlsColumn
    @ManyToMany
    @JoinTable(name = "TMT_PARENT_CHILD",
            joinColumns = @JoinColumn(name = "TMT_CHILD", referencedColumnName = "TMTID"),
            inverseJoinColumns = @JoinColumn(name = "TMT_PARENT", referencedColumnName = "TMTID"))
    private List<TMTDrug> parents;

    @ManyToMany(mappedBy = "parents")
    private List<TMTDrug> children;
    
    @Column(name = "DOSAGEFORM_GROUP", length = 100)
    private String dosageformGroup;

    @OneToMany(mappedBy = "tmtDrug", fetch = FetchType.LAZY)
    private List<ReimbursePrice> reimbursePrices;

    @Version
    private Integer version;

    public TMTDrug() {
    }

    public TMTDrug(String tmtId) {
        this.tmtId = tmtId;
    }

    @PrePersist
    public void prePersist() {
        createDate = new Date();
        lastModifiedDate = new Date();
    }

    @PreUpdate
    public void preUpdate() {
        lastModifiedDate = new Date();
    }

    public String getTmtId() {
        return tmtId;
    }

    public void setTmtId(String tmtId) {
        this.tmtId = tmtId;
    }

    public String getActiveIngredient() {
        return activeIngredient;
    }

    public void setActiveIngredient(String activeIngredient) {
        this.activeIngredient = activeIngredient;
    }

    public String getStrength() {
        return strength;
    }

    public void setStrength(String strength) {
        this.strength = strength;
    }

    public String getDosageform() {
        return dosageform;
    }

    public void setDosageform(String dosageform) {
        this.dosageform = dosageform;
    }

    public String getContvalue() {
        return contvalue;
    }

    public void setContvalue(String contvalue) {
        this.contvalue = contvalue;
    }

    public String getContunit() {
        return contunit;
    }

    public void setContunit(String contunit) {
        this.contunit = contunit;
    }

    public String getDispUnit() {
        return dispUnit;
    }

    public void setDispUnit(String dispUnit) {
        this.dispUnit = dispUnit;
    }

    public String getTradeName() {
        return tradeName;
    }

    public void setTradeName(String tradeName) {
        this.tradeName = tradeName;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getFsn() {
        return fsn;
    }

    public void setFsn(String fsn) {
        this.fsn = fsn;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Date getChageDate() {
        return changeDate;
    }

    public void setChageDate(Date chageDate) {
        this.changeDate = chageDate;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public Date getChangeDate() {
        return changeDate;
    }

    public void setChangeDate(Date changeDate) {
        this.changeDate = changeDate;
    }

    public List<TMTDrugGroupItem> getDrugGroupItems() {
        return drugGroupItems;
    }

    public void setDrugGroupItems(List<TMTDrugGroupItem> drugGroupItems) {
        this.drugGroupItems = drugGroupItems;
    }

    public List<TMTEdNed> getEdNeds() {
        if (edNeds == null) {
            edNeds = new ArrayList<>();
        }
        return edNeds;
    }

    public void setEdNeds(List<TMTEdNed> edNeds) {
        this.edNeds = edNeds;
    }

    public List<NDC24> getNdc24s() {
        return ndc24s;
    }

    public void setNdc24s(List<NDC24> ndc24s) {
        this.ndc24s = ndc24s;
    }

    public String getDosageformGroup() {
        return dosageformGroup;
    }

    public void setDosageformGroup(String dosageformGroup) {
        this.dosageformGroup = dosageformGroup;
    }

    public List<ReimbursePrice> getReimbursePrices() {
        return reimbursePrices;
    }

    public void setReimbursePrices(List<ReimbursePrice> reimbursePrices) {
        this.reimbursePrices = reimbursePrices;
    }

    public List<TMTDrug> getParents() {
        return parents;
    }

    public void setParents(List<TMTDrug> parents) {
        this.parents = parents;
    }

    public List<TMTDrug> getChildren() {
        return children;
    }

    public void setChildren(List<TMTDrug> children) {
        this.children = children;
    }
    
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 79 * hash + Objects.hashCode(this.tmtId);
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
        final TMTDrug other = (TMTDrug) obj;
        if (!Objects.equals(this.tmtId, other.tmtId)) {
            return false;
        }
        return true;
    }

}
