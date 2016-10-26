/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.controller.common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.model.ListDataModel;
import javax.faces.validator.ValidatorException;
import org.primefaces.context.RequestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import th.co.geniustree.nhso.drugcatalog.model.DrugGroup;
import th.co.geniustree.nhso.drugcatalog.model.TMTDrugGroupItem;
import th.co.geniustree.nhso.drugcatalog.repo.DrugGroupRepo;
import th.co.geniustree.nhso.drugcatalog.repo.TMTDrugGroupItemRepo;

/**
 *
 * @author moth
 */
@Component
@Scope("view")
public class SelectDrugGroupDialog implements Serializable {

    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(SelectDrugGroupDialog.class);
    @Autowired
    private DrugGroupRepo drugGroupRepo;
    @Autowired
    private TMTDrugGroupItemRepo tmtDrugGroupItemRepo;
    private List<TMTDrugGroupItem> selectedDrugGroups;
    private DrugGroupSelectableDataModel model;
    private String tmtId;
    private List<DrugGroup> drugGroups;

    @PostConstruct
    public void postConstruct() {

    }

    public void load() {
        LOG.info("Load druggroup for tmt = {}", tmtId);
        drugGroups = drugGroupRepo.findAll(new Sort(Sort.Direction.ASC, "id"));
        List<TMTDrugGroupItem> tmtDrugGroupItems = tmtDrugGroupItemRepo.findByTMTDrugTMTId(tmtId);
        List<TMTDrugGroupItem> mergeTmtDrugGroupitems = new ArrayList<>();
        for (DrugGroup drugGroup : drugGroups) {

            boolean found = false;
            for (TMTDrugGroupItem tmtDrugGroupItem : tmtDrugGroupItems) {
                if (tmtDrugGroupItem.getDrugGroup().equals(drugGroup)) {
                    mergeTmtDrugGroupitems.add(tmtDrugGroupItem);
                    found = true;
                    break;
                }
            }
            if (!found) {
                mergeTmtDrugGroupitems.add(new TMTDrugGroupItem(null, drugGroup, null));
            }
        }
        model = new DrugGroupSelectableDataModel(mergeTmtDrugGroupitems);
        selectedDrugGroups = new ArrayList<>();
    }

    public void validdateDateIn(Object value) {
        TMTDrugGroupItem tmtDrugGroupItem =(TMTDrugGroupItem) value;
        if (tmtDrugGroupItem.getDatein() == null) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "จะต้องระบุ datein", null);
            throw new ValidatorException(message);
        }

    }

    public List<TMTDrugGroupItem> getSelectedDrugGroups() {
        return selectedDrugGroups;
    }

    public void setSelectedDrugGroups(List<TMTDrugGroupItem> selectedDrugGroups) {
        this.selectedDrugGroups = selectedDrugGroups;
    }

    public DrugGroupSelectableDataModel getModel() {
        return model;
    }

    public void setModel(DrugGroupSelectableDataModel model) {
        this.model = model;
    }

    public String getTmtId() {
        return tmtId;
    }

    public void setTmtId(String tmtId) {
        this.tmtId = tmtId;
    }

    public void closeDialog() {
        for (TMTDrugGroupItem tmtDrugGroupItem : selectedDrugGroups) {
            if (tmtDrugGroupItem.getDatein() == null) {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "จะต้องระบุ datein", null);
                throw new ValidatorException(message);
            }
        }
        LOG.info("Select drug group {}", selectedDrugGroups);
        RequestContext.getCurrentInstance().closeDialog(drugGroups);
    }

    public static class DrugGroupSelectableDataModel extends ListDataModel implements org.primefaces.model.SelectableDataModel<TMTDrugGroupItem> {

        public DrugGroupSelectableDataModel() {
        }

        public DrugGroupSelectableDataModel(List<TMTDrugGroupItem> list) {
            super(list);
        }

        @Override
        public Object getRowKey(TMTDrugGroupItem object) {
            return object.getDrugGroup().getId();
        }

        @Override
        public TMTDrugGroupItem getRowData(String rowKey) {
            List<TMTDrugGroupItem> items = (List<TMTDrugGroupItem>) getWrappedData();
            for (TMTDrugGroupItem item : items) {
                if (item.getDrugGroup().getId().equals(rowKey)) {
                    return item;
                }
            }
            return null;
        }
    }
}
