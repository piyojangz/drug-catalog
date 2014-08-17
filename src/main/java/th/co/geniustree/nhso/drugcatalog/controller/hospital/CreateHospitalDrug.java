/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.controller.hospital;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import th.co.geniustree.nhso.drugcatalog.model.TMTDrug;
import th.co.geniustree.nhso.drugcatalog.model.UploadHospitalDrugItem;

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
    private boolean editPrice = false;

    @PostConstruct
    public void postConstruct() {
        clear();
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

    public boolean isEditPrice() {
        return editPrice;
    }

    public void setEditPrice(boolean editPrice) {
        this.editPrice = editPrice;
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
    public void selectTmt(SelectEvent event){
        TMTDrug tmtDrug = (TMTDrug) event.getObject();
        item.setTmtId(tmtDrug.getTmtId());
        item.setTradeName(tmtDrug.getTradeName());
        item.setManufacturer(tmtDrug.getManufacturer());
        item.setStrength(tmtDrug.getStrength());
        item.setDosageForm(tmtDrug.getDosageform());
        LOG.info("selected tmt drug => {}",tmtDrug);
    }


    public void save() {

    }

    public void clear() {
        item = new UploadHospitalDrugItem();
        if (editMode) {
            if (editPrice) {
                item.setUpdateFlag("U");
            } else {
                item.setUpdateFlag("E");
            }
        } else {
            item.setUpdateFlag("A");
        }
    }
}
