/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.controller.admin;

import com.google.common.base.CharMatcher;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.io.Files;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import javax.annotation.PostConstruct;
import net.java.truevfs.access.TArchiveDetector;
import net.java.truevfs.access.TConfig;
import net.java.truevfs.access.TFile;
import net.java.truevfs.comp.zipdriver.ZipDriver;
import org.apache.commons.io.FileUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.poifs.filesystem.NPOIFSFileSystem;
import org.apache.poi.poifs.filesystem.OfficeXmlFileException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import th.co.geniustree.nhso.drugcatalog.authen.SecurityUtil;
import th.co.geniustree.nhso.drugcatalog.controller.utils.FacesMessageUtils;
import th.co.geniustree.nhso.drugcatalog.model.ApproveFile;
import th.co.geniustree.nhso.drugcatalog.service.ApproveService;

/**
 *
 * @author moth
 */
@Component
@Scope("view")
public class UploadApprovedTmtFile implements Serializable {

    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(UploadApprovedTmtFile.class);

    private static final Map<String, String> HEADERS;

    static {
        Map<String, String> headers = new HashMap<>();
        headers.put("A", "HCODE");
        headers.put("B", "HOSPDRUGCODE");
        headers.put("C", "TMTID");
        headers.put("D", "PRODUCTCAT");
        headers.put("E", "SPECPREP");
        headers.put("F", "TRADENAME");
        headers.put("G", "GENRICNAME");
        headers.put("H", "STRENGTH");
        headers.put("I", "DOSAGEFORM");
        headers.put("J", "CONTENT");
        headers.put("K", "MANUFACTURER");
        headers.put("L", "ISED");
        headers.put("M", "UNITPRICE");
        headers.put("N", "RESULT");
        HEADERS = Collections.unmodifiableMap(headers);
    }

    private UploadedFile file;
    @Autowired
    @Qualifier("app")
    private Properties app;
    private File uploadtempFileDir;
    @Autowired
    private ApproveService approveService;

    @PostConstruct
    public void postConstruct() {
        ZipDriver zipDriver = new ZipDriver() {

            @Override
            public Charset getCharset() {
                return Charset.forName("TIS-620");
            }
        };
        TConfig.current().setArchiveDetector(new TArchiveDetector(TArchiveDetector.NULL, "zip", zipDriver));
        String uploadtempLocation = app.getProperty("uploadtempLocation");
        uploadtempFileDir = new File(uploadtempLocation, "APPROVED_FILES");
        if (!uploadtempFileDir.exists()) {
            uploadtempFileDir.mkdirs();
        }
    }

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public void handleFileUpload(FileUploadEvent event) {
        file = event.getFile();
    }

    public void save() {
        try {
            File rootDirectory = extractZip();
            processAll(rootDirectory);
            file = null;
            FacesMessageUtils.info("Process completed.");
        } catch (IOException ex) {
            LOG.error(null, ex);
            throw new RuntimeException(ex);
        }

    }

    public void reset() {

    }

    private File extractZip() throws IOException {
        try (InputStream inputFileStream = file.getInputstream()) {
            File tempFile = new File(uploadtempFileDir, file.getFileName());
            LOG.debug("save target file to = {}", tempFile.getAbsolutePath());

            Files.asByteSink(tempFile).writeFrom(inputFileStream);
            File targetExtractDir = new File(uploadtempFileDir, Files.getNameWithoutExtension(file.getFileName()));
            if (targetExtractDir.exists()) {
                FileUtils.deleteDirectory(targetExtractDir);
            }
            TFile.cp_r(new TFile(tempFile), targetExtractDir, TArchiveDetector.NULL);
            java.nio.file.Files.walkFileTree(targetExtractDir.toPath(), new SimpleFileVisitor<Path>() {

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    if (file.toFile().getName().endsWith("zip")) {
                        TFile.cp_r(new TFile(file.toFile()), new File(file.toFile().getParent(), Files.getNameWithoutExtension(file.toFile().getName())), TArchiveDetector.NULL);
                    }
                    return FileVisitResult.CONTINUE;
                }

            });
            return targetExtractDir;
        }
    }

    private void processAll(File rootDirectory) throws IOException {
        java.nio.file.Files.walkFileTree(rootDirectory.toPath(), new SimpleFileVisitor<Path>() {

            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                if (file.toFile().getName().endsWith("xls") || file.toFile().getName().endsWith("xlsx")) {
                    try {
                        processFile(file);
                    } catch (InvalidFormatException ex) {
                        throw new IOException(ex);
                    }
                }
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                LOG.error(null, exc);
                return super.visitFileFailed(file, exc); //To change body of generated methods, choose Tools | Templates.
            }
        });
    }

    private void processFile(Path file) throws IOException, InvalidFormatException {
        // The bad method body Must refactoring TODO
        ApproveFile approveFile = new ApproveFile();
        Workbook wb = null;
        NPOIFSFileSystem npoifs = null;
        OPCPackage pkg = null;
        int notNullRowCount = 0;
        try {
            approveFile.setFileName(file.toFile().getName());
            npoifs = new NPOIFSFileSystem(file.toFile());
            wb = WorkbookFactory.create(npoifs);
        } catch (OfficeXmlFileException ofe) {
            if (npoifs != null) {
                npoifs.close();
            }
            LOG.warn("Use new Office API (XLSX)");
            pkg = OPCPackage.open(file.toFile());
            wb = WorkbookFactory.create(pkg);
        } finally {
            try {
                if (wb == null) {
                    LOG.info("Can't load workbook for file {}.", file.toFile());
                    return;
                }
                Sheet sheet = wb.getSheetAt(0);
                Iterator<Row> rowIterator = sheet.rowIterator();
                rowIterator.next();//skip first  row
                List<ApproveData> datas = new ArrayList<>();
                for (; rowIterator.hasNext();) {
                    Row row = rowIterator.next();
                    notNullRowCount++;
                    String hcode = getCellValue(row.getCell(0, Row.RETURN_BLANK_AS_NULL));
                    
                    String producCat = getCellValue(row.getCell(3, Row.RETURN_BLANK_AS_NULL));
                    
                    String tradeName = getCellValue(row.getCell(5, Row.RETURN_BLANK_AS_NULL));
                    String genericName = getCellValue(row.getCell(6, Row.RETURN_BLANK_AS_NULL));
                    String strength = getCellValue(row.getCell(7, Row.RETURN_BLANK_AS_NULL));
                    String dosageForm = getCellValue(row.getCell(8, Row.RETURN_BLANK_AS_NULL));
                    String content = getCellValue(row.getCell(9, Row.RETURN_BLANK_AS_NULL));
                    String manufacturer = getCellValue(row.getCell(10, Row.RETURN_BLANK_AS_NULL));
                    
                    String hospDrug = getCellValue(row.getCell(1, Row.RETURN_BLANK_AS_NULL));
                    String tmt = getCellValue(row.getCell(2, Row.RETURN_BLANK_AS_NULL));
                    String result = getCellValue(row.getCell(13, Row.RETURN_BLANK_AS_NULL));
                    Integer uploadItemId = null;
                    try {
                        uploadItemId = Integer.parseInt(getCellValue(row.getCell(15, Row.RETURN_BLANK_AS_NULL)));
                    } catch (NumberFormatException numberFormatException) {
                        new InvalidFormatException("cannot parse 'ItemID'");
                    }
                    if (Strings.isNullOrEmpty(hcode) || Strings.isNullOrEmpty(hospDrug) || Strings.isNullOrEmpty(tmt) || Strings.isNullOrEmpty(result)) {
                        LOG.info("No process XLS for row hcode={} ,hospDrug={}, tmt={}, result={}", hcode, hospDrug, tmt, result);
                        continue;
                    }
                    boolean approve = "1".equals(result.trim());
                    Set<String> errorColumns = null;
                    if (!approve) {
                        errorColumns = extractColumns(result);
                        errorColumns.remove("SPECPREP");
                        errorColumns.remove("UNITPRICE");
                        errorColumns.remove("CONTENT");
                        if (errorColumns.isEmpty()) {
                            approve = true;
                        }
                    }
                    ApproveData approveData = new ApproveData(hcode, hospDrug, tmt, approve, errorColumns, SecurityUtil.getUserDetails().getPid(), uploadItemId);
                    approveData.setProductCat(producCat);
                    approveData.setTradeName(tradeName);
                    approveData.setGenericName(genericName);
                    approveData.setStrength(strength);
                    approveData.setDosageForm(dosageForm);
                    approveData.setContent(content);
                    approveData.setManufacturer(manufacturer);
                    datas.add(approveData);
                    LOG.debug("approve data => {}", approveData);
                }
                approveService.approveOrRejects(datas,approveFile);
            } finally {
                if (npoifs != null) {
                    npoifs.close();
                }
                if (pkg != null) {
                    pkg.close();
                }
            }
        }
        LOG.info("processed file: {} ,notNullRowCount : {}", new Object[]{file, notNullRowCount});
    }

    private String getCellValue(Cell cell) {
        if (cell != null) {
            cell.setCellType(Cell.CELL_TYPE_STRING);
            if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                return "" + (int) cell.getNumericCellValue();
            } else {
                return cell.getStringCellValue();
            }
        }
        return null;
    }

    private Set<String> extractColumns(String result) {
        List<String> splitToList = Splitter.on(CharMatcher.anyOf("=,")).trimResults().omitEmptyStrings().splitToList(result);
        Set<String> resultColumns = new HashSet<>();
        for (String column : splitToList) {
            resultColumns.add(HEADERS.get(column.substring(0, 1)));
        }
        return resultColumns;
    }
}
