/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.service;

import java.util.Date;
import java.util.List;
import th.co.geniustree.nhso.drugcatalog.input.GenericDrugExcelModel;
import th.co.geniustree.nhso.drugcatalog.input.TradeDrugExcelModel;
import th.co.geniustree.nhso.drugcatalog.model.TMTDrugUpload;

/**
 *
 * @author Thanthathon
 */
public interface TMTRFUploadService {

    public void save(List<TMTDrugUpload> tmtDrug, List<TradeDrugExcelModel> tp, List<GenericDrugExcelModel> gpu, List<GenericDrugExcelModel> gp, List<GenericDrugExcelModel> vtm, List<GenericDrugExcelModel> subs, Date releaseDate);

}
