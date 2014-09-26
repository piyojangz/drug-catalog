/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.model;

import java.io.Serializable;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;

/**
 *
 * @author moth
 */
@Entity
@Table(name = "TMT_REQUEST_ITEM")
@IdClass(HospitalDrugPK.class)
public class RequestItem implements Serializable {

    public enum Status {

        REQUEST, REJECT, ACCEPT
    }
    @Id
    @Column(name = "HCODE", nullable = false, length = 5)
    private String hcode;
    
    @Id
    @Column(name = "HOSPDRUGCODE", nullable = false, length = 30)
    private String hospDrugCode;

    @Column(name = "REQUESTUSER", nullable = false, length = 60)
    private String requestUser;

    @Column(name = "REQUESTDATE", nullable = false)
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date requestDate;

    @Column(name = "STATUS", length = 10, nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "APPROVEUSER", nullable = true, length = 60)
    private String approveUser;

    @Column(name = "APPROVEDATE", nullable = true)
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date approveDate;

    @Column(name = "MESSAGE", length = 256, nullable = true)
    private String message;
    
    @OneToOne
    @JoinColumns({
        @JoinColumn(name = "HCODE", referencedColumnName = "HCODE", nullable = false, insertable = false, updatable = false),
        @JoinColumn(name = "HOSPDRUGCODE", referencedColumnName = "HOSPDRUGCODE", nullable = false, insertable = false, updatable = false)
    })
    private HospitalDrug targetItem;

    @ElementCollection
    @CollectionTable(name = "TMT_ERRORCOLUMNS")
    private Set<String> errorColumns;

    @PrePersist
    public void prePersist() {
        requestDate = new Date();
    }

    public String getHospDrugCode() {
        return hospDrugCode;
    }

    public void setHospDrugCode(String hospDrugCode) {
        this.hospDrugCode = hospDrugCode;
    }

    public String getHcode() {
        return hcode;
    }

    public void setHcode(String hcode) {
        this.hcode = hcode;
    }

    public String getRequestUser() {
        return requestUser;
    }

    public void setRequestUser(String requestUser) {
        this.requestUser = requestUser;
    }

    public Date getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(Date requestDate) {
        this.requestDate = requestDate;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getApproveUser() {
        return approveUser;
    }

    public void setApproveUser(String approveUser) {
        this.approveUser = approveUser;
    }

    public Date getApproveDate() {
        return approveDate;
    }

    public void setApproveDate(Date approveDate) {
        this.approveDate = approveDate;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public HospitalDrug getTargetItem() {
        return targetItem;
    }

    public void setTargetItem(HospitalDrug targetItem) {
        this.targetItem = targetItem;
    }

    public Set<String> getErrorColumns() {
        if (errorColumns == null) {
            errorColumns = new LinkedHashSet<>();
        }
        return errorColumns;
    }

    public void setErrorColumns(Set<String> errorColumns) {
        this.errorColumns = errorColumns;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.hcode);
        hash = 59 * hash + Objects.hashCode(this.hospDrugCode);
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
        final RequestItem other = (RequestItem) obj;
        if (!Objects.equals(this.hcode, other.hcode)) {
            return false;
        }
        if (!Objects.equals(this.hospDrugCode, other.hospDrugCode)) {
            return false;
        }
        return true;
    }

}
