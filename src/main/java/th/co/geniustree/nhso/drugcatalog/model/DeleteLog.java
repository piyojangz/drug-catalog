/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.model;

import com.google.common.base.Joiner;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import org.apache.avro.generic.GenericData;

/**
 *
 * @author moth
 */
@Table(name="TMT_DELETELOG")
@Entity
public class DeleteLog implements Serializable{
    @Id
    @TableGenerator(name = "TMT_DELETELOG_GEN",
            table = "TMT_SEQUENCE",
            pkColumnName = "name",
            valueColumnName = "value",
            pkColumnValue = "TMT_DELETELOG")
    @GeneratedValue(generator = "TMT_DELETELOG_GEN", strategy = GenerationType.TABLE)
    private Integer id;
    @Column(name="DELETE_USER",nullable = false)
    private String deleteUser;
    @Column(name="TARGET_HCODE",nullable = false)
    private String targetHcode;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="DELETE_DATE",nullable = false)
    private Date deleteDate;
    @Column(name="FILES",length = 2000)
    private String files;
    @Transient
    private List<String> filesToBeDeletes = new ArrayList<>();
    public DeleteLog() {
    }

    public DeleteLog(String deleteUser, String targetHcode, Date deleteDate) {
        this.deleteUser = deleteUser;
        this.targetHcode = targetHcode;
        this.deleteDate = deleteDate;
    }
    @PrePersist
    public void prePersist(){
        files = Joiner.on(",").join(filesToBeDeletes);
    }
    

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDeleteUser() {
        return deleteUser;
    }

    public void setDeleteUser(String deleteUser) {
        this.deleteUser = deleteUser;
    }

    public String getTargetHcode() {
        return targetHcode;
    }

    public void setTargetHcode(String targetHcode) {
        this.targetHcode = targetHcode;
    }

    public Date getDeleteDate() {
        return deleteDate;
    }

    public void setDeleteDate(Date deleteDate) {
        this.deleteDate = deleteDate;
    }
    public void addFile(String file){
        filesToBeDeletes.add(file);
    }

    public String getFiles() {
        return files;
    }

    public void setFiles(String files) {
        this.files = files;
    }
    

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 23 * hash + Objects.hashCode(this.id);
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
        final DeleteLog other = (DeleteLog) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }
    
}
