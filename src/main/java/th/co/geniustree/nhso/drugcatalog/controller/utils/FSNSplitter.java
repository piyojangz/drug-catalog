/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.controller.utils;

import java.util.HashSet;
import java.util.List;
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

    private final Set<String> activeIngredients;
    private final Set<String> strengths;
    
    public FSNSplitter(){
        activeIngredients = new HashSet<>();
        strengths = new HashSet<>();
    }
    
    /**
     * get ActiveIngredient and Strength only from GP, TP, and TPU TMTDrugType
     * @param drug 
     */
    public void getActiveIngredientAndStrengthFromFSN(TMTDrug drug) {
        if (drug.getType().equals(TMTDrug.Type.GP) || drug.getType().equals(TMTDrug.Type.TP) || drug.getType().equals(TMTDrug.Type.TPU)) {
            String fsn = drug.getFsn();
            Pattern p = Pattern.compile(ACTIVE_INGREDIENT_AND_STRENGTH_PATTERN, Pattern.UNICODE_CHARACTER_CLASS);
            Matcher m = p.matcher(fsn);
            while (m.find()) {
                String activeIngredient = m.group(1);
                String strength = m.group(2);
                System.out.println("active + strength : " + m.group());
                System.out.println("active ingredient : " + activeIngredient);
                System.out.println("         strength : " + strength);
                activeIngredients.add(activeIngredient);
                strengths.add(strength);
            }
        } else {
            LOG.warn("Not supported {} type!",drug.getType());
        }
    }
    
    public void getActiveIngredientAndStrengthFromFSN(List<TMTDrug> drugs){
        for(TMTDrug drug : drugs){
            getActiveIngredientAndStrengthFromFSN(drug);
        }
    }

    public Set<String> getActiveIngredients() {
        return activeIngredients;
    }

    public Set<String> getStrengths() {
        return strengths;
    }
    
    

}
