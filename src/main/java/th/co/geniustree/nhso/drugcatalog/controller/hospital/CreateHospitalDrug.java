/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.controller.hospital;

import com.google.common.base.Strings;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import th.co.geniustree.nhso.drugcatalog.Constants;
import th.co.geniustree.nhso.drugcatalog.authen.SecurityUtil;
import th.co.geniustree.nhso.drugcatalog.controller.utils.DateUtils;
import th.co.geniustree.nhso.drugcatalog.controller.utils.FacesMessageUtils;
import th.co.geniustree.nhso.drugcatalog.model.HospitalDrug;
import th.co.geniustree.nhso.drugcatalog.model.HospitalDrugPK;
import th.co.geniustree.nhso.drugcatalog.model.TMTDrug;
import th.co.geniustree.nhso.drugcatalog.model.UploadHospitalDrugItem;
import th.co.geniustree.nhso.drugcatalog.repo.HospitalDrugRepo;
import th.co.geniustree.nhso.drugcatalog.repo.UploadHospitalDrugItemRepo;
import th.co.geniustree.nhso.drugcatalog.service.UploadHospitalDrugItemService;
import th.co.geniustree.nhso.drugcatalog.service.UploadHospitalDrugService;

/**
 *
 * @author moth
 */
@Component
@Scope("view")
public class CreateHospitalDrug implements Serializable {

    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(CreateHospitalDrug.class);
    private UploadHospitalDrugItem item;
    private boolean editMode = false;
    private String updateFlag = "A";
    private String hospDrugCode;

    @Autowired
    private HospitalDrugRepo hospitalDrugRepo;
    @Autowired
    private UploadHospitalDrugService uploadHospitalDrugService;
    @Autowired
    private UploadHospitalDrugItemRepo uploadItemRepo;
    @Autowired
    private UploadHospitalDrugItemService uploadHospitalDrugItemService;

    private HospitalDrug editHospitalDrug;
    private UploadHospitalDrugItem beforeEditHospitalDrug;
    private String oldUnitPrice;
    private Date oldDateEffective;
    private String oldEdStatus;
    private boolean disableSaveBtn = Boolean.FALSE;

    private List<UploadHospitalDrugItem> history;

    @PostConstruct
    public void postConstruct() {
        clear();
    }

    public String getOldEdStatus() {
        return oldEdStatus;
    }

    public void setOldEdStatus(String oldEdStatus) {
        this.oldEdStatus = oldEdStatus;
    }

    public String getOldUnitPrice() {
        return oldUnitPrice;
    }

    public void setOldUnitPrice(String oldUnitPrice) {
        this.oldUnitPrice = oldUnitPrice;
    }

    public Date getOldDateEffective() {
        return oldDateEffective;
    }

    public void setOldDateEffective(Date oldDateEffective) {
        this.oldDateEffective = oldDateEffective;
    }

    public UploadHospitalDrugItem getItem() {
        return item;
    }

    public void setItem(UploadHospitalDrugItem item) {
        this.item = item;
    }

    public boolean isEditMode() {
        return editMode;
    }

    public void setEditMode(boolean editMode) {
        this.editMode = editMode;
    }

    public String getUpdateFlag() {
        return updateFlag;
    }

    public void setUpdateFlag(String updateFlag) {
        this.updateFlag = updateFlag;
        item.setUpdateFlag(updateFlag);
    }

    public String getHospDrugCode() {
        return hospDrugCode;
    }

    public void setHospDrugCode(String hospDrugCode) {
        this.hospDrugCode = hospDrugCode;
    }

    public HospitalDrug getEditHospitalDrug() {
        return editHospitalDrug;
    }

    public void setEditHospitalDrug(HospitalDrug editHospitalDrug) {
        this.editHospitalDrug = editHospitalDrug;
    }

    public boolean isDisableSaveBtn() {
        return disableSaveBtn;
    }

    public void setDisableSaveBtn(boolean disableSaveBtn) {
        this.disableSaveBtn = disableSaveBtn;
    }

    public List<UploadHospitalDrugItem> getHistory() {
        return history;
    }

    public void setHistory(List<UploadHospitalDrugItem> history) {
        this.history = history;
    }

    public UploadHospitalDrugItem getBeforeEditHospitalDrug() {
        return beforeEditHospitalDrug;
    }

    public void setBeforeEditHospitalDrug(UploadHospitalDrugItem beforeEditHospitalDrug) {
        this.beforeEditHospitalDrug = beforeEditHospitalDrug;
    }

    public void saveBeforeEditStatus() {
        if (beforeEditHospitalDrug == null) {
            beforeEditHospitalDrug = new UploadHospitalDrugItem();
            BeanUtils.copyProperties(item, beforeEditHospitalDrug);
        } else {
            BeanUtils.copyProperties(beforeEditHospitalDrug, item);
        }
    }

    public void checkHospDrugCodeExist(FacesContext context, UIComponent component, Object value) {
        if (value == null) {
            return;
        }
        if (updateFlag.equals("A")) {
            if (uploadItemRepo.countByHospDrugCodeAndUploadDrugHcodeAndRequestAndAccept((String) value, SecurityUtil.getUserDetails().getHospital().getHcode()) > 0) {
                throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "error", "เคยมีการเพิ่มยานี้แล้ว"));
            }
        }
    }

    public void checkPriceOrEdExist(FacesContext context, UIComponent component, Object value) {
        if (value == null) {
            return;
        }
        if (uploadHospitalDrugItemService.isExistsItem(SecurityUtil.getUserDetails().getHospital().getHcode(), item.getHospDrugCode(), (Date) value, updateFlag)) {
            switch (updateFlag) {
                case "U":
                    throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "error", "เคยมีการระบุราคายา ณ วันที่ effectiveDate แล้ว"));
                case "E":
                    throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "error", "เคยมีการแก้ไขยา ณ วันที่ effectiveDate แล้ว"));
                case "D":
                    throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "error", "เคยมีการลบยา ณ วันที่ effectiveDate แล้ว"));
            }
        }
    }

    public void checkDuplicateDateEffective() {
        LOG.debug("updateFlag : {}", updateFlag);
        LOG.debug("new Date Effective : {}", item.getDateEffectiveDate());
        disableSaveBtn = uploadHospitalDrugItemService.isExistsItem(
                SecurityUtil.getUserDetails().getHospital().getHcode()
                , item.getHospDrugCode()
                , item.getDateEffectiveDate());
        if (disableSaveBtn) {
            FacesMessageUtils.error("กรุณาเปลี่ยน Date Effective");
        }
    }
    
    public boolean isSameDateEffective(Date d1 , Date d2){
        LOG.debug("DATE {}",DateUtils.format("ddMMyyyy", d1));
        return DateUtils.format("ddMMyyyy", d1).equals(DateUtils.format("ddMMyyyy", d2));
    }

    public void showHistory() {
        history = uploadHospitalDrugItemService.findEditHistory(SecurityUtil.getUserDetails().getHospital().getHcode(), hospDrugCode, item.getTmtId());
    }

    public void findTmt() {
        Map<String, Object> options = new HashMap<String, Object>();
        options.put("modal", true);
        options.put("draggable", true);
        options.put("resizable", true);
        options.put("contentHeight", 500);
        options.put("contentWidth", 1000);
        Map<String, List<String>> params = new HashMap<String, List<String>>();
        RequestContext.getCurrentInstance().openDialog("/private/hospital/create/selectDrugDialog.xhtml", options, params);
    }

    public void selectTmt(SelectEvent event) {
        TMTDrug tmtDrug = (TMTDrug) event.getObject();
        item.setGenericName(tmtDrug.getActiveIngredient());
        item.setTmtId(tmtDrug.getTmtId());
        item.setTradeName(tmtDrug.getTradeName());
        item.setManufacturer(tmtDrug.getManufacturer());
        item.setStrength(tmtDrug.getStrength());
        item.setDosageForm(tmtDrug.getDosageform());
        if (!Strings.isNullOrEmpty(tmtDrug.getContvalue())) {
            item.setContent(tmtDrug.getContvalue() + " " + tmtDrug.getContunit() + " " + tmtDrug.getDispUnit());
        }
        LOG.info("selected tmt drug => {}", tmtDrug.getTmtId());
    }

    public void loadHospitalDrug() {
        if (!editMode) {
            return;
        }
        editHospitalDrug = hospitalDrugRepo.findOne(new HospitalDrugPK(hospDrugCode, SecurityUtil.getUserDetails().getHospital().getHcode()));
        if (editHospitalDrug == null) {
            FacesMessageUtils.error("ไม่พบข้อมูล hospDrugCode = " + hospDrugCode);
            return;
        }
        BeanUtils.copyProperties(editHospitalDrug, item);
        if (editHospitalDrug.getDateChange() != null) {
            item.setDateChange(DateUtils.format(Constants.TMT_DATETIME_FORMAT, editHospitalDrug.getDateChange()));
        }
        if (editHospitalDrug.getDateUpdate() != null) {
            item.setDateUpdate(DateUtils.format(Constants.TMT_DATETIME_FORMAT, editHospitalDrug.getDateUpdate()));
        }
        if (editHospitalDrug.getDateEffective() != null) {
            item.setDateEffective(DateUtils.format(Constants.TMT_DATETIME_FORMAT, editHospitalDrug.getDateEffective()));
        }
        item.setUnitPrice(editHospitalDrug.getUnitPrice().toPlainString());
        if (editHospitalDrug.getPackPrice() != null) {
            item.setPackPrice(editHospitalDrug.getPackPrice().toPlainString());
        }
        saveBeforeEditStatus();
    }

    public String save() {
        item.timString();
        if (!editMode) {
            uploadHospitalDrugService.addNewDrugByHand(SecurityUtil.getUserDetails().getHospital().getHcode(), item);
            FacesMessageUtils.info("บันทึกเสร็จสิ้น");
            clear();
            return null;
        } else {
            if (beforeEditHospitalDrug.getDateEffective().equals(item.getDateEffectiveDate())) {
                FacesMessageUtils.info("ไม่สามารถบันทึก Date Effective เดียวกันได้");
            }
            uploadHospitalDrugService.editDrugByHand(SecurityUtil.getUserDetails().getHospital().getHcode(), item);
            FacesMessageUtils.info("แก้ไขเสร็จสิ้น ข้อมูลถูกส่งไปอนุมัติแล้ว ");
            clear();
            return "/private/hospital/listdrug/index?faces-redirect=true";
        }

    }

    public String clear() {
        item = new UploadHospitalDrugItem();
        item.setUpdateFlag("A");
        updateFlag = item.getUpdateFlag();
        editMode = false;
        hospDrugCode = null;
        return null;
    }

    public String reload() {
        return "/private/hospital/create/index?faces-redirect=true&amp;includeViewParams=true";
    }
}
