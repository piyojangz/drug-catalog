/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package th.co.geniustree.nhso.drugcatalog.service;

import java.util.List;
import org.springframework.data.domain.Sort;
import th.co.geniustree.nhso.basicmodel.readonly.Province;
import th.co.geniustree.nhso.basicmodel.readonly.Zone;

/**
 *
 * @author moth
 */
public interface NhsoZoneService {
    public Zone findZoneByOrgId(String orgId);
    public List<Zone> findAll();
    public List<Province> findByNhsoZone(String zone,Sort sort);
}
