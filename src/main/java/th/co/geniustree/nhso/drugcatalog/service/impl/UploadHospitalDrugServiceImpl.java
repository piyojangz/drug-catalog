/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.service.impl;

import java.util.List;
import org.springframework.beans.BeanUtils;
import static org.springframework.beans.BeanUtils.copyProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import th.co.geniustree.nhso.drugcatalog.authen.SecurityUtil;
import th.co.geniustree.nhso.drugcatalog.model.UploadHospitalDrug;
import th.co.geniustree.nhso.drugcatalog.model.UploadHospitalDrugItem;
import th.co.geniustree.nhso.drugcatalog.model.request.RequestItem;
import th.co.geniustree.nhso.drugcatalog.model.tmt.HospitalTMTDrug;
import th.co.geniustree.nhso.drugcatalog.repo.HospitalTMTDrugRepo;
import th.co.geniustree.nhso.drugcatalog.repo.RequestItemRepo;
import th.co.geniustree.nhso.drugcatalog.repo.UploadHospitalDrugRepo;
import th.co.geniustree.nhso.drugcatalog.service.UploadHospitalDrugService;

/**
 *
 * @author moth
 */
@Service
public class UploadHospitalDrugServiceImpl implements UploadHospitalDrugService {

    @Autowired
    private UploadHospitalDrugRepo uploadHospitalDrugRepo;
    @Autowired
    private RequestItemRepo requestItemRepo;
    @Autowired
    private HospitalTMTDrugRepo hospitalTMTDrugRepo;

    @Override
    public void saveUploadHospitalDrugAndRequest(UploadHospitalDrug uploadHospitalDrugs) {
        uploadHospitalDrugs = uploadHospitalDrugRepo.save(uploadHospitalDrugs);
        List<UploadHospitalDrugItem> passItems = uploadHospitalDrugs.getPassItems();
        for (UploadHospitalDrugItem uploadItem : passItems) {
            Integer countByHospDrugCodeAndHcode = hospitalTMTDrugRepo.countByHospDrugCodeAndHcode(uploadItem.getHospDrugCode(), uploadHospitalDrugs.getHcode());
            if (countByHospDrugCodeAndHcode == 0) {
                //create HospitalTMTDrug
                HospitalTMTDrug drug = new HospitalTMTDrug();
                BeanUtils.copyProperties(uploadItem, drug);
                drug.setHcode(uploadItem.getHospDrugCode());
                hospitalTMTDrugRepo.save(drug);
                //create Request
                if (drug.getTmtId() != null) {
                    RequestItem requestItem = new RequestItem();
                    requestItem.setHcode(uploadItem.getHospDrugCode());
                    requestItem.setRequestItem(uploadItem);
                    requestItem.setTargetItem(drug);
                    requestItem.setStatus(RequestItem.Status.REQUEST);
                    requestItem.setRequestUser(SecurityUtil.getUserDetails().getStaffName());
                    requestItem = requestItemRepo.save(requestItem);
                }
            }
        }
    }

}
