/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.controller.admin;

import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import th.co.geniustree.nhso.drugcatalog.controller.SpringDataLazyDataModelSupport;
import th.co.geniustree.nhso.drugcatalog.controller.utils.FacesMessageUtils;
import th.co.geniustree.nhso.drugcatalog.model.MatchTMTNDC24;
import th.co.geniustree.nhso.drugcatalog.model.NDC24;
import th.co.geniustree.nhso.drugcatalog.model.TMTDrug;
import th.co.geniustree.nhso.drugcatalog.repo.MatchTMTNDC24Repo;
import th.co.geniustree.nhso.drugcatalog.service.NDC24Service;
import th.co.geniustree.nhso.drugcatalog.service.TMTDrugService;

/**
 *
 * @author Thanthathon
 */
@Component
@Scope("view")
public class MatchTMTNDC24Controller implements Serializable {

    private static final Logger LOG = LoggerFactory.getLogger(MatchTMTNDC24Controller.class);

    private SpringDataLazyDataModelSupport<TMTDrug> tmtDrug;

    private SpringDataLazyDataModelSupport<MatchTMTNDC24> matchTMTNDC24s;

    private TMTDrug selectedTMT;
    private NDC24 selectedNDC24;
    private MatchTMTNDC24 selectedMatchTMTNDC24;

    @Autowired
    private TMTDrugService tmtDrugService;

    @Autowired
    private NDC24Service ndc24Service;

    @Autowired
    private MatchTMTNDC24Repo matchTMTNDC24Repo;

    public String searchWord;

    @PostConstruct
    public void initial() {
        searchMatch();
    }

    public void searchTMT() {
        tmtDrug = new SpringDataLazyDataModelSupport<TMTDrug>() {
            @Override
            public Page<TMTDrug> load(Pageable pageAble) {
                if (searchWord == null || searchWord.isEmpty()) {
                    return tmtDrugService.findAll(pageAble);
                } else {
                    return tmtDrugService.search(searchWord, pageAble);
                }
            }
        };
    }

    public void searchMatch() {
        matchTMTNDC24s = new SpringDataLazyDataModelSupport<MatchTMTNDC24>() {
            @Override
            public Page<MatchTMTNDC24> load(Pageable pageAble) {
                if (searchWord == null || searchWord.isEmpty()) {
                    return matchTMTNDC24Repo.findAll(pageAble);
                } else {
                    return matchTMTNDC24Repo.findByTmtidContainsOrNdc24Contains(searchWord, searchWord, pageAble);
                }
            }
        };
    }

    public void save() {
        try {
//            List<NDC24> list = selectedTMT.getNdc24s();
//            list.add(selectedNDC24);
//            selectedTMT.setNdc24s(list);
//            tmtDrugService.save(selectedTMT);
            MatchTMTNDC24 m = new MatchTMTNDC24();
            m.setTmtid(selectedTMT.getTmtId());
            m.setNdc24(selectedNDC24.getNdc24());
            matchTMTNDC24Repo.save(m);
            FacesMessageUtils.info("บันทึกข้อมูล สำเร็จ");
        } catch (Exception e) {
            FacesMessageUtils.error("ไม่สามารถบันทึกข้อมูลได้");
        }
//        searchMatch();
    }

    public void edit() {
        try {
            selectedMatchTMTNDC24.setNdc24(selectedNDC24.getNdc24());
            matchTMTNDC24Repo.save(selectedMatchTMTNDC24);
            FacesMessageUtils.info("แก้ไขข้อมูล สำเร็จ");
        } catch (Exception e) {
            FacesMessageUtils.error("ไม่สามารถแก้ไขข้อมูลได้");
        }
    }

    public void delete() {
        try {
            matchTMTNDC24Repo.delete(selectedMatchTMTNDC24);
            FacesMessageUtils.info("ลบข้อมูล สำเร็จ");
        } catch (Exception e) {
            FacesMessageUtils.error("ไม่สามารถลบข้อมูลได้");
        }
    }

    public void onSelect(MatchTMTNDC24 item) {
        selectedMatchTMTNDC24 = item;
        selectedNDC24 = null;
        LOG.debug("selected tmt : {}\tndc24 : {}", selectedMatchTMTNDC24.getTmtid(), selectedMatchTMTNDC24.getNdc24());
    }

    public List<NDC24> completeNDC24(String query) {
        return ndc24Service.search(query);
    }

    public void selectTMT(TMTDrug tmtDrug) {
        selectedTMT = tmtDrug;
        searchWord = tmtDrug.getTmtId();
        LOG.debug("selected TMT : {}", selectedTMT.getTmtId());
    }

    public SpringDataLazyDataModelSupport<TMTDrug> getTmtDrug() {
        return tmtDrug;
    }

    public void setTmtDrug(SpringDataLazyDataModelSupport<TMTDrug> tmtDrug) {
        this.tmtDrug = tmtDrug;
    }

    public TMTDrug getSelectedTMT() {
        return selectedTMT;
    }

    public void setSelectedTMT(TMTDrug selectedTMT) {
        this.selectedTMT = selectedTMT;
    }

    public String getSearchWord() {
        return searchWord;
    }

    public void setSearchWord(String searchWord) {
        this.searchWord = searchWord;
    }

    public NDC24 getSelectedNDC24() {
        return selectedNDC24;
    }

    public void setSelectedNDC24(NDC24 selectedNDC24) {
        this.selectedNDC24 = selectedNDC24;
    }

    public SpringDataLazyDataModelSupport<MatchTMTNDC24> getMatchTMTNDC24s() {
        return matchTMTNDC24s;
    }

    public void setMatchTMTNDC24s(SpringDataLazyDataModelSupport<MatchTMTNDC24> matchTMTNDC24s) {
        this.matchTMTNDC24s = matchTMTNDC24s;
    }

    public MatchTMTNDC24 getSelectedMatchTMTNDC24() {
        return selectedMatchTMTNDC24;
    }

    public void setSelectedMatchTMTNDC24(MatchTMTNDC24 selectedMatchTMTNDC24) {
        this.selectedMatchTMTNDC24 = selectedMatchTMTNDC24;
    }

}
