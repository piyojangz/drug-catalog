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
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import javax.annotation.PostConstruct;
import net.java.truevfs.access.TArchiveDetector;
import net.java.truevfs.access.TFile;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import th.co.geniustree.nhso.drugcatalog.controller.utils.DateUtils;
import th.co.geniustree.nhso.drugcatalog.controller.utils.FacesMessageUtils;
import th.co.geniustree.nhso.drugcatalog.input.GenericDrugExcelModel;
import th.co.geniustree.nhso.drugcatalog.model.TMTDrug;
import th.co.geniustree.nhso.drugcatalog.model.TMTDrug.Type;
import th.co.geniustree.nhso.drugcatalog.model.TMTReleaseFileUpload;
import th.co.geniustree.nhso.drugcatalog.input.TradeDrugExcelModel;
import th.co.geniustree.nhso.drugcatalog.input.Typeable;
import th.co.geniustree.nhso.drugcatalog.repo.TMTReleaseFileUploadRepo;
import th.co.geniustree.nhso.drugcatalog.service.TMTRFService;
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
    private List<TMTDrug> tmtDrugs = new ArrayList<>();
    private List<TradeDrugExcelModel> tp;
    private List<GenericDrugExcelModel> subs;
    private List<GenericDrugExcelModel> vtm;
    private List<GenericDrugExcelModel> gp;
    private List<GenericDrugExcelModel> gpu;
    @Autowired
    @Qualifier("app")
    private Properties app;
    @Autowired
    private TMTRFService tmtrfService;
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

    @PostConstruct
    public void postConstruct() {
        String uploadtempLocation = app.getProperty("uploadtempLocation");
        uploadtempFileDir = new File(uploadtempLocation, "TMT");
        if (!uploadtempFileDir.exists()) {
            uploadtempFileDir.mkdirs();
        }
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

    public List<TMTDrug> getTmtDrugs() {
        return tmtDrugs;
    }

    public void setTmtDrugs(List<TMTDrug> tmtDrugs) {
        this.tmtDrugs = tmtDrugs;
    }

    public String save() {
        tmtrfService.save(tmtDrugs, tp, gpu, gp, vtm, subs, releaseDate);
        boolean deleteTempFile = tempFile.delete();
        LOG.info("Delete temp file =>{}, result => {}", tempFile.getAbsolutePath(), deleteTempFile);
        FacesMessageUtils.info("บันทึกเสร็จสิ้น.");
        reset();
        return null;
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

    public String reset() {
        saveFileName = null;
        originalFileName = null;
        tmtDrugs.clear();
        return null;
    }

    public void handleFileUpload(FileUploadEvent event) {
        file = event.getFile();
        root = Files.getNameWithoutExtension(file.getFileName());
        firstFileNamePart = root.substring(0, 5);
        secondFileNamePart = root.substring(5, root.length());
        bonusFolder = firstFileNamePart + "_Bonus" + secondFileNamePart;
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
        gpu = readGenericDrug("GPU", GenericDrugExcelModel.class, Type.GPU);
        gp = readGenericDrug("GP", GenericDrugExcelModel.class, Type.GP);
        vtm = readGenericDrug("VTM", GenericDrugExcelModel.class, Type.VTM);
        subs = readGenericDrug("SUBS", GenericDrugExcelModel.class, Type.SUB);
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
            TFile.cp_r(new TFile(tempFile, root), new File(uploadtempFileDir, firstFileNamePart + secondFileNamePart), TArchiveDetector.NULL);
            LOG.debug("Extract TMT dir: {}", tmtRFFolder.getAbsolutePath());
        } catch (Exception iOException) {
            FacesMessageUtils.error(iOException);
            LOG.error(null, iOException);
        }
    }

    private Map<String, TradeDrugExcelModel> readTPU() {
        final Map<String, TradeDrugExcelModel> tpuDrug = new HashMap<>();
        File tpu = new File(tmtRFFolder, bonusFolder + "/Concept/TPU" + secondFileNamePart + ".xls");
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
            FacesMessageUtils.error(columnNotFound);
        } catch (Exception iOException) {
            FacesMessageUtils.error(iOException);
            LOG.error(null, iOException);
        }
        return tpuDrug;
    }

    private <T extends Typeable> List<T> readGenericDrug(String preix, Class<T> beanClass, final Type type) {
        final List<T> genericDrugs = new ArrayList<>();
        File tpu = new File(tmtRFFolder, bonusFolder + "/Concept/" + preix + secondFileNamePart + ".xls");
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
            FacesMessageUtils.error(columnNotFound);
        } catch (Exception iOException) {
            FacesMessageUtils.error(iOException);
            LOG.error(null, iOException);
        }
        return genericDrugs;
    }

    private void createTMTMaster() {
        final Map<String, TradeDrugExcelModel> tpuDrugs = readTPU();
        File tpu = new File(tmtRFFolder, bonusFolder + "/MasterTMT_" + secondFileNamePart + ".xls");
        LOG.debug("Master file: {}", tpu.getAbsolutePath());
        try {
            ReaderUtils.read(tpu, TMTDrug.class, new ReadCallback<TMTDrug>() {
                @Override
                public void header(List<String> headers) {
                    LOG.debug("HEADERS = {}", headers);
                }

                @Override
                public void ok(int rowNum, TMTDrug bean) {
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
            FacesMessageUtils.error(columnNotFound);
        } catch (Exception iOException) {
            FacesMessageUtils.error(iOException);
            LOG.error(null, iOException);
        }
    }

}
