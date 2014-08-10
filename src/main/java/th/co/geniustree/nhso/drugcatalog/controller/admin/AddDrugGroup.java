/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.controller.admin;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import th.co.geniustree.nhso.drugcatalog.controller.SpringDataLazyDataModelSupport;
import th.co.geniustree.nhso.drugcatalog.controller.utils.FacesMessageUtils;
import th.co.geniustree.nhso.drugcatalog.model.DrugGroup;
import th.co.geniustree.nhso.drugcatalog.repo.DrugGroupRepo;
import th.co.geniustree.nhso.drugcatalog.repo.TMTDrugGroupItemRepo;

/**
 *
 * @author moth
 */
@Component
@Scope("view")
public class AddDrugGroup implements Serializable {

    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(AddDrugGroup.class);
    private DrugGroup drugGroup = new DrugGroup();
    private SpringDataLazyDataModelSupport<DrugGroup> druggroups;
    @Autowired
    private DrugGroupRepo drugGroupRepo;
    @Autowired
    private TMTDrugGroupItemRepo tmtDrugGroupItemRepo;
    private boolean editMode;

    public DrugGroup getDrugGroup() {
        return drugGroup;
    }

    public SpringDataLazyDataModelSupport<DrugGroup> getDruggroups() {
        return druggroups;
    }

    public boolean isEditMode() {
        return editMode;
    }

    @PostConstruct
    public void postConstruct() {
        druggroups = new SpringDataLazyDataModelSupport<DrugGroup>(new Sort(Sort.Direction.DESC, "createDate")) {

            @Override
            public Page<DrugGroup> load(Pageable pageAble) {
                return drugGroupRepo.findAll(pageAble);
            }

        };
    }

    public void add() {
        if (!editMode) {
            DrugGroup findOne = drugGroupRepo.findOne(drugGroup.getId());
            if (findOne != null) {
                FacesMessageUtils.error("มี drug group '" + drugGroup.getId() + "' แล้ว");
                return;
            }
        }
        drugGroupRepo.save(drugGroup);
        clear();
        FacesMessageUtils.info("Save  completed.");
    }

    public void clear() {
        drugGroup = new DrugGroup();
        editMode = false;
    }

    public void delete(String id) {
        LOG.info("prepare to delete drug group id {}", id);
        long count = tmtDrugGroupItemRepo.countByDrugGroupId(drugGroup.getId());
        if (count > 0) {
            FacesMessageUtils.info("ไม่สามารถลบได้ เนื่องจากมีการใช้ Drug group นี้ไปแล้ว");
        } else {
            drugGroupRepo.delete(id);
            FacesMessageUtils.info("ลบเสร็จสิ้น");
            LOG.info("Deleted drug group {}", id);
        }

    }

    public void edit(String id) {
        LOG.info("prepare to edit drug group id {}", id);
        drugGroup = drugGroupRepo.findOne(id);
        editMode = true;
    }
}
