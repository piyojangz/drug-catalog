/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.controller.admin;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import th.co.geniustree.nhso.basicmodel.readonly.ICD10;
import th.co.geniustree.nhso.drugcatalog.controller.utils.FSNSplitter;
import th.co.geniustree.nhso.drugcatalog.model.Fund;
import th.co.geniustree.nhso.drugcatalog.model.ReimburseGroup;
import th.co.geniustree.nhso.drugcatalog.model.ReimburseGroupItem;
import th.co.geniustree.nhso.drugcatalog.model.ReimburseGroupItemPK;
import th.co.geniustree.nhso.drugcatalog.model.TMTDrug;
import th.co.geniustree.nhso.drugcatalog.service.FundService;
import th.co.geniustree.nhso.drugcatalog.service.ICD10Service;
import th.co.geniustree.nhso.drugcatalog.service.ReimburseGroupService;
import th.co.geniustree.nhso.drugcatalog.service.TMTDrugService;

/**
 *
 * @author Thanthathon
 */
@Component
@Scope("view")
public class SpecialGroupItem implements Serializable {

    private static final Logger LOG = LoggerFactory.getLogger(SpecialGroupItem.class);

    @Autowired
    private TMTDrugService tmtDrugService;

    @Autowired
    private ReimburseGroupService reimburseGroupService;

    @Autowired
    private ICD10Service icd10Service;

    @Autowired
    private FundService fundService;

    private List<Fund> funds;
    private List<Fund> selectedFunds = new ArrayList<>();
    private List<ReimburseGroup> specialGroups = new ArrayList<>();
    private List<ReimburseGroup> selectedReimburseGroups = new ArrayList<>();
    private List<ICD10> selectedICD10s = new ArrayList<>();
    private ReimburseGroupItem.ED statusED;

    private Set<String> strengths = new HashSet<>();
    private Set<String> dosageForms = new HashSet<>();
    private String selectedActiveIngredient = "";
    private String selectedStrength = "";
    private String selectedDosageForm = "";
    private String selectedContent = "";
    private List<TMTDrug> selectedTMTDrugs = new ArrayList<>();

    private List<ReimburseGroupItem> reimburseGroupItems = new ArrayList<>();

    private Set<ReimburseGroupItem> selectedReimburseGroupItems = new HashSet<>();

    private boolean checkedAll;

    private boolean initialDefaultCheckStatus = true;

    @PostConstruct
    public void postConstruct() {
        strengths.add("");
    }

    public void searchTMTDrug() {
        reimburseGroupItems.clear();
        selectedReimburseGroupItems.clear();
        selectedTMTDrugs = tmtDrugService.searchByFSN(selectedStrength + " " + selectedActiveIngredient + " " + selectedDosageForm);
        for (TMTDrug drug : selectedTMTDrugs) {
            ReimburseGroupItem item = new ReimburseGroupItem();
            item.setTmtDrug(drug);
            item.setPk(new ReimburseGroupItemPK(drug.getTmtId(), null, null, null, null));
            reimburseGroupItems.add(item);
            LOG.debug("search tmt : {}", drug.getTmtId());
            selectedReimburseGroupItems.add(item);
        }
    }

    public List<ReimburseGroup> completeSpecialGroup(String query) {
        List<ReimburseGroup> filterSpecialGroup = new ArrayList<>();
        List<ReimburseGroup> specialProjects = reimburseGroupService.findOnlySpecialProjectOrGroup(true);
        for (ReimburseGroup r : specialProjects) {
            if (r.getId().toUpperCase().contains(query.toUpperCase()) || r.getDescription().toUpperCase().contains(query.toUpperCase())) {
                filterSpecialGroup.add(r);
            }
        }
        return filterSpecialGroup;
    }

    public List<ICD10> completeICD10(String query) {
        List<ICD10> filterICD10 = new ArrayList<>();
        List<ICD10> icd10s = icd10Service.findAll();
        for (ICD10 icd10 : icd10s) {
            if (icd10.getCode().toUpperCase().contains(query.toUpperCase()) || icd10.getName().contains(query.toUpperCase())) {
                filterICD10.add(icd10);
            }
        }
        return filterICD10;
    }

    public List<ReimburseGroup> completeReimburseGroup(String query) {
        List<ReimburseGroup> filter = new ArrayList<>();
        List<ReimburseGroup> reimburseGroup = reimburseGroupService.findOnlySpecialProjectOrGroup(false);
        for (ReimburseGroup group : reimburseGroup) {
            if (group.getId().toUpperCase().contains(query.toUpperCase()) || group.getDescription().toUpperCase().contains(query.toUpperCase())) {
                filter.add(group);
            }
        }
        return filter;
    }

    public void onSelectActiveIngredient() {
        if (selectedActiveIngredient == null || selectedActiveIngredient.isEmpty()) {
            return;
        }
        strengths.clear();
        selectedTMTDrugs = tmtDrugService.searchByFSN(selectedActiveIngredient);
        FSNSplitter splitter = new FSNSplitter();
        for (TMTDrug drug : selectedTMTDrugs) {
            splitter.getActiveIngredientAndStrengthFromFSN(drug);
            strengths.addAll(splitter.getStrengths());
        }
        selectedStrength = "";
    }

    public void onSelectStrength() {
        dosageForms.clear();
        List<TMTDrug> drugs = new ArrayList<>();
        for (TMTDrug drug : selectedTMTDrugs) {
            if (drug.getFsn().contains(selectedStrength)) {
                drugs.add(drug);
            }
        }
        FSNSplitter splitter = new FSNSplitter();
        for (TMTDrug drug : drugs) {
            splitter.getActiveIngredientAndStrengthFromFSN(drug);
            String dosageForm = splitter.getDosageForm();
            if (dosageForm != null) {
                dosageForms.add(dosageForm);
            }
        }
        selectedDosageForm = "";
    }

    public List<String> completeStrength(String query) {
        if (selectedTMTDrugs == null || selectedTMTDrugs.isEmpty() || strengths == null) {
            return new ArrayList<>();
        }
        List<String> filterStrength = new ArrayList<>();
        for (String s : strengths) {
            if (s.toLowerCase().contains(query.toLowerCase())) {
                filterStrength.add(s);
            }
        }
        Collections.sort(filterStrength);
        return filterStrength;
    }

    public List<String> completeDosageForm(String query) {
        if (dosageForms == null) {
            return new ArrayList<>();
        }
        List<String> filterStrength = new ArrayList<>();
        for (String d : dosageForms) {
            if (d.toLowerCase().contains(query.toLowerCase())) {
                filterStrength.add(d);
            }
        }
        Collections.sort(filterStrength);
        return filterStrength;
    }

    public void toggleAllCheckBox() {
        if (checkedAll) {
            selectedReimburseGroupItems.addAll(reimburseGroupItems);
        } else {
            selectedReimburseGroupItems.clear();
        }
        LOG.debug("Total Selected TMT : {}" , selectedReimburseGroupItems.size());
        for(ReimburseGroupItem item : selectedReimburseGroupItems){
            LOG.debug("selected tmt : {}" , item.getPk().getTmtid());
        }
        initialDefaultCheckStatus = checkedAll;
    }

    public void selectItem(ReimburseGroupItem item) {
        for (ReimburseGroupItem r : selectedReimburseGroupItems) {
            if (r.getPk().getTmtid().equals(item.getPk().getTmtid())) {
                LOG.debug("disable selected tmt : {}", r.getPk().getTmtid());
                selectedReimburseGroupItems.remove(r);
                return;
            }
        }
        LOG.debug("enable selected tmt : {}", item.getPk().getTmtid());
        selectedReimburseGroupItems.add(item);
    }

    public void copyPrice(ReimburseGroupItem selectedItem) {
        for (ReimburseGroupItem item : selectedReimburseGroupItems) {
            item.setReimbursePrice(selectedItem.getReimbursePrice());
            LOG.debug("copy price to tmt : {}\t\tprice : {}", item.getTmtDrug().getTmtId(), item.getReimbursePrice());
        }
    }

//  ************************ getter and setter ************************
    public List<Fund> getSelectedFunds() {
        return selectedFunds;
    }

    public void setSelectedFunds(List<Fund> selectedFunds) {
        this.selectedFunds = selectedFunds;
    }

    public List<ReimburseGroup> getSpecialGroups() {
        return specialGroups;
    }

    public void setSpecialGroups(List<ReimburseGroup> specialGroups) {
        this.specialGroups = specialGroups;
    }

    public List<ReimburseGroup> getSelectedReimburseGroups() {
        return selectedReimburseGroups;
    }

    public void setSelectedReimburseGroups(List<ReimburseGroup> selectedReimburseGroups) {
        this.selectedReimburseGroups = selectedReimburseGroups;
    }

    public List<ICD10> getSelectedICD10s() {
        return selectedICD10s;
    }

    public void setSelectedICD10s(List<ICD10> selectedICD10s) {
        this.selectedICD10s = selectedICD10s;
    }

    public String getSelectedActiveIngredient() {
        return selectedActiveIngredient;
    }

    public void setSelectedActiveIngredient(String selectedActiveIngredient) {
        this.selectedActiveIngredient = selectedActiveIngredient;
    }

    public String getSelectedStrength() {
        return selectedStrength;
    }

    public void setSelectedStrength(String selectedStrength) {
        this.selectedStrength = selectedStrength;
    }

    public String getSelectedDosageForm() {
        return selectedDosageForm;
    }

    public void setSelectedDosageForm(String selectedDosageForm) {
        this.selectedDosageForm = selectedDosageForm;
    }

    public List<TMTDrug> getSelectedTMTDrugs() {
        return selectedTMTDrugs;
    }

    public void setSelectedTMTDrugs(List<TMTDrug> selectedTMTDrugs) {
        this.selectedTMTDrugs = selectedTMTDrugs;
    }

    public Set<String> getStrengths() {
        return strengths;
    }

    public void setStrengths(Set<String> strengths) {
        this.strengths = strengths;
    }

    public ReimburseGroupItem.ED getStatusED() {
        return statusED;
    }

    public void setStatusED(ReimburseGroupItem.ED statusED) {
        this.statusED = statusED;
    }

    public List<Fund> getFunds() {
        return funds;
    }

    public void setFunds(List<Fund> funds) {
        this.funds = funds;
    }

    public Set<String> getDosageForms() {
        return dosageForms;
    }

    public void setDosageForms(Set<String> dosageForms) {
        this.dosageForms = dosageForms;
    }

    public List<ReimburseGroupItem> getReimburseGroupItems() {
        return reimburseGroupItems;
    }

    public void setReimburseGroupItems(List<ReimburseGroupItem> reimburseGroupItems) {
        this.reimburseGroupItems = reimburseGroupItems;
    }

    public boolean isCheckedAll() {
        return checkedAll;
    }

    public void setCheckedAll(boolean checkedAll) {
        this.checkedAll = checkedAll;
    }

    public Set<ReimburseGroupItem> getSelectedReimburseGroupItems() {
        return selectedReimburseGroupItems;
    }

    public void setSelectedReimburseGroupItems(Set<ReimburseGroupItem> selectedReimburseGroupItems) {
        this.selectedReimburseGroupItems = selectedReimburseGroupItems;
    }

    public String getSelectedContent() {
        return selectedContent;
    }

    public void setSelectedContent(String selectedContent) {
        this.selectedContent = selectedContent;
    }

    public boolean isInitialDefaultCheckStatus() {
        return initialDefaultCheckStatus;
    }

    public void setInitialDefaultCheckStatus(boolean initialDefaultCheckStatus) {
        this.initialDefaultCheckStatus = initialDefaultCheckStatus;
    }

}
