/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.basicmodel.readonly;

import java.io.Serializable;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author pramoth
 */
@Entity
@Table(name = "HOSPITAL")
@NamedQueries({
    @NamedQuery(name = "Hospital.findAll", query = "SELECT h FROM Hospital h")})
@Cacheable
public class Hospital implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Column(name = "HMAIN", nullable = false, length = 5)
    private String hcode;
    @Column(name = "HNAME", nullable = false, length = 250)
    private String hname;
    @ManyToOne
    @JoinColumn(name = "PROVINCE_ID", nullable = false)
    private Province province;

    @Column(name = "catm")
    private String catmCode;

    @Column(name = "status_uc")
    private String statusUC;

    public Hospital() {
    }

    public String getHcode() {
        return hcode;
    }

    public void setHcode(String hcode) {
        this.hcode = hcode;
    }

    public String getHname() {
        return hname;
    }

    public void setHname(String hname) {
        this.hname = hname;
    }

    public Province getProvince() {
        return province;
    }

    public void setProvince(Province province) {
        this.province = province;
    }

    public String getFullName() {
        if (hcode == null) {
            return hname;
        }

        return hcode + " - " + hname;
    }

    public String getCatmCode() {
        return catmCode;
    }

    public void setCatmCode(String catmCode) {
        this.catmCode = catmCode;
    }

    public String getStatusUC() {
        return statusUC;
    }

    public void setStatusUC(String statusUC) {
        this.statusUC = statusUC;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 23 * hash + (this.hcode != null ? this.hcode.hashCode() : 0);
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
        final Hospital other = (Hospital) obj;
        if ((this.hcode == null) ? (other.hcode != null) : !this.hcode.equals(other.hcode)) {
            return false;
        }
        return true;
    }
}
