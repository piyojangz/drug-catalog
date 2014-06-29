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
import java.util.List;
import java.util.Properties;
import java.util.UUID;
import javax.annotation.PostConstruct;
import org.primefaces.model.UploadedFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import th.co.geniustree.nhso.drugcatalog.controller.utils.FacesMessageUtils;
import th.co.geniustree.nhso.drugcatalog.model.tmt.TMTDrug;
import th.co.geniustree.nhso.drugcatalog.repo.TMTDrugRepo;
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
    private List<TMTDrug> models = new ArrayList<>();
    @Autowired
    @Qualifier("app")
    private Properties app;
    @Autowired
    private TMTDrugRepo tmtDrugRepo;
    private File uploadtempFileDir;
    private String saveFileName;
    private String originalFileName;

    @PostConstruct
    public void postConstruct() {
        String uploadtempLocation = app.getProperty("uploadtempLocation");
        uploadtempFileDir = new File(uploadtempLocation);
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

    public List<TMTDrug> getModels() {
        return models;
    }

    public void setModels(List<TMTDrug> models) {
        this.models = models;
    }
    

    public String save() {
        tmtDrugRepo.save(models);
        FacesMessageUtils.info("Save completed.");
        reset();
        return null;
    }

    public String reset() {
        saveFileName = null;
        originalFileName = null;
        models.clear();
        return null;
    }

    public String upload() {
        models.clear();
        //TODO file == null NOT work in prime fileupload(simple) and require NOT work too.
        if (file.getFileName() == null || file.getFileName().isEmpty()) {
            FacesMessageUtils.info("Please select file first.");
            return null;
        }
        try (InputStream inputFileStream = file.getInputstream()) {
            originalFileName = file.getFileName();
            saveFileName = UUID.randomUUID().toString() + "-" + file.getFileName();
            File targetFile = new File(uploadtempFileDir, saveFileName);
            LOG.debug("save target file to = {}" + targetFile.getAbsolutePath());
            Files.asByteSink(targetFile).writeFrom(inputFileStream);

            ReaderUtils.read(targetFile, TMTDrug.class, new ReadCallback<TMTDrug>() {
                int rowNum = 0;

                @Override
                public void header(List<String> headers) {
                    LOG.debug("HEADERS = {}", headers);
                    rowNum++;
                }

                @Override
                public void ok(int rowNum, TMTDrug bean) {
                    models.add(bean);
                }

                @Override
                public void err(Exception e) {
                    LOG.error(null, e);
                }
            });
            FacesMessageUtils.info("Parse file completed. Please press 'Save' button.");
        } catch (ColumnNotFoundException columnNotFound) {
            FacesMessageUtils.error(columnNotFound);
        } catch (Exception iOException) {
            throw new RuntimeException(iOException);
        }
        LOG.debug(
                "File : {}", file);

        return null;
    }
}
