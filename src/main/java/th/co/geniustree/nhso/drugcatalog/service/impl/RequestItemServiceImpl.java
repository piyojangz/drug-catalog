/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.service.impl;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import th.co.geniustree.nhso.drugcatalog.authen.SecurityUtil;
import th.co.geniustree.nhso.drugcatalog.model.RequestItem;
import th.co.geniustree.nhso.drugcatalog.model.UploadHospitalDrugItem;
import th.co.geniustree.nhso.drugcatalog.repo.RequestItemRepo;
import th.co.geniustree.nhso.drugcatalog.repo.UploadHospitalDrugItemRepo;
import th.co.geniustree.nhso.drugcatalog.service.RequestItemService;

/**
 *
 * @author moth
 */
@Service
@Transactional(propagation = Propagation.REQUIRED)
public class RequestItemServiceImpl implements RequestItemService {

    private static final Logger LOG = LoggerFactory.getLogger(RequestItemServiceImpl.class);

    @Autowired
    private RequestItemRepo requestItemRepo;

    @Autowired
    private UploadHospitalDrugItemRepo uploadHospitalDrugItemRepo;

    @Override
    public void generateRequest(UploadHospitalDrugItem uploadItem) {
        RequestItem requestItem = uploadHospitalDrugItemRepo.findRejectItem(uploadItem.getHospDrugCode(), uploadItem.getUploadDrug().getHcode(), uploadItem.getTmtId(), uploadItem.getDateEffective(), uploadItem.getUpdateFlag());
        if (requestItem != null) {
            requestItemRepo.delete(requestItem);
        }
        requestItem = new RequestItem();
        requestItem.setStatus(RequestItem.Status.REQUEST);
        requestItem.setRequestUser(SecurityUtil.getUserDetails().getPid());
        requestItem.setUploadDrugItem(uploadItem);
        requestItem = requestItemRepo.save(requestItem);
        uploadItem.setRequestItem(requestItem);
    }

    @Override
    public void generateAll() {
        Pageable pageRquest = new PageRequest(0, 1000);
        Page<UploadHospitalDrugItem> findAll;
        while (true) {
            findAll = uploadHospitalDrugItemRepo.findAll(pageRquest);
            List<UploadHospitalDrugItem> contents = findAll.getContent();
            for (UploadHospitalDrugItem uploadItem : contents) {
                if (uploadItem.getRequestItem() != null) {
                    continue;
                }
                RequestItem requestItem = new RequestItem();
                requestItem.setStatus(RequestItem.Status.REQUEST);
                requestItem.setRequestUser(SecurityUtil.getUserDetails().getPid());
                requestItem.setUploadDrugItem(uploadItem);
                requestItem.setRequestUser(SecurityUtil.getUserDetails().getPid());
                requestItem.setRequestDate(uploadItem.getUploadDrug().getCreateDate());
                requestItem = requestItemRepo.save(requestItem);
                uploadItem.setRequestItem(requestItem);
            }
            uploadHospitalDrugItemRepo.flush();
            if (findAll.hasNext()) {
                pageRquest = findAll.nextPageable();
            } else {
                break;
            }
        }
    }

    @Override
    public List<RequestItem> findAllByStatusAndTmtId(RequestItem.Status status, String tmtId) {
        return requestItemRepo.findAllByStatusAndTmtId(status, tmtId);
    }

    @Override
    public Page<RequestItem> findByStatusAndHcodeAndTmtIdIsNull(RequestItem.Status status, String hcode, Pageable pageable) {
        return requestItemRepo.findByStatusAndUploadDrugItemUploadDrugHcodeAndUploadDrugItemTmtIdAndDeletedFalse(status, hcode, "NULLID", pageable);
    }
}
