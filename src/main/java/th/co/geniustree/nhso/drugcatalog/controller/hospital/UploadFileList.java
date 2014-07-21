/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.controller.hospital;

import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import org.primefaces.model.LazyDataModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import th.co.geniustree.nhso.drugcatalog.authen.SecurityUtil;
import th.co.geniustree.nhso.drugcatalog.controller.SpringDataLazyDataModelSupport;
import th.co.geniustree.nhso.drugcatalog.model.UploadHospitalDrug;
import th.co.geniustree.nhso.drugcatalog.repo.UploadHospitalDrugRepo;

/**
 *
 * @author moth
 */
@Scope("view")
@Component
public class UploadFileList implements Serializable {

    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(UploadFileList.class);
    @Autowired
    private UploadHospitalDrugRepo uploadFileRepo;
    private LazyDataModel<UploadHospitalDrug> uploadHospitalDrugs;

    @PostConstruct
    public void postConstruct() {
        final String hcode = SecurityUtil.getUserDetails().getOrgId();
        LOG.debug("get upload history for {}", hcode);
        uploadHospitalDrugs = new SpringDataLazyDataModelSupport<UploadHospitalDrug>() {

            @Override
            public Page<UploadHospitalDrug> load(Pageable pageAble) {
                return uploadFileRepo.findByHcode(hcode, pageAble);
            }

        };
    }

    public LazyDataModel<UploadHospitalDrug> getUploadHospitalDrugs() {
        return uploadHospitalDrugs;
    }

    public void setUploadHospitalDrugs(LazyDataModel<UploadHospitalDrug> uploadHospitalDrugs) {
        this.uploadHospitalDrugs = uploadHospitalDrugs;
    }

}
