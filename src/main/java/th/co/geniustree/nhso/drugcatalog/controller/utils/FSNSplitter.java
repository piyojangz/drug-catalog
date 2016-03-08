/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.controller.utils;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import th.co.geniustree.nhso.drugcatalog.model.TMTDrug;

/**
 *
 * @author Thanthathon
 */
public class FSNSplitter {

    private static final Logger LOG = LoggerFactory.getLogger(FSNSplitter.class);

    private final String PATTERN_FSN_TP = PATTERN_TRADENAME
            .concat("\\s\\(").concat(PATTERN_MANUFACTURER).concat("\\)")
            .concat("\\s\\(").concat(PATTERN_ACTIVEINGREDIENT_MATCH_STRENGTH).concat("\\)")
            .concat("\\s").concat(PATTERN_DOSAGEFORM).concat("(,\\s\\w+)?");

    private final String PATTERN_FSN_TPU = PATTERN_FSN_TP.concat(",\\s").concat(PATTERN_CONTENT);

    private final String PATTERN_ACTIVEINGREDIENT_STRENGTH = PATTERN_ACTIVEINGREDIENT.concat("\\s").concat(PATTERN_STRENGTH);
    
    private static final String PATTERN_ACTIVEINGREDIENT_MATCH_STRENGTH = "(?<activeIngregientAndStrength>.+)";
    private static final String PATTERN_ACTIVEINGREDIENT = "(?<activeIngregient>\\w+[\\w\\s\\d\\-(),/]*)";
    private static final String PATTERN_STRENGTH = "(?<strength>\\d+(\\.?\\d+)?\\s[a-zA-Z]+(/\\d+(\\.?\\d+)?\\s[a-zA-Z]+)?)";
    private static final String PATTERN_DOSAGEFORM = "(?<dosageForm>[\\w\\s\\d\\-/]+)";
    private static final String PATTERN_TRADENAME = "(?<tradeName>.+)";
    private static final String PATTERN_MANUFACTURER = "(?<manufacturer>.+)";
    private static final String PATTERN_CONTENT = "(?<content>\\d+(\\.?\\d+)?\\s[\\w\\s\\d\\-/,]+)$";
    
    private final Map<String, String> activeIngredients;
    private String dosageForm;
    private String content;

    public FSNSplitter() {
        activeIngredients = new HashMap<>();
    }

    public Map<String, String> getActiveIngredientAndStrengthFromFSN(TMTDrug drug) {
        activeIngredients.clear();
        String regex;
        if (drug.getType().equals(TMTDrug.Type.TPU)) {
            regex = PATTERN_FSN_TPU;
        } else if (drug.getType().equals(TMTDrug.Type.TP)) {
            regex = PATTERN_FSN_TP;
        } else {
            throw new IllegalArgumentException("TMTDrug type must only in {'TP','TPU'}");
        }
        String fsn = drug.getFsn().trim();
        Pattern p = Pattern.compile(regex, Pattern.UNICODE_CHARACTER_CLASS);
        Matcher m = p.matcher(fsn);
        if (m.find()) {
            dosageForm = m.group("dosageForm");
            if (drug.getType().equals(TMTDrug.Type.TPU)) {
                content = m.group("content");
            } else {
                content = null;
            }
        }
        if (m.reset().find()) {
            fsn = m.group("activeIngregientAndStrength");
        }
        p = Pattern.compile(PATTERN_ACTIVEINGREDIENT_STRENGTH, Pattern.UNICODE_CHARACTER_CLASS);
        m = p.matcher(fsn);
        while (m.find()) {
            activeIngredients.put(m.group("activeIngregient"), m.group("strength"));
        }
        return activeIngredients;
    }

    public Set<String> getOnlyActiveIngredients() {
        return activeIngredients.keySet();
    }

    public Collection<String> getOnlyStrengths() {
        return activeIngredients.values();
    }

    public Map<String, String> getActiveIngredients() {
        return activeIngredients;
    }

    public Set<String> getActiveIngredientAndStrength() {
        Set<String> set = new HashSet<>();
        for (Entry entry : activeIngredients.entrySet()) {
            set.add(entry.getKey().toString() + " " + entry.getValue().toString());
        }
        return set;
    }

    public String getDosageForm() {
        return dosageForm;
    }

    public String getContent() {
        return content;
    }

}
