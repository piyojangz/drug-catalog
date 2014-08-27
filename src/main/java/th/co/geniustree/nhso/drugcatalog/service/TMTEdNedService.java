/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package th.co.geniustree.nhso.drugcatalog.service;

import java.util.Date;
import java.util.List;
import th.co.geniustree.nhso.drugcatalog.input.ExcelTMTEdNed;

/**
 *
 * @author moth
 */
public interface TMTEdNedService {
    public void save(List<ExcelTMTEdNed> excelTMTEdNeds);

    public boolean exist(String tmtId, Date dateIn);
}
