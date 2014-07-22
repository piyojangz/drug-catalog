/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 *
 * @author moth
 */
public class DigestUtils {

    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(DigestUtils.class);

    public static String shaHex(File file) {
        try (InputStream in = new FileInputStream(file)) {
            return org.apache.commons.codec.digest.DigestUtils.shaHex(in);
        } catch (Exception e) {
            LOG.warn(null, e);
        }
        return null;
    }
}
