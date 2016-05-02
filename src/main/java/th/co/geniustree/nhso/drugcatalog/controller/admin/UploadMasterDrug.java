/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.controller.admin;

import com.google.common.io.Files;
import java.io.File;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import net.java.truevfs.access.TArchiveDetector;
import net.java.truevfs.access.TFile;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import th.co.geniustree.nhso.drugcatalog.controller.utils.DateUtils;
import th.co.geniustree.nhso.drugcatalog.controller.utils.FacesMessageUtils;
import th.co.geniustree.nhso.drugcatalog.input.GenericDrugExcelModel;
import th.co.geniustree.nhso.drugcatalog.input.TMTGpToGpu;
import th.co.geniustree.nhso.drugcatalog.input.TMTGpToTp;
import th.co.geniustree.nhso.drugcatalog.input.TMTGpuToTpu;
import th.co.geniustree.nhso.drugcatalog.input.TMTSubsToVtm;
import th.co.geniustree.nhso.drugcatalog.input.TMTTpToTpu;
import th.co.geniustree.nhso.drugcatalog.input.TMTVtmToGp;
import th.co.geniustree.nhso.drugcatalog.model.TMTDrug.Type;
import th.co.geniustree.nhso.drugcatalog.model.TMTReleaseFileUpload;
import th.co.geniustree.nhso.drugcatalog.input.TradeDrugExcelModel;
import th.co.geniustree.nhso.drugcatalog.input.Typeable;
import th.co.geniustree.nhso.drugcatalog.input.UploadRelationshipModel;
import th.co.geniustree.nhso.drugcatalog.model.TMTDrug;
import th.co.geniustree.nhso.drugcatalog.model.TMTDrugUpload;
import th.co.geniustree.nhso.drugcatalog.model.TMTRelation;
import th.co.geniustree.nhso.drugcatalog.repo.TMTReleaseFileUploadRepo;
import th.co.geniustree.nhso.drugcatalog.service.TMTDrugService;
import th.co.geniustree.nhso.drugcatalog.service.TMTRFUploadService;
import th.co.geniustree.nhso.drugcatalog.service.TMTRelationService;
import th.co.geniustree.xls.beans.ColumnNotFoundException;
import th.co.geniustree.xls.beans.ReadCallback;
import th.co.geniustree.xls.beans.ReaderUtils;

/**
 *
 * @author moth
 */
@Component
@Scope("view")
public class UploadMasterDrug implements Serializable {

    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(UploadMasterDrug.class);
    private UploadedFile file;
    private List<TMTDrugUpload> tmtDrugs = new ArrayList<>();
    private List<TradeDrugExcelModel> tp = new ArrayList<>();
    private List<GenericDrugExcelModel> subs = new ArrayList<>();
    private List<GenericDrugExcelModel> vtm = new ArrayList<>();
    private List<GenericDrugExcelModel> gp = new ArrayList<>();
    private List<GenericDrugExcelModel> gpu = new ArrayList<>();

    private List<TMTGpToGpu> gpToGpu = new LinkedList<>();
    private List<TMTGpToTp> gpToTp = new LinkedList<>();
    private List<TMTGpuToTpu> gpuToTpu = new LinkedList<>();
    private List<TMTSubsToVtm> subsToVtm = new LinkedList<>();
    private List<TMTTpToTpu> tpToTpu = new LinkedList<>();
    private List<TMTVtmToGp> vtmToGp = new LinkedList<>();
    @Autowired
    @Qualifier("app")
    private Properties app;
    @Autowired
    private TMTRFUploadService tmtrfService;

    @Autowired
    private TMTRelationService tmtRelationService;

    @Autowired
    private TMTDrugService tmtDrugService;

    private File uploadtempFileDir;
    private String saveFileName;
    private String originalFileName;
    private File tempFile;
    private String firstFileNamePart;
    private String secondFileNamePart;
    private String bonusFolder;
    private String root;
    private File tmtRFFolder;
    @Autowired
    private TMTReleaseFileUploadRepo tmtReleaseFileUploadRepo;
    private Date releaseDate;

    private String latestFile;
    
    @PostConstruct
    public void postConstruct() {
        String uploadtempLocation = app.getProperty("uploadtempLocation");
        uploadtempFileDir = new File(uploadtempLocation, "TMT");
        if (!uploadtempFileDir.exists()) {
            uploadtempFileDir.mkdirs();
        }
        
        TMTReleaseFileUpload findLastestReleaseDate = tmtReleaseFileUploadRepo.findLastestReleaseDate();
        if (findLastestReleaseDate != null) {
            latestFile = "TMTRF" + DateUtils.format("yyyyMMdd", findLastestReleaseDate.getReleaseDate());
        }
    }

    public String getLatestFile() {
        return latestFile;
    }

    public void setLatestFile(String latestFile) {
        this.latestFile = latestFile;
    }

    public String getOriginalFileName() {
        return originalFileName;
    }

    public void setOriginalFileName(String originalFileName) {
        this.originalFileName = originalFileName;
    }

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public List<TMTDrugUpload> getTmtDrugs() {
        return tmtDrugs;
    }

    public void setTmtDrugs(List<TMTDrugUpload> tmtDrugs) {
        this.tmtDrugs = tmtDrugs;
    }

    public String save() {
        try {
            tmtrfService.save(tmtDrugs, tp, gpu, gp, vtm, subs, releaseDate);
            latestFile = originalFileName;
            reset();
            saveRelationship(subsToVtm);
            saveRelationship(vtmToGp);
            saveRelationship(gpToGpu);
            saveRelationship(gpToTp);
            saveRelationship(gpuToTpu);
            saveRelationship(tpToTpu);
            boolean deleteTempFile = tempFile.delete();
            LOG.info("Delete temp file =>{}, result => {}", tempFile.getAbsolutePath(), deleteTempFile);
            FacesMessageUtils.info("บันทึกเสร็จสิ้น.");
        } catch (Exception e) {
            LOG.error(null, e);
            FacesMessageUtils.error("ไม่สามารถบันทึกได้.");
        }
        return null;
    }

    private <T extends UploadRelationshipModel> void saveRelationship(List<T> relationship) {
        List<TMTRelation> relations = new ArrayList<>();
        for (UploadRelationshipModel tmt : relationship) {
            if (tmt.getParentTmtId() == null || tmt.getChildTmtId() == null) {
                // In future , we'll validate on handleFileUpload method and report the not pass model
                continue;
            }
            TMTDrug parent = tmtDrugService.findOneWithoutTx(tmt.getParentTmtId());
            TMTDrug child = tmtDrugService.findOneWithoutTx(tmt.getChildTmtId());
            if (parent == null || child == null) {
                continue;
                // In future , we'll validate on handleFileUpload method and report the not pass model
            }
            TMTRelation r = new TMTRelation(parent, child);
            relations.add(r);
        }
        try {
            tmtRelationService.saveAll(relations);
        } catch (Exception e) {
            LOG.error("Can't save relationship.", e);
        }
        relationship.clear();
    }

    public List<TradeDrugExcelModel> getTp() {
        return tp;
    }

    public List<GenericDrugExcelModel> getSubs() {
        return subs;
    }

    public List<GenericDrugExcelModel> getVtm() {
        return vtm;
    }

    public List<GenericDrugExcelModel> getGp() {
        return gp;
    }

    public List<GenericDrugExcelModel> getGpu() {
        return gpu;
    }

    public List<TMTGpToGpu> getGpToGpu() {
        return gpToGpu;
    }

    public List<TMTGpToTp> getGpToTp() {
        return gpToTp;
    }

    public List<TMTGpuToTpu> getGpuToTpu() {
        return gpuToTpu;
    }

    public List<TMTSubsToVtm> getSubsToVtm() {
        return subsToVtm;
    }

    public List<TMTTpToTpu> getTpToTpu() {
        return tpToTpu;
    }

    public List<TMTVtmToGp> getVtmToGp() {
        return vtmToGp;
    }

    public boolean isCanSave() {
        return !tmtDrugs.isEmpty() || !tp.isEmpty() || !gp.isEmpty() || !gpu.isEmpty() || !subs.isEmpty() || !vtm.isEmpty();
    }

    public String reset() {
        saveFileName = null;
        originalFileName = null;
        tmtDrugs.clear();
        tp.clear();
        subs.clear();
        vtm.clear();
        gp.clear();
        gpu.clear();
        return null;
    }

    public void handleFileUpload(FileUploadEvent event) {
        file = event.getFile();
        root = Files.getNameWithoutExtension(file.getFileName());
        firstFileNamePart = root.substring(0, 5);
        secondFileNamePart = root.substring(5, root.length());
        bonusFolder = firstFileNamePart + secondFileNamePart + "_BONUS";
        releaseDate = DateUtils.parseUSDate("yyyyMMdd", secondFileNamePart);
        TMTReleaseFileUpload lastestRelease = tmtReleaseFileUploadRepo.findLastestReleaseDate();
        if (lastestRelease != null) {
            if (lastestRelease.getReleaseDate().after(releaseDate)) {
                FacesMessageUtils.error("TMTRF file is outdate.");
                return;
            } else if (lastestRelease.getReleaseDate().equals(releaseDate)) {
                FacesMessageUtils.error("TMTRF file is already uploaded.");
                return;
            }

        }
        createTempFile(file);
        createTMTMaster();
        tp = readGenericDrug("TP", TradeDrugExcelModel.class, Type.TP);
        LOG.info("----------------TP size {}(\"----------------", tp.size());
        gpu = readGenericDrug("GPU", GenericDrugExcelModel.class, Type.GPU);
        LOG.info("----------------GPU size {}(\"----------------", gpu.size());
        gp = readGenericDrug("GP", GenericDrugExcelModel.class, Type.GP);
        LOG.info("----------------GP size {}(\"----------------", gp.size());
        vtm = readGenericDrug("VTM", GenericDrugExcelModel.class, Type.VTM);
        LOG.info("----------------VTM size {}(\"----------------", vtm.size());
        subs = readGenericDrug("SUBS", GenericDrugExcelModel.class, Type.SUB);
        LOG.info("----------------SUBS size {}(\"----------------", subs.size());
//        subsToVtm = readRelationShip("SUBStoVTM", TMTSubsToVtm.class, subsToVtm);
//        LOG.info("----------------SUBStoVTM size {}(\"----------------", subsToVtm.size());
//        vtmToGp = readRelationShip("VTMtoGP", TMTVtmToGp.class, vtmToGp);
//        LOG.info("----------------VTMtoGP size {}(\"----------------", vtmToGp.size());
//        gpToGpu = readRelationShip("GPtoGPU", TMTGpToGpu.class, gpToGpu);
//        LOG.info("----------------GPtoGPU size {}(\"----------------", gpToGpu.size());
//        gpToTp = readRelationShip("GPtoTP", TMTGpToTp.class, gpToTp);
//        LOG.info("----------------GPtoTP size {}(\"----------------", gpToTp.size());
//        gpuToTpu = readRelationShip("GPUtoTPU", TMTGpuToTpu.class, gpuToTpu);
//        LOG.info("----------------GPUtoTPU size {}(\"----------------", gpuToTpu.size());
//        tpToTpu = readRelationShip("TPtoTPU", TMTTpToTpu.class, tpToTpu);
//        LOG.info("----------------TPtoTPU size {}(\"----------------", tpToTpu.size());
    }

    private void createTempFile(UploadedFile file) {
        try (InputStream inputFileStream = file.getInputstream()) {
            originalFileName = file.getFileName();
            saveFileName = UUID.randomUUID().toString() + "-" + file.getFileName();
            tempFile = new File(uploadtempFileDir, saveFileName);
            LOG.debug("save target file to = {}", tempFile.getAbsolutePath());
            Files.asByteSink(tempFile).writeFrom(inputFileStream);
            tmtRFFolder = new File(uploadtempFileDir, firstFileNamePart + secondFileNamePart);
            tmtRFFolder.mkdir();
            //TMTRF is create by human and we must check if to try to import.
            TFile mostOuter = new TFile(tempFile);
            TFile[] listFiles = mostOuter.listFiles();
            if (listFiles.length == 1) {
                TFile.cp_r(new TFile(tempFile, root), new File(uploadtempFileDir, firstFileNamePart + secondFileNamePart), TArchiveDetector.NULL);
            } else {
                TFile.cp_r(new TFile(tempFile), new File(uploadtempFileDir, firstFileNamePart + secondFileNamePart), TArchiveDetector.NULL);
            }
            LOG.debug("Extract TMT dir: {}", tmtRFFolder.getAbsolutePath());
        } catch (Exception iOException) {
            FacesMessageUtils.error(iOException);
            LOG.error(null, iOException);
        }
    }

    private Map<String, TradeDrugExcelModel> readTPU() {
        final Map<String, TradeDrugExcelModel> tpuDrug = new HashMap<>();
        File tpu = new File(tmtRFFolder, bonusFolder + "/Concept/TPU" + secondFileNamePart + ".xls");
        if (!tpu.exists()) {
            tpu = new File(tmtRFFolder, bonusFolder + "/Concepts/TPU" + secondFileNamePart + ".xls");
        }
        try {
            ReaderUtils.read(tpu, TradeDrugExcelModel.class, new ReadCallback<TradeDrugExcelModel>() {
                @Override
                public void header(List<String> headers) {
                    LOG.debug("HEADERS = {}", headers);
                }

                @Override
                public void ok(int rowNum, TradeDrugExcelModel bean) {
                    tpuDrug.put(bean.getTmtId(), bean);
                }

                @Override
                public void err(Exception e) {
                    LOG.error(null, e);
                }
            });
        } catch (ColumnNotFoundException columnNotFound) {
            FacesMessageUtils.warn(columnNotFound);
        } catch (Exception iOException) {
            FacesMessageUtils.warn(iOException);
            LOG.error(null, iOException);
        }
        return tpuDrug;
    }

    private <T extends Typeable> List<T> readGenericDrug(String preix, Class<T> beanClass, final Type type) {
        final List<T> genericDrugs = new ArrayList<>();
        File tpu = new File(tmtRFFolder, bonusFolder + "/Concept/" + preix + secondFileNamePart + ".xls");
        if (!tpu.exists()) {
            tpu = new File(tmtRFFolder, bonusFolder + "/Concepts/" + preix + secondFileNamePart + ".xls");
        }
        LOG.debug("file: {}", tpu.getAbsolutePath());
        try {
            ReaderUtils.read(tpu, beanClass, new ReadCallback<T>() {
                @Override
                public void header(List<String> headers) {
                    LOG.debug("HEADERS = {}", headers);
                }

                @Override
                public void ok(int rowNum, T bean) {
                    bean.setType(type);
                    genericDrugs.add(bean);
                }

                @Override
                public void err(Exception e) {
                    LOG.error(null, e);
                }
            });
        } catch (ColumnNotFoundException columnNotFound) {
            FacesMessageUtils.warn(columnNotFound);
        } catch (Exception iOException) {
            FacesMessageUtils.warn(iOException);
            LOG.error(null, iOException);
        }
        return genericDrugs;
    }

    private <T extends UploadRelationshipModel> List<T> readRelationShip(String prefix, Class<T> beanClass, final List<T> tmtRelations) {
        tmtRelations.clear();
        File file = new File(tmtRFFolder, bonusFolder + "/Relationship/" + prefix + secondFileNamePart + ".xls");
        if (!file.exists()) {
            file = new File(tmtRFFolder, bonusFolder + "/Relationships/" + prefix + secondFileNamePart + ".xls");
        }
        LOG.debug("file: {}", file.getAbsolutePath());
        try {
            ReaderUtils.read(file, beanClass, new ReadCallback<T>() {
                @Override
                public void header(List<String> headers) {
                    LOG.debug("HEADERS = {}", headers);
                }

                @Override
                public void ok(int rowNum, T bean) {
                    tmtRelations.add(bean);
                }

                @Override
                public void err(Exception e) {
                    LOG.error(null, e);
                }
            });
        } catch (ColumnNotFoundException columnNotFound) {
            FacesMessageUtils.warn(columnNotFound);
        } catch (Exception iOException) {
            FacesMessageUtils.warn(iOException);
            LOG.error(null, iOException);
        }
        return tmtRelations;
    }

    private void createTMTMaster() {
        final Map<String, TradeDrugExcelModel> tpuDrugs = readTPU();
        File tpu = new File(tmtRFFolder, bonusFolder + "/MasterTMT_" + secondFileNamePart + ".xls");
        LOG.debug("Master file: {}", tpu.getAbsolutePath());
        try {
            ReaderUtils.read(tpu, TMTDrugUpload.class, new ReadCallback<TMTDrugUpload>() {
                @Override
                public void header(List<String> headers) {
                    LOG.debug("HEADERS = {}", headers);
                }

                @Override
                public void ok(int rowNum, TMTDrugUpload bean) {
                    String changeDate = tpuDrugs.get(bean.getTmtId()).getChangeDateString();
                    bean.setChageDate(DateUtils.parseUSDate("yyyyMMdd", changeDate));
                    bean.setType(TMTDrug.Type.TPU);
                    tmtDrugs.add(bean);
                }

                @Override
                public void err(Exception e) {
                    LOG.error(null, e);
                }
            });
        } catch (ColumnNotFoundException columnNotFound) {
            FacesMessageUtils.warn(columnNotFound);
        } catch (Exception iOException) {
            FacesMessageUtils.warn(iOException);
            LOG.error(null, iOException);
        }
        LOG.info("----------------TPU size {}(\"----------------", tmtDrugs.size());
    }

}
