/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.controller.hospital;

import com.google.common.base.Strings;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import th.co.geniustree.nhso.drugcatalog.authen.SecurityUtil;
import th.co.geniustree.nhso.drugcatalog.controller.utils.FacesMessageUtils;
import th.co.geniustree.nhso.drugcatalog.model.RequestItem;
import th.co.geniustree.nhso.drugcatalog.model.TMTDrug;
import th.co.geniustree.nhso.drugcatalog.model.UploadHospitalDrugItem;
import th.co.geniustree.nhso.drugcatalog.repo.UploadHospitalDrugItemRepo;
import th.co.geniustree.nhso.drugcatalog.service.UploadHospitalDrugService;

/**
 *
 * @author thanthathon.b
 */
@Component
@Scope("view")
public class EditRequestHospitalDrug implements Serializable {

    private static final Logger LOG = LoggerFactory.getLogger(EditRequestHospitalDrug.class);

    @Autowired
    private UploadHospitalDrugItemRepo uploadItemRepo;
    @Autowired
    private UploadHospitalDrugService uploadHospitalDrugService;

    private UploadHospitalDrugItem item;
    private UploadHospitalDrugItem restoreItem;

    private Integer itemId;

    public UploadHospitalDrugItem getItem() {
        return item;
    }

    public void setItem(UploadHospitalDrugItem item) {
        this.item = item;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public void loadHospitalDrug() {
        item = new UploadHospitalDrugItem();
        UploadHospitalDrugItem uploadItem = uploadItemRepo.findOne(itemId);
        if (uploadItem == null || uploadItem.getRequestItem().isDeleted()) {
            FacesMessageUtils.error("ไม่พบข้อมูล กรุณาติดต่อ สปสช.");
            LOG.error("Hospital {} : Not found item ID : {}", SecurityUtil.getUserDetails().getHospital().getHcode(), itemId);
        } else if (uploadItem.getRequestItem().getStatus().equals(RequestItem.Status.ACCEPT)) {
            FacesMessageUtils.error("ข้อมูลถูก Approve ไปเมื่อสักครู่ ลองตรวจสอบที่หน้าจอ รายการยาที่ผ่านการตรวจสอบ");
        } else if (uploadItem.getRequestItem().getStatus().equals(RequestItem.Status.REJECT)) {
            FacesMessageUtils.error("ข้อมูลถูก Reject ไปเมื่อสักครู่ ลองตรวจสอบที่หน้าจอ รายการยาที่ไม่ผ่านการตรวจสอบ");
        } else {
            BeanUtils.copyProperties(uploadItem, item);
            createRestoreItem();
        }

    }

    public void createRestoreItem() {
        if (restoreItem == null) {
            restoreItem = new UploadHospitalDrugItem();
            BeanUtils.copyProperties(item, restoreItem);
        }
    }

    public void restoreItem() {
        BeanUtils.copyProperties(restoreItem, item);
    }

    public void findTmt() {
        Map<String, Object> options = new HashMap<>();
        options.put("modal", true);
        options.put("draggable", true);
        options.put("resizable", true);
        options.put("contentHeight", 500);
        options.put("contentWidth", 1000);
        Map<String, List<String>> params = new HashMap<>();
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
    }

    public String cancel() {
        item = null;
        restoreItem = null;
        itemId = null;
        return "/private/hospital/listdrug/wait.xhtml";
    }

    public String save() {
        item.timString();
        
        //same as remove old drug and create new drug
        uploadHospitalDrugService.editDrugFlagAByHand(SecurityUtil.getUserDetails().getHospital().getHcode(), item);
        
        FacesMessageUtils.info("แก้ไขเสร็จสิ้น กรุณาตรวจสอบข้อมูลอีกครั้ง");
        cancel();
        return "/private/hospital/listdrug/wait?wait=true&faces-redirect=true";
    }

}
