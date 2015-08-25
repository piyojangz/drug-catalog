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

    private static final String ACTIVE_INGREDIENT_AND_STRENGTH_PATTERN = "((?<activeIngregient>\\w+[\\w\\s]*)\\s+(?<strength>[\\d]+[.\\d]*\\s*[a-zA-Z]+(/[\\d.]+\\s*\\w+)*))+";

    private final Set<String> activeIngredients;
    private final Set<String> strengths;
    private String dosageForm;

    public FSNSplitter() {
        activeIngredients = new HashSet<>();
        strengths = new HashSet<>();
    }

    public void getActiveIngredientAndStrengthFromFSN(TMTDrug drug) {
        
        String regex = "[(](?<manufacturer>[\\w\\s+-,./]+)[)]\\s*[(](?<activeIngregientAndStrength>[\\w\\s+-,./\\d]+)[)]\\s*(?<dosageForm>\\w+[\\w\\s\\-]*)\\s*,*";
        activeIngredients.clear();
        strengths.clear();
        if (drug.getType().equals(TMTDrug.Type.TPU) || drug.getType().equals(TMTDrug.Type.TP)) {
            String fsn = drug.getFsn();
            Pattern p = Pattern.compile(regex, Pattern.UNICODE_CHARACTER_CLASS);
            Matcher m = p.matcher(fsn);
            if (m.find()) {
                dosageForm = m.group("dosageForm");
            }
            if(m.reset().find()){
                fsn = m.group("activeIngregientAndStrength");
            }
            p = Pattern.compile(ACTIVE_INGREDIENT_AND_STRENGTH_PATTERN, Pattern.UNICODE_CHARACTER_CLASS);
            m = p.matcher(fsn);
            while (m.find()) {
                String activeIngredient = m.group("activeIngregient");
                String strength = m.group("strength");
                activeIngredients.add(activeIngredient);
                strengths.add(strength);
            }
            
        }
    }

    public Set<String> getActiveIngredients() {
        return activeIngredients;
    }

    public Set<String> getStrengths() {
        return strengths;
    }

    public String getDosageForm() {
        return dosageForm;
    }

}
