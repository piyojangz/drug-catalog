/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import th.co.geniustree.nhso.basicmodel.readonly.Province;
import th.co.geniustree.nhso.basicmodel.readonly.Zone;
import th.co.geniustree.nhso.basicmodel.repository.ProvinceRepository;
import th.co.geniustree.nhso.basicmodel.repository.ZoneRepository;
import th.co.geniustree.nhso.drugcatalog.service.NhsoZoneService;

/**
 *
 * @author moth
 */
@Service("nhsoZoneService")
public class NhsoZoneServiceImpl implements NhsoZoneService {

    @Autowired
    private ZoneRepository zoneRepo;
    @Autowired
    private ProvinceRepository provinceRepository;

    @Override
    public Zone findZoneByOrgId(String orgId) {
        if (orgId.trim().length() == 1) {
            orgId = "0" + orgId;
        }
        Zone zone = zoneRepo.findByNhsoZone(orgId);
        return zone;
    }

    @Override
    public List<Zone> findAll() {
        return zoneRepo.findAll(new Sort(Sort.Direction.ASC, "nhsoZone"));
    }

    @Override
    public List<Province> findByNhsoZone(String zone, Sort sort) {
        return provinceRepository.findByNhsoZone(zone, sort);
    }

}
