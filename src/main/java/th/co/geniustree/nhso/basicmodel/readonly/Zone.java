/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.basicmodel.readonly;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author pramoth
 */
@Entity
@Table(name = "ZONE")
@XmlAccessorType(XmlAccessType.FIELD)
public class Zone implements Serializable {

    @Id
    @Column(name = "NHSO_ZONE")
    private String nhsoZone;
    @Column(name = "NHSO_ZONENAME")
    private String zoneName;
    @XmlTransient
    @OneToMany(mappedBy = "nhsoZone")
    private List<Province> provinces;

    public String getNhsoZone() {
        return nhsoZone;
    }

    public void setNhsoZone(String nhsoZone) {
        this.nhsoZone = nhsoZone;
    }

    public String getZoneName() {
        return zoneName;
    }

    public void setZoneName(String zoneName) {
        this.zoneName = zoneName;
    }

    public String getFullName() {
        if(nhsoZone == null){
            return zoneName;
        }
        
        return nhsoZone + " - " + zoneName;
    }

    public List<Province> getProvinces() {
        return provinces;
    }

    public void setProvinces(List<Province> provinces) {
        this.provinces = provinces;
    }
    

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + (this.nhsoZone != null ? this.nhsoZone.hashCode() : 0);
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
        final Zone other = (Zone) obj;
        if ((this.nhsoZone == null) ? (other.nhsoZone != null) : !this.nhsoZone.equals(other.nhsoZone)) {
            return false;
        }
        return true;
    }
}
