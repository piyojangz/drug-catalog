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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import th.co.geniustree.nhso.drugcatalog.controller.admin.SummaryRequest;
import th.co.geniustree.nhso.drugcatalog.controller.admin.SummaryRequestMapper;
import th.co.geniustree.nhso.drugcatalog.model.RequestItem;
import th.co.geniustree.nhso.drugcatalog.repo.RequestItemRepo;
import th.co.geniustree.nhso.drugcatalog.service.SummaryRequestService;

/**
 *
 * @author Thanthathon
 */
@Service
@Transactional(propagation = Propagation.REQUIRED)
public class SummaryRequestServiceImpl implements SummaryRequestService {

    private static final Logger LOG = LoggerFactory.getLogger(SummaryRequestServiceImpl.class);

    @Autowired
    private RequestItemRepo requestItemRepo;

    private Long totalRequest;

    @Override
    public Page<SummaryRequest> loadSummaryRequest(String selectedZone, String selectedProvince, Pageable pageable) {
        List<SummaryRequest> summaryRequests = null;
        Page<Object[]> objPage = null;

        if (selectedProvince.equals(SummaryRequest.ALL_PROVINCE)) {
            if (selectedZone.equals(SummaryRequest.ALL_ZONE)) {
                objPage = requestItemRepo.countSummaryRequestAll(pageable);
                totalRequest = requestItemRepo.countTotalRequestAll();
            } else {
                objPage = requestItemRepo.countSummaryRequestByZone(selectedZone, pageable);
                totalRequest = requestItemRepo.countTotalRequestByZone(selectedZone);
            }
        } else {
            objPage = requestItemRepo.countSummaryRequestByProvince(selectedProvince, pageable);
            totalRequest = requestItemRepo.countTotalRequestByProvince(selectedProvince);
        }
        if (totalRequest == null) {
            //fixed NullPointerException
            totalRequest = 0l;
        }
        summaryRequests = SummaryRequestMapper.mapAllToModel(objPage.getContent());
        Page<SummaryRequest> summary = new PageImpl<>(summaryRequests, pageable, objPage.getTotalElements());
        return summary;
    }

    @Override
    public Long getTotalReqeust() {
        return totalRequest;
    }

}
