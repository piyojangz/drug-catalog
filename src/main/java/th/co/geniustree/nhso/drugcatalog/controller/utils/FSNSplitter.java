/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.controller.utils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
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

    private static final String ACTIVE_INGREDIENT_AND_STRENGTH_PATTERN = "(\\w+[\\w\\s]*\\s)([\\d.]+\\s*[a-zA-Z]+/?[\\d.]*\\s*[a-zA-Z]*)";

    private final Map<String, String> activeIngreDientAndStrength;

    public FSNSplitter() {
        activeIngreDientAndStrength = new HashMap<>();
    }

    /**
     * get ActiveIngredient and Strength only from GP, TP, and TPU TMTDrugType
     *
     * @param drug
     */
    public void getActiveIngredientAndStrengthFromFSN(TMTDrug drug) {
        if (drug.getType().equals(TMTDrug.Type.GP)
                || drug.getType().equals(TMTDrug.Type.GPU)
                || drug.getType().equals(TMTDrug.Type.TP)
                || drug.getType().equals(TMTDrug.Type.TPU)) {
            activeIngreDientAndStrength.clear();
            String fsn = drug.getFsn();
            LOG.debug("              fsn : {}", fsn);

            Pattern p = Pattern.compile(ACTIVE_INGREDIENT_AND_STRENGTH_PATTERN, Pattern.UNICODE_CHARACTER_CLASS);
            Matcher m = p.matcher(fsn);
            while (m.find()) {
                String activeIngredient = m.group(1);
                String strength = m.group(2);
                LOG.debug("active + strength : {}", m.group());
                LOG.debug("active ingredient : {}", activeIngredient);
                LOG.debug("         strength : {}\n", strength);
                activeIngreDientAndStrength.put(activeIngredient, strength);
            }
        } else {
            LOG.warn("Not supported {} type!", drug.getType());
        }
    }

    public Set<String> getActiveIngredients() {
        return activeIngreDientAndStrength.keySet();
    }

    public Set<String> getStrengths() {
        Set<String> strengths = new HashSet<>();
        strengths.addAll(activeIngreDientAndStrength.values());
        return strengths;
    }

    public Map<String, String> getActiveIngreDientAndStrength() {
        return activeIngreDientAndStrength;
    }

}
