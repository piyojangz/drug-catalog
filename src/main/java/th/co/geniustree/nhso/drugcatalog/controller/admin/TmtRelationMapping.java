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
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.PostConstruct;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DualListModel;
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
    private boolean checkItem;
    private TMTDrug.Type filterType;

    private DualListModel<TMTDrug> dualTMTChildren;

    private final List<TMTDrug.Type> allTypes = Arrays.asList(new TMTDrug.Type[]{TMTDrug.Type.SUB, TMTDrug.Type.VTM, TMTDrug.Type.GP, TMTDrug.Type.GPU, TMTDrug.Type.TP});

    @PostConstruct
    public void postConstruct() {
        keyword = "";
        selectedTMTChildren = new HashSet<>();
        tmtParents = searchTMTDrug(null, allTypes);
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

    public void openSearchTMTDialog() {
        Map<String, Object> options = new HashMap<String, Object>();
        options.put("modal", true);
        options.put("draggable", true);
        options.put("resizable", true);
        options.put("contentHeight", 500);
        options.put("contentWidth", 1000);
        Map<String, List<String>> params = new HashMap<String, List<String>>();
        RequestContext.getCurrentInstance().openDialog("/private/hospital/create/selectDrugDialog.xhtml", options, params);
    }

    public void tmtDrugDialogReturn(SelectEvent event) {
        selectedTMTParent = (TMTDrug) event.getObject();
        findChildrenOfSelectParent(selectedTMTParent);
        dualTMTChildren = new DualListModel<>(searchTMTDrug(),children);
    }

    private List<TMTDrug> searchTMTDrug() {
        Specification spec = Specifications.where(TMTDrugSpecs.typeIn(findChildTypeFromParentType(selectedTMTParent.getType())));
        return tmtDrugService.findBySpec(spec);
    }

    public void deleteByParent() {
        try {
            selectedTMTParent.setChildren(null);
            tmtRelationService.deleteAllChildren(selectedTMTParent);
            FacesMessageUtils.info("ลบความสัมพันธ์ เรียบร้อย");
        } catch (Exception e) {
            LOG.error("Can't delete", e);
            FacesMessageUtils.error("ไม่สามารถลบความสัมพันธ์ได้");
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

    public void selectParentTMT(TMTDrug tmtDrug){
        selectedTMTParent = tmtDrug;
        findChildrenOfSelectParent(selectedTMTParent);
    }
    
    public void viewPreviousParent() {
        if (selectedTMTParent.getParents().size() == 1) {
            selectedTMTParent = selectedTMTParent.getParents().get(0);
            findChildrenOfSelectParent(selectedTMTParent);
        }
    }

    private void findChildrenOfSelectParent(TMTDrug parent) {
        children = new LinkedList<>();
        if (parent.getChildren() != null) {
            children.addAll(parent.getChildren());
        }
    }

    private List<TMTDrug.Type> findChildTypeFromParentType(TMTDrug.Type parentType) {
        List<TMTDrug.Type> childType = new LinkedList<>();
        if (parentType.equals(TMTDrug.Type.SUB)) {
            childType.add(TMTDrug.Type.VTM);
//            childType.add(TMTDrug.Type.GP);
//            childType.add(TMTDrug.Type.GPU);
//            childType.add(TMTDrug.Type.TP);
//            childType.add(TMTDrug.Type.TPU);
        } else if (parentType.equals(TMTDrug.Type.VTM)) {
            childType.add(TMTDrug.Type.GP);
//            childType.add(TMTDrug.Type.GPU);
//            childType.add(TMTDrug.Type.TP);
//            childType.add(TMTDrug.Type.TPU);
        } else if (parentType.equals(TMTDrug.Type.GP)) {
            childType.add(TMTDrug.Type.GPU);
            childType.add(TMTDrug.Type.TP);
//            childType.add(TMTDrug.Type.TPU);
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

    public void reset() {
        selectedTMTParent = null;
        selectedTMTChildren.clear();
        keyword = "";
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

    public DualListModel<TMTDrug> getDualTMTChildren() {
        return dualTMTChildren;
    }

    public void setDualTMTChildren(DualListModel<TMTDrug> dualTMTChildren) {
        this.dualTMTChildren = dualTMTChildren;
    }

}
