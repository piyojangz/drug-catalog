/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.controller.admin;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
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
import th.co.geniustree.nhso.drugcatalog.controller.SpringDataLazyDataModelSupport;
import th.co.geniustree.nhso.drugcatalog.controller.utils.FSNSplitter;
import th.co.geniustree.nhso.drugcatalog.controller.utils.FacesMessageUtils;
import th.co.geniustree.nhso.drugcatalog.model.TMTDrug;
import th.co.geniustree.nhso.drugcatalog.model.TMTRelation;
import th.co.geniustree.nhso.drugcatalog.model.TMTRelationID;
import th.co.geniustree.nhso.drugcatalog.repo.spec.TMTDrugSpecs;
import th.co.geniustree.nhso.drugcatalog.repo.spec.TMTRelationSpecs;
import th.co.geniustree.nhso.drugcatalog.service.TMTDrugService;
import th.co.geniustree.nhso.drugcatalog.service.TMTRelationService;

/**
 *
 * @author Thanthathon
 */
@Component
@Scope("session")
public class TmtRelationMapping implements Serializable {

    private static final Logger LOG = LoggerFactory.getLogger(TmtRelationMapping.class);

    @Autowired
    private TMTDrugService tmtDrugService;
    @Autowired
    private TMTRelationService tmtRelationService;

    private SpringDataLazyDataModelSupport<TMTDrug> tmtParents;
    private SpringDataLazyDataModelSupport<TMTDrug> tmtChildren;
    private SpringDataLazyDataModelSupport<TMTRelation> tmtRelations;
    private List<TMTDrug> viewChildrenTMT;

    private TMTDrug selectedTMTParent;
    private String keyword;
    private Set<TMTDrug> selectedTMTChildren;
    private List<TMTDrug> beforeEditTMTChildren;
    private List<TMTDrug> deletedTMTChildren;
    private boolean checkItem;
    private final List<TMTDrug.Type> allType = new ArrayList<>();
    private TMTDrug.Type filterType;
    private TMTRelation selectRelation;

    @PostConstruct
    public void postConstruct() {
        keyword = "";
        selectedTMTChildren = new HashSet<>();
        filterType = TMTDrug.Type.SUB;
        allType.add(TMTDrug.Type.SUB);
        allType.add(TMTDrug.Type.VTM);
        allType.add(TMTDrug.Type.GP);
        allType.add(TMTDrug.Type.GPU);
        allType.add(TMTDrug.Type.TP);
        viewAllRelation();
    }

    public void viewAllRelation() {
        tmtRelations = searchRelation(null, allType);
    }

    public void searchRelation() {
        if (filterType == null) {
            tmtRelations = searchRelation(keyword, allType);
        } else {
            List<TMTDrug.Type> typeIn = new ArrayList<>();
            typeIn.add(filterType);
            tmtRelations = searchRelation(keyword, typeIn);
        }

    }

    private SpringDataLazyDataModelSupport<TMTRelation> searchRelation(final String keyword, final List<TMTDrug.Type> typeIn) {
        return new SpringDataLazyDataModelSupport<TMTRelation>() {

            @Override
            public Page<TMTRelation> load(Pageable pageAble) {
                if (keyword != null && !keyword.isEmpty()) {
                    List<String> keywords = Arrays.asList(keyword.split("\\s+"));
                    Specification<TMTRelation> spec = Specifications.where(TMTRelationSpecs.tmtIdContains(keywords)).or(TMTRelationSpecs.fsnContains(keywords));
                    if (typeIn != null) {
                        spec = Specifications.where(spec).and(TMTRelationSpecs.typeIn(typeIn));
                    }
                    return tmtRelationService.findBySpec(spec, pageAble);
                } else {
                    return tmtRelationService.findAll(pageAble);
                }
            }
        };
    }

    public void search() {
        if (filterType == null) {
            tmtParents = searchTMTDrug(keyword, allType);
        } else {
            List<TMTDrug.Type> filterTypes = new ArrayList<>();
            filterTypes.add(filterType);
            tmtParents = searchTMTDrug(keyword, filterTypes);
        }

    }

    private SpringDataLazyDataModelSupport<TMTDrug> searchTMTDrug(final String keyword, final List<TMTDrug.Type> typeIn) {
        return new SpringDataLazyDataModelSupport<TMTDrug>() {
            @Override
            public Page<TMTDrug> load(Pageable pageAble) {
                if (keyword != null && !keyword.isEmpty()) {
                    List<String> keywords = Arrays.asList(keyword.split("\\s+"));
                    Specification<TMTDrug> spec = Specifications.where(TMTDrugSpecs.tmtIdContains(keywords)).or(TMTDrugSpecs.fsnContains(keywords));
                    if (typeIn != null) {
                        spec = Specifications.where(spec).and(TMTDrugSpecs.typeIn(typeIn));
                    }
                    return tmtDrugService.findAllAndEagerGroup(spec, pageAble);
                } else {
                    return tmtDrugService.findAll(pageAble);
                }
            }
        };
    }

    public String onEdit(TMTDrug tmtDrug) {
        if (tmtDrug != null) {
            selectedTMTParent = tmtDrug;

            tmtChildren = searchTMTDrug(selectedTMTParent.getFsn(), findChildTypeFromParentType(selectedTMTParent.getType()));
            beforeEditTMTChildren = new ArrayList<>(selectedTMTParent.getChildren());
            selectedTMTChildren = new HashSet<>(selectedTMTParent.getChildren());
        }
        return "mapping.xhtml";
    }

    private String getActiveIngredientFrom(TMTDrug drug) {
        FSNSplitter splitter = new FSNSplitter();
        splitter.getActiveIngredientAndStrengthFromFSN(drug);
        StringBuilder sb = new StringBuilder();
        for (String activeIngredient : splitter.getActiveIngredients()) {
            sb.append(activeIngredient).append(" ");
        }
        return sb.toString();
    }

    public void onSelectParentTMT(TMTDrug tmt) {
        if (selectedTMTChildren == null) {
            selectedTMTChildren = new HashSet<>();
        } else {
            selectedTMTChildren.clear();
        }
        selectedTMTParent = tmt;
        beforeEditTMTChildren = new ArrayList<>(selectedTMTParent.getChildren());
        LOG.debug("selected Parent TMT : {}", selectedTMTParent.getTmtId());
        searchChildrenTMT();
    }

    private List<TMTDrug.Type> findChildTypeFromParentType(TMTDrug.Type parentType) {
        List<TMTDrug.Type> childType = new ArrayList<>();
        if (parentType.equals(TMTDrug.Type.SUB)) {
            childType.add(TMTDrug.Type.VTM);
        } else if (parentType.equals(TMTDrug.Type.VTM)) {
            childType.add(TMTDrug.Type.GP);
        } else if (parentType.equals(TMTDrug.Type.GP)) {
            childType.add(TMTDrug.Type.GPU);
            childType.add(TMTDrug.Type.TP);
        } else if (parentType.equals(TMTDrug.Type.GPU) || parentType.equals(TMTDrug.Type.TP)) {
            childType.add(TMTDrug.Type.TPU);
        } else {
            return null;
        }
        return childType;
    }

    public void searchChildrenTMT() {
        tmtChildren = searchTMTDrug(selectedTMTParent.getFsn(), findChildTypeFromParentType(selectedTMTParent.getType()));
        selectedTMTChildren.addAll(selectedTMTParent.getChildren());
    }

    public void selectItem(TMTDrug tmtDrug) {
        if (selectedTMTChildren.contains(tmtDrug)) {
            FacesMessageUtils.warn("รายการนี้ถูกเลือกไปแล้ว");
            return;
        }
        selectedTMTChildren.add(tmtDrug);
        LOG.debug("add tmt : {}", tmtDrug.getTmtId());
    }

    public void unSelectItem(TMTDrug tmtDrug) {
        selectedTMTChildren.remove(tmtDrug);
        LOG.debug("remove tmt : {}", tmtDrug.getTmtId());
    }

    public String onSave() {
        deletedTMTChildren = new ArrayList<>();
        for (TMTDrug tmt : beforeEditTMTChildren) {
            if (!selectedTMTChildren.contains(tmt)) {
                deletedTMTChildren.add(tmt);
            }
        }
        return "confirm_mapping_tp_tpu.xhtml";
    }

    public String save() {
        try {
            TMTRelation relation = new TMTRelation();
            relation.setParent(selectedTMTParent);
            for (TMTDrug child : selectedTMTChildren) {
                if (!beforeEditTMTChildren.contains(child)) {
                    TMTRelationID id = new TMTRelationID();
                    id.setParentId(selectedTMTParent.getTmtId());
                    id.setChildId(child.getTmtId());
                    relation.setId(id);
                    relation.setChild(child);
                    tmtRelationService.save(relation);
                }
            }
            delete(relation);
            FacesMessageUtils.info("บันทึก/แก้ไข ข้อมูล เรียบร้อย");

        } catch (Exception e) {
            FacesMessageUtils.error("ไม่สามารถ บันทึก/แก้ไข ได้");
        }
        reset();
        return "tmt_parent_child.xhtml";
    }

    private void delete(TMTRelation relation) {
        LOG.debug("DELETE TMT Parent : {}", relation.getParent().getTmtId());
        for (TMTDrug tmt : deletedTMTChildren) {
            TMTRelationID id = new TMTRelationID();
            id.setParentId(relation.getParent().getTmtId());
            id.setChildId(tmt.getTmtId());
            relation.setId(id);
            relation.setChild(tmt);
            tmtRelationService.delete(relation);
            LOG.debug("DELETE TMT Child : {}", relation.getId().getChildId());
        }
    }

    public void showTMTChildOfSelectParent(TMTRelation relation) {
        selectedTMTParent = relation.getParent();
        viewChildrenTMT = relation.getParent().getChildren();
    }

    public void reset() {
        selectedTMTParent = null;
        selectedTMTChildren.clear();
        keyword = "";
    }

    public void deleteAllRelation() {
        try {
            tmtRelationService.deleteAllRelationByParent(selectRelation);
            FacesMessageUtils.warn("ลบความสัมพันธ์ทั้งหมด เรียบร้อย");
        } catch (Exception e) {
            FacesMessageUtils.warn("ไม่สามารถ ลบความสัมพันธ์ทั้งหมดได้");
        }
    }

    public void onSelectRelation(TMTRelation relation) {
        selectRelation = relation;
    }

    public TMTRelation getSelectRelation() {
        return selectRelation;
    }

    public void setSelectRelation(TMTRelation selectRelation) {
        this.selectRelation = selectRelation;
    }

    public String getKeyword() {
        return keyword;
    }

    public SpringDataLazyDataModelSupport<TMTDrug> getTmtParents() {
        return tmtParents;
    }

    public void setTmtParents(SpringDataLazyDataModelSupport<TMTDrug> tmtParents) {
        this.tmtParents = tmtParents;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public TMTDrug getSelectedTMTParent() {
        return selectedTMTParent;
    }

    public void setSelectedTMTParent(TMTDrug selectedTMTParent) {
        this.selectedTMTParent = selectedTMTParent;
    }

    public Set<TMTDrug> getSelectedTMTChildren() {
        return selectedTMTChildren;
    }

    public void setSelectedTMTChildren(Set<TMTDrug> selectedTMTChildren) {
        this.selectedTMTChildren = selectedTMTChildren;
    }

    public boolean isCheckItem() {
        return checkItem;
    }

    public void setCheckItem(boolean checkItem) {
        this.checkItem = checkItem;
    }

    public SpringDataLazyDataModelSupport<TMTDrug> getTmtChildren() {
        return tmtChildren;
    }

    public void setTmtChildren(SpringDataLazyDataModelSupport<TMTDrug> tmtChildren) {
        this.tmtChildren = tmtChildren;
    }

    public List<TMTDrug.Type> getAllType() {
        return allType;
    }

    public TMTDrug.Type getFilterType() {
        return filterType;
    }

    public void setFilterType(TMTDrug.Type filterType) {
        this.filterType = filterType;
    }

    public List<TMTDrug> getBeforeEditTMTChildren() {
        return beforeEditTMTChildren;
    }

    public void setBeforeEditTMTChildren(List<TMTDrug> beforeEditTMTChildren) {
        this.beforeEditTMTChildren = beforeEditTMTChildren;
    }

    public SpringDataLazyDataModelSupport<TMTRelation> getTmtRelations() {
        return tmtRelations;
    }

    public void setTmtRelations(SpringDataLazyDataModelSupport<TMTRelation> tmtRelations) {
        this.tmtRelations = tmtRelations;
    }

    public List<TMTDrug> getDeletedTMTChildren() {
        return deletedTMTChildren;
    }

    public void setDeletedTMTChildren(List<TMTDrug> deletedTMTChildren) {
        this.deletedTMTChildren = deletedTMTChildren;
    }

    public List<TMTDrug> getViewChildrenTMT() {
        return viewChildrenTMT;
    }

    public void setViewChildrenTMT(List<TMTDrug> viewChildrenTMT) {
        this.viewChildrenTMT = viewChildrenTMT;
    }

}
