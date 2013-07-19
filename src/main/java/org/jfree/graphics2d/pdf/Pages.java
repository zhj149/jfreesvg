/**
 * (C)opyright 2013, by Object Refinery Limited.  All rights reserved.
 */
package org.jfree.graphics2d.pdf;

import java.awt.Font;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Manages the pages.
 */
public class Pages extends PDFObject {
    
    private PDFDocument parent;
    
    private List<Page> pages;

    /** The list of font objects used in the document. */
    private List<PDFFont> fonts;
    
    private Map<FontKey, PDFFont> fontMap;
    
    private int nextFont = 1;
    
    Pages(int number, int generation, PDFDocument parent) {
        super(number, generation);
        this.parent = parent;
        this.pages = new ArrayList<Page>();
        this.fonts = new ArrayList<PDFFont>();
        this.fontMap = new HashMap<FontKey, PDFFont>();
    }
    
    public PDFDocument getDocument() {
        return this.parent;
    }
    
    public List<Page> getPages() {
        return this.pages;
    }
    
    public List<PDFFont> getFonts() {
        return this.fonts;
    }
    
    public PDFFont getFont(String name) {
        for (PDFFont f : this.fonts) {
            if (f.getName().equals(name)) {
                return f;
            }
        }
        return null;
    }
    
    void add(Page page) {
        this.pages.add(page);
    }
    
    
    public String findOrCreateFontReference(Font f) {
        // for now, map all fonts to one of the standard PDF fonts
        FontKey fontKey = FontKey.createFontKey(f);
        PDFFont pdfFont = this.fontMap.get(fontKey);
        if (pdfFont == null) {
            int number = this.parent.getNextNumber();
            String name = "/F" + this.nextFont + "D" + f.getFamily();
            this.nextFont++;
            pdfFont = new PDFFont(number, 0, name, "/Times-Roman", "/MacRomanEncoding");
            this.fonts.add(pdfFont);
            this.fontMap.put(fontKey, pdfFont);
        }
        return pdfFont.getName();
    }

    @Override
    public String getObjectString() {
        Dictionary dictionary = new Dictionary("/Pages");
        Page[] pagesArray = new Page[this.pages.size()];
        for (int i = 0; i < this.pages.size(); i++) {
            pagesArray[i] = this.pages.get(i);
        }
        dictionary.put("/Kids", pagesArray);
        dictionary.put("/Count", Integer.valueOf(pages.size()));
        return dictionary.toPDFString();
    }

}