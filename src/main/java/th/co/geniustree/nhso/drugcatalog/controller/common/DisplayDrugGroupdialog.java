/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.controller.common;

import java.io.Serializable;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
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
public class DisplayDrugGroupdialog implements Serializable {

    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(DisplayDrugGroupdialog.class);
    @Autowired
    private TMTDrugGroupItemRepo tmtDrugGroupItemRepo;
    private String tmtId;
    private List<TMTDrugGroupItem> tmtDrugGroupItems;

    public String getTmtId() {
        return tmtId;
    }

    public void setTmtId(String tmtId) {
        this.tmtId = tmtId;
    }

    public List<TMTDrugGroupItem> getTmtDrugGroupItems() {
        return tmtDrugGroupItems;
    }

    public void setTmtDrugGroupItems(List<TMTDrugGroupItem> tmtDrugGroupItems) {
        this.tmtDrugGroupItems = tmtDrugGroupItems;
    }

    public void load() {
        tmtDrugGroupItems = tmtDrugGroupItemRepo.findByTMTDrugTMTId(tmtId);
    }
}
