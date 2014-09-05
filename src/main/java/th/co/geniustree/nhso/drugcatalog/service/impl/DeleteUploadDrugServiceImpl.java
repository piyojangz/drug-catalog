/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.service.impl;

import com.google.common.io.Files;
import java.io.File;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import th.co.geniustree.nhso.drugcatalog.model.HospitalDrug;
import th.co.geniustree.nhso.drugcatalog.model.UploadHospitalDrug;
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

    @Autowired
    private UploadHospitalDrugRepo uploadHospitalDrugRepo;
    @Autowired
    private HospitalDrugRepo hospitalDrugRepo;
    @Autowired
    @Qualifier("app")
    private Properties app;

    @Override
    public void delete(String hcode) {
        Long countByHcodeAndApproved = hospitalDrugRepo.countByHcodeAndApproved(hcode, true);
        if(countByHcodeAndApproved > 0){
            throw new RuntimeException("มีการ approve ยาของ รพ. นี้แล้ว จะไม่สามารถลบข้อมูลได้ ต้องทำการแก้ไขข้อมูลเท่านั้น");
        }
        File uploadDir = createOrFindDir("UPLOAD");
        File deleteDir = createOrFindDir("DELETED");
        Page<UploadHospitalDrug> uploadHospitaDrugs = uploadHospitalDrugRepo.findByHcode(hcode, new PageRequest(0, 100_000));
        for (UploadHospitalDrug uploadHospitalDrug : uploadHospitaDrugs) {
            try {
                File savedFile = new File(uploadDir, uploadHospitalDrug.getSavedFileName());
                File deleteFileLocation = new File(deleteDir, uploadHospitalDrug.getSavedFileName());
                Files.move(savedFile, deleteFileLocation);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
        uploadHospitalDrugRepo.delete(uploadHospitaDrugs.getContent());
        Page<HospitalDrug> hospitalDrugs = hospitalDrugRepo.findByHcode(hcode, new PageRequest(0, 100_000));
        hospitalDrugRepo.delete(hospitalDrugs.getContent());
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
