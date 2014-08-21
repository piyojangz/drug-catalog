/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.controller.report;

import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import th.co.geniustree.nhso.basicmodel.readonly.Hospital;
import th.co.geniustree.nhso.basicmodel.readonly.Province;
import th.co.geniustree.nhso.basicmodel.readonly.Zone;
import th.co.geniustree.nhso.drugcatalog.repo.UploadHospitalDrugRepo;
import th.co.geniustree.nhso.drugcatalog.service.NhsoZoneService;

/**
 *
 * @author moth
 */
public class ReportConditionBase implements Serializable {

    @Autowired
    private NhsoZoneService nhsoZoneService;
    @Autowired
    private UploadHospitalDrugRepo uploadHospitalDrugRepo;
    private List<Zone> zones;
    private List<Province> provinces;
    private List<Hospital> hospitals;
    private String selectZone;
    private String selectProvince;
    private String selectHospital;

    @PostConstruct
    public void postConstruct() {
        zones = nhsoZoneService.findAll();
        Zone getOut14 = new Zone();
        getOut14.setNhsoZone("14");
        zones.remove(getOut14);
        Zone getOut15 = new Zone();
        getOut15.setNhsoZone("15");
        zones.remove(getOut15);
    }

    public List<Zone> getZones() {
        return zones;
    }

    public void setZones(List<Zone> zones) {
        this.zones = zones;
    }

    public String getSelectZone() {
        return selectZone;
    }

    public void setSelectZone(String selectZone) {
        this.selectZone = selectZone;
        if (selectZone != null) {
            this.provinces = nhsoZoneService.findByNhsoZone(selectZone, new Sort(Sort.Direction.ASC, "name"));
        }
        this.hospitals = null;
    }

    public String getSelectProvince() {
        return selectProvince;
    }

    public void setSelectProvince(String selectProvince) {
        this.selectProvince = selectProvince;
        if (selectProvince != null) {
            this.hospitals = uploadHospitalDrugRepo.findAllHospitalInProvince(selectProvince);
        } else {
            this.hospitals = null;
        }
    }

    public List<Province> getProvinces() {
        return provinces;
    }

    public void setProvinces(List<Province> provinces) {
        this.provinces = provinces;
    }

    public List<Hospital> getHospitals() {
        return hospitals;
    }

    public void setHospitals(List<Hospital> hospitals) {
        this.hospitals = hospitals;
    }

    public NhsoZoneService getNhsoZoneService() {
        return nhsoZoneService;
    }

    public void setNhsoZoneService(NhsoZoneService nhsoZoneService) {
        this.nhsoZoneService = nhsoZoneService;
    }

    public String getSelectHospital() {
        return selectHospital;
    }

    public void setSelectHospital(String selectHospital) {
        this.selectHospital = selectHospital;
    }


    public void reset() {
        selectZone = null;
        selectProvince = null;
        setSelectZone(null);
    }
}
