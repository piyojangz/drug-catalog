/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.model.request;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import th.co.geniustree.nhso.drugcatalog.model.UploadHospitalDrugItem;
import th.co.geniustree.nhso.drugcatalog.model.tmt.HospitalTMTDrug;

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
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Integer id;
    @Column(name = "HCODE", insertable = false, updatable = false)
    private String hcode;
    private String requestUser;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date requestDate;
    @Enumerated(EnumType.STRING)
    private Status status;
    private String approveUser;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date approveDate;
    private String message;
    @OneToOne
    private UploadHospitalDrugItem requestItem;
    @OneToOne
    private HospitalTMTDrug targetItem;

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

    public UploadHospitalDrugItem getRequestItem() {
        return requestItem;
    }

    public void setRequestItem(UploadHospitalDrugItem requestItem) {
        this.requestItem = requestItem;
    }

    public HospitalTMTDrug getTargetItem() {
        return targetItem;
    }

    public void setTargetItem(HospitalTMTDrug targetItem) {
        this.targetItem = targetItem;
    }

}
