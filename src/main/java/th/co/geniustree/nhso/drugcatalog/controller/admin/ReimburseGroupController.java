/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.controller.admin;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import th.co.geniustree.nhso.drugcatalog.controller.SpringDataLazyDataModelSupport;
import th.co.geniustree.nhso.drugcatalog.controller.utils.FacesMessageUtils;
import th.co.geniustree.nhso.drugcatalog.model.ReimburseGroup;
import th.co.geniustree.nhso.drugcatalog.service.ReimburseGroupService;

/**
 *
 * @author Thanthathon
 */
@Component
@Scope("view")
public class ReimburseGroupController implements Serializable {

    private static final Logger LOG = LoggerFactory.getLogger(ReimburseGroupController.class);

    @Autowired
    private ReimburseGroupService reimburseGroupService;

    private SpringDataLazyDataModelSupport<ReimburseGroup> reimburseGroups;
    private ReimburseGroup selectedReimburseGroup;

    private String reimburseGroupCode;
    private String reimburseGroupDescription;
    private boolean specialProject;
    private String keyword;

    @PostConstruct
    public void postConstruct() {
        findAll();
    }

    public void save() {
        try {
            reimburseGroupService.save(reimburseGroupCode, reimburseGroupDescription, specialProject);
            FacesMessageUtils.info("บันทึกข้อมูล สำเร็จ");
            reimburseGroupCode = "";
            reimburseGroupDescription = "";
        } catch (Exception e) {
            FacesMessageUtils.error("ไม่สามารถบันทึกข้อมูลได้ อาจเป็นเพราะมีข้อมูล " + reimburseGroupCode + " อยู่แล้ว");
        }
    }

    public void onSelect(ReimburseGroup selectedGroup) {
        LOG.debug("selected dosageFormGroup -> {}", selectedGroup.getId());
        selectedReimburseGroup = selectedGroup;
    }

    public void edit() {
        try {
            reimburseGroupService.edit(selectedReimburseGroup);
            FacesMessageUtils.info("แก้ไขข้อมูล สำเร็จ");
        } catch (Exception e) {
            FacesMessageUtils.error("ไม่สามารถแก้ไขข้อมูลได้");
        }
    }

    public void delete() {
        try {
            reimburseGroupService.delete(selectedReimburseGroup);
            FacesMessageUtils.info("ลบข้อมูล สำเร็จ");
        } catch (Exception e) {
            FacesMessageUtils.error("ไม่สามารถลบข้อมูลได้");
        }
    }

    public void search() {
        reimburseGroups = new SpringDataLazyDataModelSupport<ReimburseGroup>() {
            @Override
            public Page<ReimburseGroup> load(Pageable pageAble) {
                Page<ReimburseGroup> page = reimburseGroupService.search(keyword, specialProject, pageAble);
                return page;
            }
        };
    }

    private void findAll() {
        reimburseGroups = new SpringDataLazyDataModelSupport<ReimburseGroup>() {
            @Override
            public Page<ReimburseGroup> load(Pageable pageAble) {
                Page<ReimburseGroup> page = reimburseGroupService.findAllPaging(specialProject, pageAble);
                return page;
            }
        };
    }

    public ReimburseGroup getSelectedReimburseGroup() {
        return selectedReimburseGroup;
    }

    public void setSelectedReimburseGroup(ReimburseGroup selectedReimburseGroup) {
        this.selectedReimburseGroup = selectedReimburseGroup;
    }

    public String getReimburseGroupCode() {
        return reimburseGroupCode;
    }

    public void setReimburseGroupCode(String reimburseGroupCode) {
        this.reimburseGroupCode = reimburseGroupCode;
    }

    public String getReimburseGroupDescription() {
        return reimburseGroupDescription;
    }

    public void setReimburseGroupDescription(String reimburseGroupDescription) {
        this.reimburseGroupDescription = reimburseGroupDescription;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public SpringDataLazyDataModelSupport<ReimburseGroup> getReimburseGroups() {
        return reimburseGroups;
    }

    public void setReimburseGroups(SpringDataLazyDataModelSupport<ReimburseGroup> reimburseGroups) {
        this.reimburseGroups = reimburseGroups;
    }

    public boolean isSpecialProject() {
        return specialProject;
    }

    public void setSpecialProject(boolean specialProject) {
        this.specialProject = specialProject;
    }

}
