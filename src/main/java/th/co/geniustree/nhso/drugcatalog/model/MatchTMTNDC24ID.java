/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.model;

import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author Thanthathon
 */
public class MatchTMTNDC24ID implements Serializable{
    
    private String tmtid;
    
    private String ndc24;

    public String getTmtid() {
        return tmtid;
    }

    public void setTmtid(String tmtid) {
        this.tmtid = tmtid;
    }

    public String getNdc24() {
        return ndc24;
    }

    public void setNdc24(String ndc24) {
        this.ndc24 = ndc24;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 67 * hash + Objects.hashCode(this.tmtid);
        hash = 67 * hash + Objects.hashCode(this.ndc24);
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
        final MatchTMTNDC24ID other = (MatchTMTNDC24ID) obj;
        if (!Objects.equals(this.tmtid, other.tmtid)) {
            return false;
        }
        if (!Objects.equals(this.ndc24, other.ndc24)) {
            return false;
        }
        return true;
    }
    
    
    
}
