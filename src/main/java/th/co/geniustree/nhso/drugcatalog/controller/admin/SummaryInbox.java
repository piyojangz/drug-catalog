/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.controller.admin;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import th.co.geniustree.nhso.basicmodel.readonly.Hospital;
import th.co.geniustree.nhso.basicmodel.readonly.Province;
import th.co.geniustree.nhso.basicmodel.readonly.Zone;
import th.co.geniustree.nhso.basicmodel.repository.HospitalRepository;
import th.co.geniustree.nhso.basicmodel.repository.ProvinceRepository;
import th.co.geniustree.nhso.basicmodel.repository.ZoneRepository;
import th.co.geniustree.nhso.drugcatalog.controller.SpringDataLazyDataModelSupport;
import th.co.geniustree.nhso.drugcatalog.model.RequestItem;
import th.co.geniustree.nhso.drugcatalog.repo.RequestItemRepo;
import th.co.geniustree.nhso.drugcatalog.service.RequestItemService;

/**
 *
 * @author Thanthathon
 */
@Component
@Scope("view")
public class SummaryInbox {

    private static final Logger LOG = LoggerFactory.getLogger(SummaryInbox.class);

    @Autowired
    private ZoneRepository zoneRepo;
    @Autowired
    private ProvinceRepository provinceRepo;
    @Autowired
    private HospitalRepository hospitalRepo;
    @Autowired
    private RequestItemRepo requestItemRepo;
    private RequestItemService requestItemService;

    private List<Zone> zones;
    private List<Province> provinces;
    private SpringDataLazyDataModelSupport<SummaryRequest> summary;

    private String selectedZone;
    private String selectedProvince;
    private Hospital selectedHospital;

    private String totalRequestOfProvince;
    private String totalHospitalRequest;

    @PostConstruct
    public void postConstruct() {
        zones = zoneRepo.findAll(new Sort("nhsoZone"));
    }

    public void onSelectZone() {
        LOG.info("Selected Zone -> {}", selectedZone);
        provinces = provinceRepo.findByNhsoZone(selectedZone, new Sort("id"));
    }

    public void onSelectProvince() {
        summary = new SpringDataLazyDataModelSupport() {
            @Override
            public Page<SummaryRequest> load(Pageable pageAble) {
                List<SummaryRequest> summaryRequests = new ArrayList<>();
                long totalElement = 0;
                Page<Object[]> page = null;
                if (!selectedProvince.isEmpty()) {
                    page = requestItemRepo.countSummaryRequestByProvince(RequestItem.Status.REQUEST, selectedProvince, pageAble);
                    List<Object[]> list = page.getContent();
                    for (Object[] objArray : list) {
                        SummaryRequest summaryRequest = SummaryRequestMapper.mapToModel(objArray);
                        summaryRequests.add(summaryRequest);
                    }
                    totalHospitalRequest = Long.toString(page.getTotalElements());
                    totalElement =  page.getTotalElements();
                }
                Page<SummaryRequest> summary = new PageImpl<>(summaryRequests, pageAble,totalElement);
                return summary;
            }
        };
        totalRequestOfProvince = requestItemRepo.countTotalRequestByProvince(RequestItem.Status.REQUEST, selectedProvince).toString();

    }

    public void onSelectHospital() {

    }

    //****************************** getter and setter methods ******************************
    public String getTotalRequestOfProvince() {
        return totalRequestOfProvince;
    }

    public void setTotalRequestOfProvince(String totalRequestOfProvince) {
        this.totalRequestOfProvince = totalRequestOfProvince;
    }

    public String getTotalHospitalRequest() {
        return totalHospitalRequest;
    }

    public void setTotalHospitalRequest(String totalHospitalRequest) {
        this.totalHospitalRequest = totalHospitalRequest;
    }

    public List<Zone> getZones() {
        return zones;
    }

    public void setZones(List<Zone> zones) {
        this.zones = zones;
    }

    public List<Province> getProvinces() {
        return provinces;
    }

    public void setProvinces(List<Province> provinces) {
        this.provinces = provinces;
    }

    public SpringDataLazyDataModelSupport getHospitals() {
        return summary;
    }

    public void setHospitals(SpringDataLazyDataModelSupport summary) {
        this.summary = summary;
    }

    public String getSelectedZone() {
        return selectedZone;
    }

    public void setSelectedZone(String selectedZone) {
        this.selectedZone = selectedZone;
    }

    public String getSelectedProvince() {
        return selectedProvince;
    }

    public void setSelectedProvince(String selectedProvince) {
        this.selectedProvince = selectedProvince;
    }

    public Hospital getSelectedHospital() {
        return selectedHospital;
    }

    public void setSelectedHospital(Hospital selectedHospital) {
        this.selectedHospital = selectedHospital;
    }
    //****************************** getter and setter methods ******************************

}
