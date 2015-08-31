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
import th.co.geniustree.nhso.drugcatalog.model.DosageFormGroup;
import th.co.geniustree.nhso.drugcatalog.service.DosageFormGroupService;

/**
 *
 * @author Thanthathon
 */
@Component
@Scope("view")
public class DosageFormGroupController implements Serializable {

    private static final Logger LOG = LoggerFactory.getLogger(DosageFormGroupController.class);

    @Autowired
    private DosageFormGroupService dosageFormGroupService;

    private SpringDataLazyDataModelSupport<DosageFormGroup> dosageFormGroups;
    private DosageFormGroup selectedDosageFormGroup;

    private String dfgId;
    private String dfgDescription;

    private String keyword;

    @PostConstruct
    public void postConstruct() {
        findAll();
    }

    public void save() {
        try {
            dosageFormGroupService.save(dfgId.toLowerCase(), dfgDescription);
            FacesMessageUtils.info("บันทึกข้อมูล สำเร็จ");
        } catch (Exception e) {
            FacesMessageUtils.error("ไม่สามารถบันทึกข้อมูลได้ อาจเป็นเพราะมีข้อมูล " + dfgId + " อยู่แล้ว");
        }
    }

    public void onSelect(DosageFormGroup selectedGroup) {
        LOG.debug("selected dosageFormGroup -> {}", selectedGroup.getIdGroup());
        selectedDosageFormGroup = selectedGroup;
    }

    public void edit() {
        try {
            dosageFormGroupService.edit(selectedDosageFormGroup);
            FacesMessageUtils.info("แก้ไขข้อมูล สำเร็จ");
        } catch (Exception e) {
            FacesMessageUtils.error("ไม่สามารถแก้ไขข้อมูลได้");
        }
    }

    public void delete() {
        try {
            dosageFormGroupService.delete(selectedDosageFormGroup);
            FacesMessageUtils.info("ลบข้อมูล สำเร็จ");
        } catch (Exception e) {
            FacesMessageUtils.error("ไม่สามารถลบข้อมูลได้ อาจเป็นเพราะข้อมูลนี้ ถูกใช้งานอยู่");
        }
    }

    public void search() {
        dosageFormGroups = new SpringDataLazyDataModelSupport<DosageFormGroup>() {
            @Override
            public Page<DosageFormGroup> load(Pageable pageAble) {
                Page<DosageFormGroup> page = dosageFormGroupService.search(keyword, pageAble);
                return page;
            }
        };
    }

    private void findAll() {
        dosageFormGroups = new SpringDataLazyDataModelSupport<DosageFormGroup>() {
            @Override
            public Page<DosageFormGroup> load(Pageable pageAble) {
                Page<DosageFormGroup> page = dosageFormGroupService.findAll(pageAble);
                return page;
            }
        };
    }

//    ************************** getter and setter method **************************
    public SpringDataLazyDataModelSupport<DosageFormGroup> getDosageFormGroups() {
        return dosageFormGroups;
    }

    public void setDosageFormGroups(SpringDataLazyDataModelSupport<DosageFormGroup> dosageFormGroups) {
        this.dosageFormGroups = dosageFormGroups;
    }

    public DosageFormGroup getSelectedDosageFormGroup() {
        return selectedDosageFormGroup;
    }

    public void setSelectedDosageFormGroup(DosageFormGroup selectedDosageFormGroup) {
        this.selectedDosageFormGroup = selectedDosageFormGroup;
    }

    public String getDfgId() {
        return dfgId;
    }

    public void setDfgId(String dfgId) {
        this.dfgId = dfgId;
    }

    public String getDfgDescription() {
        return dfgDescription;
    }

    public void setDfgDescription(String dfgDescription) {
        this.dfgDescription = dfgDescription;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

}
