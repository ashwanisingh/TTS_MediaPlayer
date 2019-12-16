package com.mbtechpro.tts;

/**
 * Created by ashwanisingh on 17/10/17.
 */

public class LanguageItem {

    public String displayName;
    public String country;
    public String language;
    public boolean isSelected;
    public boolean isExist;


    @Override
    public boolean equals(Object obj) {
        super.equals(obj);

        if(obj instanceof LanguageItem) {
            LanguageItem item = (LanguageItem) obj;
            return item.country.equals(country) && item.language.equals(language);
        }

        return false;

    }

    @Override
    public int hashCode() {
        super.hashCode();
        return country.hashCode()+language.hashCode();
    }
}
