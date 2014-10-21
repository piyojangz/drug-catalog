/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.service.impl;

import com.google.common.io.Files;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import th.co.geniustree.nhso.drugcatalog.authen.SecurityUtil;
import th.co.geniustree.nhso.drugcatalog.model.DeleteLog;
import th.co.geniustree.nhso.drugcatalog.model.HospitalDrug;
import th.co.geniustree.nhso.drugcatalog.model.UploadHospitalDrug;
import th.co.geniustree.nhso.drugcatalog.repo.DeleteLogRepo;
import th.co.geniustree.nhso.drugcatalog.repo.HospitalDrugRepo;
import th.co.geniustree.nhso.drugcatalog.repo.UploadHospitalDrugRepo;
import th.co.geniustree.nhso.drugcatalog.service.DeleteUploadDrugService;

/**
 *
 * @author moth
 */
@Service
@Transactional(propagation = Propagation.REQUIRED)
public class DeleteUploadDrugServiceImpl implements DeleteUploadDrugService {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(DeleteUploadDrugServiceImpl.class);
    @Autowired
    private UploadHospitalDrugRepo uploadHospitalDrugRepo;
    @Autowired
    private HospitalDrugRepo hospitalDrugRepo;
    @Autowired
    @Qualifier("app")
    private Properties app;
    @Autowired
    private DeleteLogRepo deleteLogRepo;

    @Override
    public void delete(String hcode) {
        File uploadDir = createOrFindDir("UPLOAD");
        File deleteDir = createOrFindDir("DELETED");
        Page<UploadHospitalDrug> uploadHospitaDrugs = uploadHospitalDrugRepo.findByHcode(hcode, new PageRequest(0, 100_000));
        DeleteLog deleteLog = new DeleteLog(SecurityUtil.getUserDetails().getPid(), hcode, new Date());
        for (UploadHospitalDrug uploadHospitalDrug : uploadHospitaDrugs) {
            try {
                deleteLog.addFile(uploadHospitalDrug.getSavedFileName());
                File savedFile = new File(uploadDir, uploadHospitalDrug.getSavedFileName());
                File deleteFileLocation = new File(deleteDir, uploadHospitalDrug.getSavedFileName());
                Files.move(savedFile, deleteFileLocation);
            } catch (IOException ex) {
                LOG.warn(ex.getMessage());
            }
        }
        uploadHospitalDrugRepo.delete(uploadHospitaDrugs.getContent());
        Page<HospitalDrug> hospitalDrugs = hospitalDrugRepo.findByHcode(hcode, new PageRequest(0, 100_000));
        hospitalDrugRepo.delete(hospitalDrugs.getContent());
        deleteLogRepo.save(deleteLog);
    }

    private File createOrFindDir(String dir) {
        String uploadtempLocation = app.getProperty("uploadtempLocation");
        File uploadtempFileDir = new File(uploadtempLocation);
        File deleteDir = new File(uploadtempFileDir, dir);
        if (!uploadtempFileDir.exists()) {
            uploadtempFileDir.mkdirs();
        }
        deleteDir.mkdir();
        return deleteDir;
    }

}
