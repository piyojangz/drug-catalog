/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.service.impl;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import th.co.geniustree.nhso.drugcatalog.model.RequestItem;
import th.co.geniustree.nhso.drugcatalog.repo.RequestItemRepo;
import th.co.geniustree.nhso.drugcatalog.repo.UploadHospitalDrugItemRepo;
import th.co.geniustree.nhso.drugcatalog.service.ApproveService;
import th.co.geniustree.nhso.drugcatalog.service.AutoApproveService;
import th.co.geniustree.nhso.drugcatalog.service.UploadHospitalDrugService;

/**
 *
 * @author moth
 */
@Service
@Transactional(propagation = Propagation.REQUIRED)
public class AutoApproveServiceImpl implements AutoApproveService {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(AutoApproveServiceImpl.class);
    @Autowired
    private RequestItemRepo requestItemRepo;
    @Autowired
    private ApproveService approveService;
    @Autowired
    private UploadHospitalDrugItemRepo uploadHospitalDrugItemRepo;

    @Override
    public void approveRejectAndEditCountGreaterThanZero() {
        List<RequestItem> items = requestItemRepo.findByStatusAndEditCountGreaterThan(RequestItem.Status.REQUEST, 0);
        List<RequestItem> itemsThatExports = new ArrayList<>();
        for (RequestItem item : items) {
            long countTmtApproveFile = requestItemRepo.countTmtApproveFile(item.getId());
            if (countTmtApproveFile > 0) {
                itemsThatExports.add(item);
            }
            log.info("auto approve for request and editCount=0 ==>{}", itemsThatExports.size());
            approveService.approve(itemsThatExports);
        }
        uploadHospitalDrugItemRepo.copyDataProcedure();
    }

    @Override
    public void approveRequestWhichTMTisNull() {
        List<RequestItem> items = requestItemRepo.findByStatusAndUploadDrugItemTmtIdIsNull(RequestItem.Status.REQUEST);
        log.info("auto approve for request and tmt is null ==>{}", items.size());
        approveService.approve(items);
        uploadHospitalDrugItemRepo.copyDataProcedure();
        log.info("auto approve for request and tmt is null ==>{} completed", items.size());
    }

    @Override
    public void approveRequestWhichCreateOneline() {
        List<RequestItem> items = requestItemRepo.findByStatusAndUploadDrugItemUploadDrugShaHex(RequestItem.Status.REQUEST, UploadHospitalDrugService.SPECIAL_SHAHEX_VALUE);
        log.info("auto approve for request and that creat ONLINE ==>{}", items.size());
        approveService.approve(items);
        uploadHospitalDrugItemRepo.copyDataProcedure();
        log.info("auto approve for request and that creat ONLINE ==>{} completed", items.size());
    }

    @Override
    public void approveByHcode(String hcode) {
        List<RequestItem> items = requestItemRepo.findByStatusAndUploadDrugItemUploadDrugHcode(RequestItem.Status.REQUEST, hcode);
        log.info("auto approve request for HCODE size ==>{}", items.size());
        approveService.approve(items);
        uploadHospitalDrugItemRepo.copyDataProcedure();
        log.info("auto approve request for HCODE size ==>{} completed", items.size());
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public boolean approvePartial(int page, int pageSize) {
        Page<RequestItem> items = requestItemRepo.findByStatusAndDeletedIsFalse(RequestItem.Status.ACCEPT, new PageRequest(page, pageSize, Sort.Direction.ASC, "id"));
        approveService.reApproveAndNotChangeRequestItemState(items.getContent());
        return items.hasNext();
    }

    @Override
    public void approveRequestFlag(String flag) {
        List<RequestItem> items = requestItemRepo.findAllByFlag(RequestItem.Status.REQUEST, flag);
        log.info("auto approve for request and flag \'{}\' ==> {} items", flag, items.size());
        approveService.approve(items);
    }
    
    @Override
    public void approveSelectedFlagBySystem(String flag){
        List<RequestItem> items = requestItemRepo.findAllByFlag(RequestItem.Status.REQUEST, flag);
        log.info("auto approve for request and flag \'{}\' ==> {} items", flag, items.size());
        approveService.approveBySystem(items);
    }
    
    
}
