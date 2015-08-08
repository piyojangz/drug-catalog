/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.service;

import java.util.List;
import org.springframework.data.domain.Sort;
import th.co.geniustree.nhso.basicmodel.readonly.Province;

/**
 *
 * @author Thanthathon
 */
public interface ProvinceService {

    public List<Province> findBySelectedZone(String zoneId, Sort sortBy);
    
}
