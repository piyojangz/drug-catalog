/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.schedule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import th.co.geniustree.nhso.drugcatalog.service.AutoApproveService;

/**
 *
 * @author Thanthathon
 */
@Service
public class AutoApproveScheduler implements Processor{

    @Autowired
    private AutoApproveService autoApproveService;
    
    @Scheduled(cron = " 0 30 23 * * ?")
    @Override
    public void process() {
        autoApproveService.approveSelectedFlagBySystem("U");
        autoApproveService.approveSelectedFlagBySystem("D");
    }
    
}