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
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;

/**
 *
 * @author moth
 */
@Entity
@Table(name = "TMT_REQUEST_ITEM")
public class RequestItem implements Serializable {

    public enum Status {

        REQUEST, REJECT, ACCEPT
    }
    @Id
    @Column(name = "ID")
    private Integer id;

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

    @ManyToOne
    @JoinColumns({
        @JoinColumn(name = "HCODE", referencedColumnName = "HCODE"),
        @JoinColumn(name = "HOSPDRUGCODE", referencedColumnName = "HOSPDRUGCODE")
    })
    private HospitalDrug targetItem;

    @OneToOne
    @PrimaryKeyJoinColumn(name = "ID", referencedColumnName = "ID")
    private UploadHospitalDrugItem uploadDrugItem;

    @ElementCollection
    @CollectionTable(name = "TMT_ERRORCOLUMNS")
    private Set<String> errorColumns;

    @PrePersist
    public void prePersist() {
        requestDate = new Date();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public UploadHospitalDrugItem getUploadDrugItem() {
        return uploadDrugItem;
    }

    public void setUploadDrugItem(UploadHospitalDrugItem uploadDrugItem) {
        this.uploadDrugItem = uploadDrugItem;
        this.id = uploadDrugItem.getId();
    }

    @Override
    public int hashCode() {
        int hash = 7;
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
        final RequestItem other = (RequestItem) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

}
