/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.service;

import java.util.Date;
import java.util.List;
import th.co.geniustree.nhso.drugcatalog.model.GenericDrug;
import th.co.geniustree.nhso.drugcatalog.model.TMTDrug;
import th.co.geniustree.nhso.drugcatalog.model.TradeDrug;

/**
 *
 * @author moth
 */
public interface TMTRFService {

    public void save(List<TMTDrug> tmtDrug, List<TradeDrug> tp, List<GenericDrug> gpu, List<GenericDrug> gp, List<GenericDrug> vtm, List<GenericDrug> subs, Date releaseDate);
}
