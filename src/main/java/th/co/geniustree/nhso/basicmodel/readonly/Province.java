/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.basicmodel.readonly;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
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
@Table(name = "PROVINCE")
@NamedQueries({
    @NamedQuery(name = Province.FIND_ALL, query = "SELECT p FROM Province p order by p.name asc")})
@XmlAccessorType(XmlAccessType.FIELD)
@Cacheable
public class Province implements Serializable {

    private static final long serialVersionUID = 1L;
    public static final String FIND_ALL = "Province.findAll";
    @Id
    @Basic(optional = false)
    @Column(name = "PROVINCE_ID")
    private String id;
    @Column(name = "PROVINCE_NAME")
    private String name;
    @ManyToOne
    @JoinColumn(name = "NHSO_ZONE")
    private Zone nhsoZone;
    @XmlTransient
    @OneToMany(mappedBy = "province")
    private List<Hospital> hospitals;

    public Province() {
    }

    public Province(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Zone getNhsoZone() {
        return nhsoZone;
    }

    public void setNhsoZone(Zone nhsoZone) {
        this.nhsoZone = nhsoZone;
    }

    public List<Hospital> getHospitals() {
        return hospitals;
    }

    public void setHospitals(List<Hospital> hospitals) {
        this.hospitals = hospitals;
    }
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Province)) {
            return false;
        }
        Province other = (Province) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    public String getFullName() {
        if(id == null){
            return name;
        }
        
        return id + " - " + name;
    }
}
