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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import th.co.geniustree.nhso.drugcatalog.authen.SecurityUtil;
import th.co.geniustree.nhso.drugcatalog.controller.utils.FacesMessageUtils;
import th.co.geniustree.nhso.drugcatalog.model.HospitalDrugPK;
import th.co.geniustree.nhso.drugcatalog.model.RequestItem;
import th.co.geniustree.nhso.drugcatalog.model.TMTDrug;
import th.co.geniustree.nhso.drugcatalog.model.UploadHospitalDrugItem;
import th.co.geniustree.nhso.drugcatalog.repo.HospitalDrugRepo;
import th.co.geniustree.nhso.drugcatalog.repo.RequestItemRepo;
import th.co.geniustree.nhso.drugcatalog.repo.UploadHospitalDrugItemRepo;
import th.co.geniustree.nhso.drugcatalog.service.EdNEdService;
import th.co.geniustree.nhso.drugcatalog.service.PriceService;

/**
 *
 * @author moth
 */
@Component
@Scope("view")
public class EditNotPassRequest implements Serializable {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(EditNotPassRequest.class);
    private Integer requestItemId;
    private UploadHospitalDrugItem item;
    @Autowired
    private RequestItemRepo requestItemRepo;
    @Autowired
    private UploadHospitalDrugItemRepo uploadHospitalDrugItemRepo;
    @Autowired
    private HospitalDrugRepo hospitalDrugRepo;
    private String updateFlag;
    @Autowired
    private PriceService priceService;
    @Autowired
    private EdNEdService edNEdService;

    public Integer getRequestItemId() {
        return requestItemId;
    }

    public void setRequestItemId(Integer requestItemId) {
        this.requestItemId = requestItemId;
    }

    public UploadHospitalDrugItem getItem() {
        return item;
    }

    public void setItem(UploadHospitalDrugItem item) {
        this.item = item;
    }

    public String getUpdateFlag() {
        return updateFlag;
    }

    public void setUpdateFlag(String updateFlag) {
        this.updateFlag = updateFlag;
    }

    @PostConstruct
    public void postConstruct() {

    }

    public void load() {
        RequestItem requestItem = requestItemRepo.findOne(requestItemId);
        if (requestItem == null) {
            FacesMessageUtils.warn("ไม่พบรายการนี้อยู่ในระบบ");
            return;
        }
        if (requestItem.getStatus() != RequestItem.Status.REJECT) {
            FacesMessageUtils.warn("แก้ไขรายการที่ไม่อนุมัติเท่านั้น");
            return;
        }
        item = requestItem.getUploadDrugItem();
        updateFlag = item.getUpdateFlag();
        if (!item.getUploadDrug().getHcode().equals(SecurityUtil.getUserDetails().getHospital().getHcode())) {
            FacesMessageUtils.warn("แก้ไขได้เฉพาะรายการของหน่วยบริการตนเองเท่านั้น");
            return;
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
        item.setGenericName(tmtDrug.getActiveIngredient());
        item.setTmtId(tmtDrug.getTmtId());
        item.setTradeName(tmtDrug.getTradeName());
        item.setManufacturer(tmtDrug.getManufacturer());
        item.setStrength(tmtDrug.getStrength());
        item.setDosageForm(tmtDrug.getDosageform());
        log.info("selected tmt drug => {}", tmtDrug);
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

    public void checkPriceOrEdExist(FacesContext context, UIComponent component, Object value) {
        if (value == null) {
            return;
        }
        if (updateFlag.equals("U")) {
            if (priceService.isPriceDuplicate(SecurityUtil.getUserDetails().getHospital().getHcode(), item.getHospDrugCode(), (Date) value)) {
                throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "error", "เคยมีการระบุราคายา ณ วันที่ effectiveDate แล้ว"));
            }
        } else if (updateFlag.equals("E")) {
            if (edNEdService.isDuplicateEdNed(SecurityUtil.getUserDetails().getHospital().getHcode(), item.getHospDrugCode(), (Date) value)) {
                throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "error", "เคยมีการระบุ ISED ณ วันที่ effectiveDate แล้ว"));
            }
        }
    }

    public String save() {
        item.setUpdateFlag(updateFlag);
        item.getRequestItem().setApproveDate(null);
        item.getRequestItem().setApproveUser(null);
        item.getRequestItem().setStatus(RequestItem.Status.REQUEST);
        item = uploadHospitalDrugItemRepo.save(item);
        FacesMessageUtils.info("แก้ไขเสร็จสิ้น ข้อมูลถูกส่งไปอนุมัติแล้ว");
        return cancel();
    }

    public String cancel() {
        return "/private/hospital/listdrug/not-approved.xhtml?faces-redirect=true&amp;includeViewParams=true&amp;notApproved=true";
    }
}
