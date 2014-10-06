/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.service;

import org.springframework.stereotype.Service;
import th.co.geniustree.nhso.drugcatalog.model.HospitalDrug;
import th.co.geniustree.nhso.drugcatalog.model.TMTDrug;

/**
 *
 * @author moth
 */

public interface TmtDrugTxService {
    public void addNewTmtDrugTx(HospitalDrug hospitalDrug,TMTDrug tmtDrug);
}
