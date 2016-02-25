/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.controller.admin;

import com.google.common.base.Strings;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Component;
import th.co.geniustree.nhso.drugcatalog.controller.SpringDataLazyDataModelSupport;
import th.co.geniustree.nhso.drugcatalog.controller.utils.FacesMessageUtils;
import th.co.geniustree.nhso.drugcatalog.model.TMTDrug;
import th.co.geniustree.nhso.drugcatalog.model.TMTRelation;
import th.co.geniustree.nhso.drugcatalog.model.TMTRelationID;
import th.co.geniustree.nhso.drugcatalog.repo.spec.TMTDrugSpecs;
import th.co.geniustree.nhso.drugcatalog.service.TMTDrugService;
import th.co.geniustree.nhso.drugcatalog.service.TMTRelationService;

/**
 *
 * @author Thanthathon
 */
@Component
@Scope("view")
public class TmtRelationMapping implements Serializable {

    private static final Logger LOG = LoggerFactory.getLogger(TmtRelationMapping.class);

    @Autowired
    private TMTDrugService tmtDrugService;
    @Autowired
    private TMTRelationService tmtRelationService;

    private SpringDataLazyDataModelSupport<TMTDrug> tmtParents;
    private SpringDataLazyDataModelSupport<TMTDrug> tmtChildren;
    private List<TMTDrug> children = new LinkedList<>();
    private List<TMTDrug> viewChildrenTMT;

    private TMTDrug selectedTMTParent;
    private String keyword;
    private Set<TMTDrug> selectedTMTChildren;
    private List<TMTDrug> beforeEditTMTChildren;
    private List<TMTDrug> deletedTMTChildren;
    private boolean checkItem;
    private TMTDrug.Type filterType;
    
    private final List<TMTDrug.Type> allTypes = Arrays.asList(new TMTDrug.Type[]{TMTDrug.Type.SUB, TMTDrug.Type.VTM, TMTDrug.Type.GP, TMTDrug.Type.GPU, TMTDrug.Type.TP});

    @PostConstruct
    public void postConstruct() {
        keyword = "";
        selectedTMTChildren = new HashSet<>();
        tmtParents = searchTMTDrug(null,allTypes);
    }

    public void search() {
        if (filterType == null) {
            tmtParents = searchTMTDrug(keyword, allTypes);
        } else {
            List<TMTDrug.Type> filterTypes = new ArrayList<>();
            filterTypes.add(filterType);
            tmtParents = searchTMTDrug(keyword, filterTypes);
        }

    }

    private SpringDataLazyDataModelSupport<TMTDrug> searchTMTDrug(final String keyword, final List<TMTDrug.Type> types) {
        return new SpringDataLazyDataModelSupport<TMTDrug>(new Sort("tmtId")) {
            @Override
            public Page<TMTDrug> load(Pageable pageAble) {
                Specification<TMTDrug> spec = Specifications.where(null);
                if (!Strings.isNullOrEmpty(keyword)) {
                    List<String> keywords = Arrays.asList(keyword.split("\\s+"));
                    spec = Specifications.where(spec).and(Specifications.where(TMTDrugSpecs.tmtIdContains(keywords)).or(TMTDrugSpecs.fsnContains(keywords)));
                }
                if (types != null) {
                    spec = Specifications.where(spec).and(TMTDrugSpecs.typeIn(types));
                }
                return tmtDrugService.findAllAndEagerGroup(spec, pageAble);
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

    public void onSelectParentTMT(TMTDrug tmt) {
        selectedTMTParent = tmt;
        LOG.debug("selected Parent TMT : {}", selectedTMTParent.getTmtId());
        searchChildrenFromParent(selectedTMTParent);
        LOG.debug("total Children of TMT = {} items", children.size());
    }

    private void searchChildrenFromParent(TMTDrug parent) {
        children = new LinkedList<>();
        if (parent.getChildren() != null) {
            children.addAll(parent.getChildren());
//            for (TMTDrug drug : parent.getChildren()) {
//                searchChildrenFromParent(drug);
//            }
        }
    }

    private List<TMTDrug.Type> findChildTypeFromParentType(TMTDrug.Type parentType) {
        List<TMTDrug.Type> childType = new LinkedList<>();
        if (parentType.equals(TMTDrug.Type.SUB)) {
            childType.add(TMTDrug.Type.VTM);
            childType.add(TMTDrug.Type.GP);
            childType.add(TMTDrug.Type.GPU);
            childType.add(TMTDrug.Type.TP);
            childType.add(TMTDrug.Type.TPU);
        } else if (parentType.equals(TMTDrug.Type.VTM)) {
            childType.add(TMTDrug.Type.GP);
            childType.add(TMTDrug.Type.GPU);
            childType.add(TMTDrug.Type.TP);
            childType.add(TMTDrug.Type.TPU);
        } else if (parentType.equals(TMTDrug.Type.GP)) {
            childType.add(TMTDrug.Type.GPU);
            childType.add(TMTDrug.Type.TP);
            childType.add(TMTDrug.Type.TPU);
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

    public void showTMTChildOfSelectParent(TMTDrug drug) {
        selectedTMTParent = drug;
        viewChildrenTMT = drug.getChildren();
    }

    public void reset() {
        selectedTMTParent = null;
        selectedTMTChildren.clear();
        keyword = "";
    }

    public void deleteAllRelation() {
        try {
//            tmtRelationService.deleteAllRelationByParent(selectRelation);
            FacesMessageUtils.warn("ลบความสัมพันธ์ทั้งหมด เรียบร้อย");
        } catch (Exception e) {
            FacesMessageUtils.warn("ไม่สามารถ ลบความสัมพันธ์ทั้งหมดได้");
        }
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

    public List<TMTDrug> getChildren() {
        return children;
    }

    public void setChildren(List<TMTDrug> children) {
        this.children = children;
    }

}
