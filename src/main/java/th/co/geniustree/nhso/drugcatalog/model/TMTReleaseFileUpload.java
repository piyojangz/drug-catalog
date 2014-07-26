/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author moth
 */
@Entity
@Table(name = "TMT_RELEASEFILE")
public class TMTReleaseFileUpload implements Serializable {

    @Id
    @Temporal(TemporalType.DATE)
    private Date releaseDate;
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;

    public TMTReleaseFileUpload() {
    }

    public TMTReleaseFileUpload(Date releaseDate) {
        this.releaseDate = releaseDate;
    }
    

    @PrePersist
    public void prePersist() {
        createDate = new Date();
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 79 * hash + Objects.hashCode(this.releaseDate);
        hash = 79 * hash + Objects.hashCode(this.createDate);
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
        final TMTReleaseFileUpload other = (TMTReleaseFileUpload) obj;
        if (!Objects.equals(this.releaseDate, other.releaseDate)) {
            return false;
        }
        if (!Objects.equals(this.createDate, other.createDate)) {
            return false;
        }
        return true;
    }

}
