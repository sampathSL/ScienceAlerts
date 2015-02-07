/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.appzone.sciencealerts.datafeeds;

import java.util.ArrayList;
import java.util.List;

public class Feed
{

    final String title;
    final String description;
    final String language;
    final String copyright;
    final String lastBuildDate;
    final String guid;
    final List entries = new ArrayList();

    public Feed(String title, String description, String language, String copyright, String lastBuildDate, String guid)
    {
        this.title = title;
        this.description = description;
        this.language = language;
        this.copyright = copyright;
        this.lastBuildDate = lastBuildDate;
        this.guid = guid;
    }

    public String getTitle()
    {
        return title;
    }

    public String getDescription()
    {
        return description;
    }

    public String getLanguage()
    {
        return language;
    }

    public String getCopyright()
    {
        return copyright;
    }

    public String getLastBuildDate()
    {
        return lastBuildDate;
    }

    public List getEntries()
    {
        return entries;
    }
}
