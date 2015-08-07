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
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import th.co.geniustree.nhso.basicmodel.readonly.Hospital;
import th.co.geniustree.nhso.basicmodel.readonly.Province;
import th.co.geniustree.nhso.basicmodel.readonly.Zone;
import th.co.geniustree.nhso.basicmodel.repository.ZoneRepository;
import th.co.geniustree.nhso.drugcatalog.controller.SpringDataLazyDataModelSupport;
import th.co.geniustree.nhso.drugcatalog.repo.RequestItemRepo;
import th.co.geniustree.nhso.drugcatalog.service.ProvinceService;
import th.co.geniustree.nhso.drugcatalog.service.SummaryRequestService;

/**
 *
 * @author Thanthathon
 */
@Component
@Scope("view")
public class SummaryInbox {

    private static final Logger LOG = LoggerFactory.getLogger(SummaryInbox.class);

    @Autowired
    private SummaryRequestService summaryRequestService;
    
    @Autowired
    private ZoneRepository zoneRepo;
    @Autowired
    private ProvinceService provinceService;

    private List<Zone> zones;
    private List<Province> provinces;
    private SpringDataLazyDataModelSupport<SummaryRequest> summary = null;

    private String selectedZone;
    private String selectedProvince;
    private Province province;
    private Hospital selectedHospital;

    private long totalRequestOfProvince;
    private long totalHospitalRequest;

    private boolean notEmptyRequest;
    private final String selectAllZone = SummaryRequest.ALL_ZONE;
    private final String selectAllProvince = SummaryRequest.ALL_PROVINCE;

    @PostConstruct
    public void postConstruct() {
        notEmptyRequest = false;
        initZone();
    }

    private void initZone() {
        zones = zoneRepo.findAll(new Sort("nhsoZone"));
        Zone deletedZone = new Zone();
        deletedZone.setNhsoZone("14");
        zones.remove(deletedZone);
        deletedZone.setNhsoZone("15");
        zones.remove(deletedZone);
        selectedZone = "";
    }

    public void onSelectZone() {
        LOG.info("Selected Zone -> {}", selectedZone);
        provinces = provinceService.findBySelectedZone(selectedZone, new Sort("name"));
        selectedProvince = "";
    }
    
    public void search(){
        summary = new SpringDataLazyDataModelSupport<SummaryRequest>() {
            @Override
            public Page<SummaryRequest> load(Pageable pageAble) {
                Page<SummaryRequest> summary = summaryRequestService.loadSummaryRequest(selectedZone, selectedProvince, pageAble);
                totalHospitalRequest = summary.getTotalElements();
                totalRequestOfProvince = summaryRequestService.getTotalReqeust();
                return summary;
            }
        };
    }

    public List<Province> completeProvince(String query) {
        if (provinces == null || provinces.isEmpty()) {
            provinces = provinceService.findBySelectedZone(selectedZone, new Sort("name"));
        }
        List<Province> filterProvince = new ArrayList<>();
        for (Province p : provinces) {
            if (p.getName() == null) {
                p.setName(" - ");
            }
            if (p.getFullName().toLowerCase().contains(query)) {
                filterProvince.add(p);
            }
        }
        Province p = new Province(selectAllProvince);
        p.setName("เลือกทุกจังหวัด");
        filterProvince.add(0, p);
        return filterProvince;
    }
    
    public void onSelectHospital() {

    }

    public boolean isNotEmptyRequest() {
        return notEmptyRequest;
    }

    public void setNotEmptyRequest(boolean notEmptyRequest) {
        this.notEmptyRequest = notEmptyRequest;
    }

    //****************************** getter and setter methods ******************************
    public long getTotalRequestOfProvince() {
        return totalRequestOfProvince;
    }

    public void setTotalRequestOfProvince(long totalRequestOfProvince) {
        this.totalRequestOfProvince = totalRequestOfProvince;
    }

    public long getTotalHospitalRequest() {
        return totalHospitalRequest;
    }

    public void setTotalHospitalRequest(long totalHospitalRequest) {
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

    public SpringDataLazyDataModelSupport<SummaryRequest> getSummary() {
        return summary;
    }

    public void setSummary(SpringDataLazyDataModelSupport<SummaryRequest> summary) {
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
    public String getSelectAllZone() {
        return selectAllZone;
    }

    public String getSelectAllProvince() {
        return selectAllProvince;
    }

    public Province getProvince() {
        return province;
    }

    public void setProvince(Province province) {
        this.province = province;
    }

    
}
