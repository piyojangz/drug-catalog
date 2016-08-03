/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import th.co.geniustree.nhso.drugcatalog.input.DrugAndGroup;
import th.co.geniustree.nhso.drugcatalog.model.DrugGroup;
import th.co.geniustree.nhso.drugcatalog.model.TMTDrug;
import th.co.geniustree.nhso.drugcatalog.model.TMTDrugGroupItem;
import th.co.geniustree.nhso.drugcatalog.model.TMTDrugGroupItemPK;
import th.co.geniustree.nhso.drugcatalog.repo.DrugGroupRepo;
import th.co.geniustree.nhso.drugcatalog.repo.TMTDrugGroupItemRepo;
import th.co.geniustree.nhso.drugcatalog.repo.TMTDrugRepo;
import th.co.geniustree.nhso.drugcatalog.service.DeletedLogService;
import th.co.geniustree.nhso.drugcatalog.service.TMTDrugGroupItemService;

/**
 *
 * @author moth
 */
@Service
public class TMTDrugGroupItemServiceImpl implements TMTDrugGroupItemService {

    @Autowired
    private TMTDrugGroupItemRepo tMTDrugGroupItemRepo;
    @Autowired
    private DrugGroupRepo drugGroupRepo;
    @Autowired
    private TMTDrugRepo tMTDrugRepo;

    @Autowired
    @Qualifier("TMTDrugGroupItemDeletedLogServiceImpl")
    private DeletedLogService deletedLogService;

    @Override
    public void validate(DrugAndGroup drugAndGroup) {
        TMTDrugGroupItem findOne = tMTDrugGroupItemRepo.findOne(new TMTDrugGroupItemPK(drugAndGroup.getTmtId(), drugAndGroup.getDrugGroup(), drugAndGroup.getDateInDate()));
        if (findOne != null && drugAndGroup.getDateOutDate() == null) {
            drugAndGroup.addError("drugGroup", "มีการเพิ่ม Drug group นี้แล้ว.");
        }
    }

    @Override
    public void save(List<DrugAndGroup> passModels) {
        for (DrugAndGroup drugAndGrooup : passModels) {
            TMTDrugGroupItem findOne = tMTDrugGroupItemRepo.findOne(new TMTDrugGroupItemPK(drugAndGrooup.getTmtId(), drugAndGrooup.getDrugGroup(), drugAndGrooup.getDateInDate()));
            if (findOne != null) {
                findOne.setDateOut(drugAndGrooup.getDateOutDate());
                tMTDrugGroupItemRepo.save(findOne);
            } else {
                TMTDrug tmtDrug = tMTDrugRepo.findOne(drugAndGrooup.getTmtId());
                DrugGroup drugGroup = drugGroupRepo.findOne(drugAndGrooup.getDrugGroup());
                TMTDrugGroupItem druggroupItem = new TMTDrugGroupItem(tmtDrug, drugGroup, drugAndGrooup.getDateInDate());
                druggroupItem.setDateOut(drugAndGrooup.getDateOutDate());
                tMTDrugGroupItemRepo.save(druggroupItem);

            }
        }
    }

    @Override
    public void delete(TMTDrugGroupItem drugGroupItem) {
        tMTDrugGroupItemRepo.delete(drugGroupItem);
        deletedLogService.createLog(drugGroupItem);
    }

}
