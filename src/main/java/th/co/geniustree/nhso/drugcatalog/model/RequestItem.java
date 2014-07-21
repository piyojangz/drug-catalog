/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
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
        @TableGenerator(name = "TMT_REQUEST_ITEM_GEN",
            table = "TMT_SEQUENCE",
            pkColumnName = "name",
            valueColumnName = "value",
            pkColumnValue = "TMT_REQUEST_ITEM")
    @GeneratedValue(generator = "TMT_REQUEST_ITEM_GEN", strategy = GenerationType.TABLE)
    private Integer id;
    @Column(name = "HCODE", insertable = false, updatable = false, length = 5)
    private String hcode;

    @Column(name = "REQUESTUSER", nullable = false, length = 60)
    private String requestUser;

    @Column(name = "REQUESTDATE", nullable = false)
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date requestDate;

    @Column(name = "STATUS", length = 10)
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
    @JoinColumn(name = "UPLOADHOSPDRUG_ITEM_ID", referencedColumnName = "ID", nullable = false)
    private UploadHospitalDrugItem requestItem;

    @OneToOne
    @JoinColumns({
        @JoinColumn(name = "HCODE", referencedColumnName = "HCODE", nullable = false),
        @JoinColumn(name = "HOSPDRUGCODE", referencedColumnName = "HOSPDRUGCODE", nullable = false)
    })
    private HospitalDrug targetItem;

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

    public HospitalDrug getTargetItem() {
        return targetItem;
    }

    public void setTargetItem(HospitalDrug targetItem) {
        this.targetItem = targetItem;
    }

}
