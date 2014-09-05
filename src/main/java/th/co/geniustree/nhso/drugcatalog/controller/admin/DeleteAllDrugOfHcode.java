/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.controller.admin;

import java.io.Serializable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import th.co.geniustree.nhso.drugcatalog.controller.utils.FacesMessageUtils;
import th.co.geniustree.nhso.drugcatalog.service.DeleteUploadDrugService;

/**
 *
 * @author moth
 */
@Component
@Scope("request")
public class DeleteAllDrugOfHcode implements Serializable {

    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(DeleteAllDrugOfHcode.class);
    @Autowired
    private DeleteUploadDrugService deleteUploadDrugService;

    public String delete(String hcode) {
        LOG.info("prepare to delete hcode {}", hcode);
        try {
            deleteUploadDrugService.delete(hcode);
            FacesMessageUtils.info("Delete completed.");
        } catch (Exception e) {
            FacesMessageUtils.error("Can't delete." + e.getMessage());
        }
        return "/private/admin/report/import-report.xhtml?faces-redirect=true";
    }
}
