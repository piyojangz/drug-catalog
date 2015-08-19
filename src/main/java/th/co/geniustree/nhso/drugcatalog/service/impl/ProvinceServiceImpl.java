/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.service.impl;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import th.co.geniustree.nhso.basicmodel.readonly.Province;
import th.co.geniustree.nhso.basicmodel.repository.ProvinceRepository;
import th.co.geniustree.nhso.drugcatalog.controller.admin.SummaryRequest;
import th.co.geniustree.nhso.drugcatalog.service.ProvinceService;

/**
 *
 * @author Thanthathon
 */
@Service
@Transactional(propagation = Propagation.REQUIRED)
public class ProvinceServiceImpl implements ProvinceService {

    @Autowired
    private ProvinceRepository provinceRepo;

    @Override
    public List<Province> findBySelectedZone(String zoneId, Sort sortBy) {
        List<Province> provinces = null;
        if (zoneId.equals(SummaryRequest.ALL_ZONE)) {
            List<String> notZone = new ArrayList<>();
            notZone.add("15");
            provinces = provinceRepo.findByNhsoZoneNhsoZoneNotIn(notZone, sortBy);
        } else {
            provinces = provinceRepo.findByNhsoZone(zoneId, sortBy);
        }

        return provinces;
    }

}
