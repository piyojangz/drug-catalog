/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author moth
 */
@Entity
@Table(name = "TMT_APPROVE_FILE")
public class ApproveFile implements Serializable {

    @Id
    @TableGenerator(name = "APPROVE_FILE_GEN",
            table = "TMT_SEQUENCE",
            pkColumnName = "name",
            valueColumnName = "value",
            pkColumnValue = "APPROVE_FILE")
    @GeneratedValue(generator = "APPROVE_FILE_GEN", strategy = GenerationType.TABLE)
    @Column(name = "ID", nullable = false)
    private Integer id;
    @Column(name = "FILENAME", nullable = false)
    private String fileName;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "UPLOAD_DATE", nullable = false)
    private Date uploadDate;
    @Column(name = "UPLOAD_USER", nullable = false)
    private String uploadUser;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Date getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(Date uploadDate) {
        this.uploadDate = uploadDate;
    }

    public String getUploadUser() {
        return uploadUser;
    }

    public void setUploadUser(String uploadUser) {
        this.uploadUser = uploadUser;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 43 * hash + Objects.hashCode(this.id);
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
        final ApproveFile other = (ApproveFile) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

}
