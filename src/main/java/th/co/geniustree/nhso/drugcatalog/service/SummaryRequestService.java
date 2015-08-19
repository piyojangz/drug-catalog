/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import th.co.geniustree.nhso.drugcatalog.controller.admin.SummaryRequest;

/**
 *
 * @author Thanthathon
 */
public interface SummaryRequestService {
    public Page<SummaryRequest> loadSummaryRequest(String selectedZone , String selectedProvince,Pageable pageable);
    
    public Long getTotalReqeust();
    
}
