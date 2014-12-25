/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.controller.admin;

import java.io.Serializable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import th.co.geniustree.nhso.drugcatalog.service.AutoApproveService;

/**
 *
 * @author moth
 */
@Component
@Scope("view")
public class AutoApprove implements Serializable {

    @Autowired
    private AutoApproveService autoApproveService;

    public void approveRejectAndEditCountGreaterThanZero() {
        autoApproveService.approveRejectAndEditCountGreaterThanZero();
    }

    public void approveRequestWhichTMTisNull() {
        autoApproveService.approveRequestWhichTMTisNull();
    }    
    public void approveRequestWhichCreateOneline(){
        autoApproveService.approveRequestWhichCreateOneline();
    }
}
