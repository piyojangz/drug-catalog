/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.controller;

import java.io.Serializable;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 *
 * @author moth
 */
@Component
@Scope("session")
public class ThemeSwitching implements Serializable {

    private String[] avialableThemes = {"green", "purple"};
    private String selectedTheme = "green";

    public String[] getAvialableThemes() {
        return avialableThemes;
    }

    public void setAvialableThemes(String[] avialableThemes) {
        this.avialableThemes = avialableThemes;
    }

    public String getSelectedTheme() {
        return selectedTheme;
    }

    public void setSelectedTheme(String selectedTheme) {
        this.selectedTheme = selectedTheme;
    }

    public String switchTheme() {
        return null;
    }
}
