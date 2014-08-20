/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.controller.hospital;

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
import th.co.geniustree.nhso.drugcatalog.service.EdNEdService;
import th.co.geniustree.nhso.drugcatalog.service.PriceService;
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
    private PriceService priceService;
    @Autowired
    private EdNEdService edNEdService;

    @PostConstruct
    public void postConstruct() {
        clear(false);
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

    public void checkHospDrugCodeExist(FacesContext context, UIComponent component, Object value) {
        if (value == null) {
            return;
        }
        boolean exists = hospitalDrugRepo.exists(new HospitalDrugPK(value.toString(), SecurityUtil.getUserDetails().getHospital().getHcode()));
        if (exists) {
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "error", "มี HOSPDRUGCODE นี้แล้ว"));
        }
    }

    public void checkTmtExist(FacesContext context, UIComponent component, Object value) {
        if (value == null || editMode) {
            return;
        }
        Long count = hospitalDrugRepo.countByHcodeAndTmtId(SecurityUtil.getUserDetails().getHospital().getHcode(), value.toString());
        if (count > 0) {
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "error", "มีการขอเพิ่ม TMTID นี้แล้ว"));
        }
    }

    public void checkPriceExist(FacesContext context, UIComponent component, Object value) {
        if (value == null || !editMode || !updateFlag.equals("U")) {
            return;
        }
        if (priceService.isPriceDuplicate(SecurityUtil.getUserDetails().getHospital().getHcode(), item.getHospDrugCode(), (Date) value)) {
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "error", "เคยมีการระบุราคายา ณ วันที่ effectiveDate แล้ว"));
        }
    }

    public void checkEdNedExist(FacesContext context, UIComponent component, Object value) {
        if (value == null || !editMode || !updateFlag.equals("E")) {
            return;
        }
        if (edNEdService.isDuplicateEdNed(SecurityUtil.getUserDetails().getHospital().getHcode(), item.getHospDrugCode(), (Date) value)) {
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "error", "เคยมีการระบุ ISED ณ วันที่ dateChange แล้ว"));
        }
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
        item.setTmtId(tmtDrug.getTmtId());
        item.setTradeName(tmtDrug.getTradeName());
        item.setManufacturer(tmtDrug.getManufacturer());
        item.setStrength(tmtDrug.getStrength());
        item.setDosageForm(tmtDrug.getDosageform());
        LOG.info("selected tmt drug => {}", tmtDrug);
    }

    public void loadHospitalDrug() {
        if (!editMode) {
            return;
        }
        HospitalDrug findOne = hospitalDrugRepo.findOne(new HospitalDrugPK(hospDrugCode, SecurityUtil.getUserDetails().getHospital().getHcode()));
        if (findOne == null) {
            FacesMessageUtils.error("ไม่พบข้อมูล hospDrugCode = " + hospDrugCode);
            return;
        }
        BeanUtils.copyProperties(findOne, item);
        if (findOne.getDateChange() != null) {
            item.setDateChange(DateUtils.format(Constants.TMT_DATETIME_FORMAT, findOne.getDateChange()));
        }
        if (findOne.getDateUpdate() != null) {
            item.setDateUpdate(DateUtils.format(Constants.TMT_DATETIME_FORMAT, findOne.getDateUpdate()));
        }
        if (findOne.getDateEffective() != null) {
            item.setDateEffective(DateUtils.format(Constants.TMT_DATETIME_FORMAT, findOne.getDateEffective()));
        }
        item.setUnitPrice(findOne.getUnitPrice().toPlainString());
        if (findOne.getPackPrice() != null) {
            item.setPackPrice(findOne.getPackPrice().toPlainString());
        }
    }

    public String save() {
        if (!editMode) {
            uploadHospitalDrugService.addNewDrugByHand(SecurityUtil.getUserDetails().getHospital().getHcode(), item);
            clear(false);
            FacesMessageUtils.info("บันทึกเสร็จสิ้น");
            return null;
        } else {
            uploadHospitalDrugService.editDrugByHand(SecurityUtil.getUserDetails().getHospital().getHcode(), item);
            FacesMessageUtils.info("แก้ไขเสร็จสิ้น");
            return "/private/hospital/listdrug/index?faces-redirect=true";
        }

    }

    public String clear(boolean navigate) {
        item = new UploadHospitalDrugItem();
        item.setUpdateFlag("A");
        updateFlag = item.getUpdateFlag();
        editMode = false;
        hospDrugCode = null;
        if(navigate){
            return "/private/hospital/listdrug/index?faces-redirect=true";
        }else{
            return null;
        }
    }
}
