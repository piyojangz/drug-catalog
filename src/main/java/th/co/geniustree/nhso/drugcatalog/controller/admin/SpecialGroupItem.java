/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.controller.admin;

import java.io.Serializable;
import java.util.ArrayList;
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
import th.co.geniustree.nhso.drugcatalog.controller.SpringDataLazyDataModelSupport;
import th.co.geniustree.nhso.drugcatalog.controller.utils.FSNSplitter;
import th.co.geniustree.nhso.drugcatalog.model.Fund;
import th.co.geniustree.nhso.drugcatalog.model.ReimburseGroup;
import th.co.geniustree.nhso.drugcatalog.model.ReimburseGroupItem;
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
    private String selectedActiveIngredient = "";
    private String selectedStrength = "";
    private String selectedDosageForm = "";
    private SpringDataLazyDataModelSupport<TMTDrug> tmtDrugs;
    private List<TMTDrug> selectedTMTDrugs = new ArrayList<>();

    @PostConstruct
    public void postConstruct() {
        funds = fundService.findAll();
    }
    
    public void searchTMTDrug(){
        LOG.debug("ed/ned : {}", statusED);
    }
        
    public List<ReimburseGroup> completeSpecialGroup(String query){
        List<ReimburseGroup> filterSpecialGroup = new ArrayList<>();
        List<ReimburseGroup> specialProjects = reimburseGroupService.findOnlySpecialProjectOrGroup(true);
        for(ReimburseGroup r : specialProjects){
            if(r.getId().toUpperCase().contains(query.toUpperCase()) || r.getDescription().toUpperCase().contains(query.toUpperCase())){
                filterSpecialGroup.add(r);
            }
        }
        return filterSpecialGroup;
    }
    
    public List<ICD10> completeICD10(String query){
        List<ICD10> filterICD10 = new ArrayList<>();
        List<ICD10> icd10s = icd10Service.findAll();
        for(ICD10 icd10 : icd10s){
            if(icd10.getCode().toUpperCase().contains(query.toUpperCase()) || icd10.getName().contains(query.toUpperCase())){
                filterICD10.add(icd10);
            }
        }
        return filterICD10;
    }
    
    public List<ReimburseGroup> completeReimburseGroup(String query){
        List<ReimburseGroup> filter = new ArrayList<>();
        List<ReimburseGroup> reimburseGroup = reimburseGroupService.findOnlySpecialProjectOrGroup(false);
        for(ReimburseGroup group : reimburseGroup){
            if(group.getId().toUpperCase().contains(query.toUpperCase()) || group.getDescription().toUpperCase().contains(query.toUpperCase())){
                filter.add(group);
            }
        }
        return filter;
    }

    public void onFindStrength() {
        strengths.clear();
        selectedTMTDrugs = tmtDrugService.searchByFSN(selectedActiveIngredient);
        FSNSplitter splitter = new FSNSplitter();
        for (TMTDrug drug : selectedTMTDrugs) {
            splitter.getActiveIngredientAndStrengthFromFSN(drug);
            strengths.addAll(splitter.getStrengths());
        }
    }

    public List<String> completeStrength(String query) {
        if (selectedTMTDrugs == null || selectedTMTDrugs.isEmpty()) {
            onFindStrength();
        }
        if (strengths == null || strengths.isEmpty()) {
            return new ArrayList<>(1);
        }
        List<String> filterStrength = new ArrayList<>();
        for (String s : strengths) {
            if (s.toLowerCase().contains(query.toLowerCase())) {
                filterStrength.add(s);
            }
        }
        return filterStrength;
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

    public SpringDataLazyDataModelSupport<TMTDrug> getTmtDrugs() {
        return tmtDrugs;
    }

    public void setTmtDrugs(SpringDataLazyDataModelSupport<TMTDrug> tmtDrugs) {
        this.tmtDrugs = tmtDrugs;
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

    
    
}
