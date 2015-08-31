/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.controller.admin;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Component;
import th.co.geniustree.nhso.basicmodel.readonly.ICD10;
import th.co.geniustree.nhso.drugcatalog.controller.SpringDataLazyDataModelSupport;
import th.co.geniustree.nhso.drugcatalog.controller.utils.FSNSplitter;
import th.co.geniustree.nhso.drugcatalog.controller.utils.FacesMessageUtils;
import th.co.geniustree.nhso.drugcatalog.model.Fund;
import th.co.geniustree.nhso.drugcatalog.model.ReimburseGroup;
import th.co.geniustree.nhso.drugcatalog.model.ReimburseGroupItem;
import th.co.geniustree.nhso.drugcatalog.model.ReimburseGroupItemPK;
import th.co.geniustree.nhso.drugcatalog.model.TMTDrug;
import th.co.geniustree.nhso.drugcatalog.repo.spec.TMTDrugSpecs;
import th.co.geniustree.nhso.drugcatalog.service.FundService;
import th.co.geniustree.nhso.drugcatalog.service.ICD10Service;
import th.co.geniustree.nhso.drugcatalog.service.ReimburseGroupItemService;
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

    @Autowired
    private ReimburseGroupItemService reimburseGroupItemService;

    private List<Fund> funds;

    private List<Fund> selectedFunds;
    private List<ReimburseGroup> selectedReimburseGroups;
    private List<ReimburseGroup> selectedSpecialProjects;
    private List<ICD10> selectedICD10s;
    private ReimburseGroupItem.ED statusED;
    private Date dateIn;

    private Set<String> strengths = new HashSet<>();
    private Set<String> dosageForms = new HashSet<>();
    private List<String> contents = new ArrayList<>();
    private String selectedActiveIngredient = "";
    private String selectedStrength = "";
    private String selectedDosageForm = "";
    private String selectedContent = "";
    private List<TMTDrug> selectedTMTDrugs = new ArrayList<>();

    private List<ReimburseGroupItem> reimburseGroupItems = new ArrayList<>();
    private Set<ReimburseGroupItem> selectedReimburseGroupItems = new HashSet<>();

    private Set<ReimburseGroupItem> beforeSaveGroupItems = new HashSet<>();

    private SpringDataLazyDataModelSupport<ReimburseGroupItem> groupedItems;

    private boolean checkedAll;

    private boolean initialDefaultCheckStatus = true;

    private ReimburseGroupItem selectItem;

    @PostConstruct
    public void postConstruct() {
        strengths.add("");
        funds = fundService.findAll();
        groupedItems = new SpringDataLazyDataModelSupport<ReimburseGroupItem>() {

            @Override
            public Page<ReimburseGroupItem> load(Pageable pageAble) {
                return reimburseGroupItemService.findAllPaging(pageAble);
            }

        };

    }

    public void reset() {
        reimburseGroupItems.clear();
        selectedReimburseGroupItems.clear();
        contents.clear();
    }

    public void searchTMTDrug() {
        reset();
        List<String> keywords = Arrays.asList((selectedStrength + " " + selectedActiveIngredient + " " + selectedDosageForm).split("\\s+"));
        if (!(keywords == null || keywords.isEmpty())) {
            List<TMTDrug.Type> typesIn = new ArrayList<>();
            typesIn.add(TMTDrug.Type.GP);
            typesIn.add(TMTDrug.Type.TP);
            typesIn.add(TMTDrug.Type.TPU);
            Specification<TMTDrug> spec = Specifications.where(TMTDrugSpecs.fsnContains(keywords)).and(TMTDrugSpecs.typeIn(typesIn));
            selectedTMTDrugs = tmtDrugService.findBySpec(spec);
        }
        FSNSplitter splitter = new FSNSplitter();
        Set<String> contentSet = new HashSet<>();
        for (TMTDrug drug : selectedTMTDrugs) {
            ReimburseGroupItem item = new ReimburseGroupItem();
            item.setTmtDrug(drug);
            item.setPk(new ReimburseGroupItemPK(drug.getTmtId(), null, null, null, null));

            reimburseGroupItems.add(item);
            if (!drug.getType().equals(TMTDrug.Type.GP)) {
                selectedReimburseGroupItems.add(item);
                splitter.getActiveIngredientAndStrengthFromFSN(drug);
                String content = splitter.getContent();
                if (content != null) {
                    contentSet.add(content);
                }
            }
        }
        contents.addAll(contentSet);
    }

    public List<ReimburseGroup> completeSpecialGroup(String query) {
        return reimburseGroupService.searchOnlySpecialStatus(query, true);
    }

    public List<ICD10> completeICD10(String query) {
        return icd10Service.search(query);
    }

    public List<ReimburseGroup> completeReimburseGroup(String query) {
        return reimburseGroupService.searchOnlySpecialStatus(query, false);
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
        List<String> filterDosageForm = new ArrayList<>();
        for (String d : dosageForms) {
            if (d.toLowerCase().contains(query.toLowerCase())) {
                filterDosageForm.add(d);
            }
        }
        Collections.sort(filterDosageForm);
        return filterDosageForm;
    }

    public List<String> completeContent(String query) {
        if (contents == null) {
            return new ArrayList<>();
        }
        List<String> filterContent = new ArrayList<>();
        for (String c : contents) {
            if (c.toLowerCase().contains(query.toLowerCase())) {
                filterContent.add(c);
            }
        }
        Collections.sort(filterContent);
        return filterContent;
    }

    public void onFilterByContent() {
        selectedReimburseGroupItems.clear();
        for (ReimburseGroupItem r : reimburseGroupItems) {
            if (r.getTmtDrug().getFsn().contains(selectedContent)) {
                selectedReimburseGroupItems.add(r);
            }
        }
    }

    public void checkAll() {
        LOG.debug("is check all : {}", checkedAll);
        if (checkedAll) {
            for (ReimburseGroupItem item : reimburseGroupItems) {
                if (!item.getTmtDrug().getType().equals(TMTDrug.Type.GP)) {
                    selectedReimburseGroupItems.add(item);
                    LOG.debug("selected tmt : {}", item.getPk().getTmtid());
                }
            }
        } else {
            selectedReimburseGroupItems.clear();
        }
        initialDefaultCheckStatus = checkedAll;
        LOG.debug("Total Selected TMT : {}", selectedReimburseGroupItems.size());

    }

    public void checkItem(ReimburseGroupItem item) {
        LOG.debug("checkbox status : {}", initialDefaultCheckStatus);
        if (initialDefaultCheckStatus) {
            selectedReimburseGroupItems.add(item);
            LOG.debug("add item tmt : {}", item.getPk().getTmtid());
        } else {
            selectedReimburseGroupItems.remove(item);
            LOG.debug("remove item tmt : {}", item.getPk().getTmtid());
        }

    }

    public void copyPrice(ReimburseGroupItem selectedItem) {
        for (ReimburseGroupItem item : selectedReimburseGroupItems) {
            item.setReimbursePrice(selectedItem.getReimbursePrice());
            LOG.debug("copy price to tmt : {}\t\tprice : {}", item.getTmtDrug().getTmtId(), item.getReimbursePrice());
        }
    }

    public void copyDateIn(ReimburseGroupItem selectedItem) {
        for (ReimburseGroupItem item : selectedReimburseGroupItems) {
            item.getPk().setBudgetYear(selectedItem.getPk().getBudgetYear());
            LOG.debug("copy date to tmt : {}\t\tdate : {}", item.getTmtDrug().getTmtId(), item.getPk().getBudgetYear());
        }
    }

    public void onSave() {
        List<ReimburseGroup> selectedGroups = new ArrayList<>();
        selectedGroups.addAll(selectedSpecialProjects);
        selectedGroups.addAll(selectedReimburseGroups);
        for (ReimburseGroupItem r : selectedReimburseGroupItems) {
            for (Fund fund : selectedFunds) {
                for (ReimburseGroup group : selectedGroups) {
                    for (ICD10 icd10 : selectedICD10s) {
                        ReimburseGroupItem item = new ReimburseGroupItem(r.getTmtDrug(), fund, icd10, group, statusED, r.getPk().getBudgetYear(), r.getReimbursePrice());
                        item.setPk(new ReimburseGroupItemPK(r.getTmtDrug().getTmtId(), fund.getCode(), icd10.getCode(), group.getId(), r.getPk().getBudgetYear()));
                        beforeSaveGroupItems.add(item);
                        LOG.debug("tmtid : {}\t\tFSN : {}", item.getPk().getTmtid(), item.getTmtDrug().getFsn());
                        LOG.debug("ReimbursePrice : {}", item.getReimbursePrice());
                        LOG.debug("DateIn : ", item.getPk().getBudgetYear());
                        LOG.debug("Fund : {} - {}", item.getPk().getFundCode(), item.getFund().getName());
                        LOG.debug("ReimburseGroup : {} - {}", item.getPk().getReimburseGroupId(), item.getReimburseGroup().getDescription());
                        LOG.debug("ICD10 : {} - {}", item.getPk().getIcd10Code(), item.getIcd10().getCode());
                        LOG.debug("ED / NED : {} - {}", item.getStatusEd());
                    }
                }
            }
        }
    }

    public void save() {
        try {
            reimburseGroupItemService.save(beforeSaveGroupItems);
            FacesMessageUtils.info("ยาถูกจัดกลุ่ม สำเร็จ");
        } catch (Exception e) {
            FacesMessageUtils.error("ไม่สามารถจัดกลุ่มยาได้");
        }
    }

    public void onDelete(ReimburseGroupItem item) {
        this.selectItem = item;
        LOG.debug("selete tmt : {}", item.getPk().getTmtid());
        LOG.debug("delete Fund : {}", item.getPk().getFundCode());
        LOG.debug("delete Group : {}", item.getPk().getReimburseGroupId());
        LOG.debug("delete ICD10 : {}", item.getPk().getIcd10Code());
        LOG.debug("delete dateIn : {}", item.getPk().getBudgetYear());
    }

    public void delete() {
        try{
            reimburseGroupItemService.delete(selectItem);
            FacesMessageUtils.info("ลบข้อมูลสำเร็จ");
        } catch (Exception e){
             FacesMessageUtils.error("ไม่สามารถลบข้อมูลได้");
        }
    }

//  ************************ getter and setter ************************
    public List<Fund> getSelectedFunds() {
        return selectedFunds;
    }

    public void setSelectedFunds(List<Fund> selectedFunds) {
        this.selectedFunds = selectedFunds;
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

    public Set<ReimburseGroupItem> getBeforeSaveGroupItems() {
        return beforeSaveGroupItems;
    }

    public void setBeforeSaveGroupItems(Set<ReimburseGroupItem> beforeSaveGroupItems) {
        this.beforeSaveGroupItems = beforeSaveGroupItems;
    }

    public List<String> getContents() {
        return contents;
    }

    public void setContents(List<String> contents) {
        this.contents = contents;
    }

    public Date getDateIn() {
        return dateIn;
    }

    public void setDateIn(Date dateIn) {
        this.dateIn = dateIn;
    }

    public List<ReimburseGroup> getSelectedSpecialProjects() {
        return selectedSpecialProjects;
    }

    public void setSelectedSpecialProjects(List<ReimburseGroup> selectedSpecialProjects) {
        this.selectedSpecialProjects = selectedSpecialProjects;
    }

    public SpringDataLazyDataModelSupport<ReimburseGroupItem> getGroupedItems() {
        return groupedItems;
    }

    public void setGroupedItems(SpringDataLazyDataModelSupport<ReimburseGroupItem> groupedItems) {
        this.groupedItems = groupedItems;
    }

    public ReimburseGroupItem getSelectItem() {
        return selectItem;
    }

    public void setSelectItem(ReimburseGroupItem selectItem) {
        this.selectItem = selectItem;
    }

}
