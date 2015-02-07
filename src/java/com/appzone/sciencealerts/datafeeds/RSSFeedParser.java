/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.appzone.sciencealerts.datafeeds;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;

public class RSSFeedParser {

    static final String TITLE = "title";
    static final String DESCRIPTION = "description";
    static final String CHANNEL = "channel";
    static final String LANGUAGE = "language";
    static final String COPYRIGHT = "copyright";
    static final String LINK = "link";
    static final String ITEM = "item";
    static final String PUB_DATE = "pubDate";
    static final String LAST_BUILD_DATE = "lastBuildDate";
    static final String GUID = "guid";
    private static URL url = null;

    public RSSFeedParser(String feedUrl) {
        try {
            url = new URL(feedUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public Feed readFeed() {
        Feed feed = null;
        try {
            boolean isFeedHeader = true;
            String description = "";
            String title = "";
            String language = "";
            String copyright = "";
            String pubdate = "";
            String lastBuildDate = "";
            String guid = "";
            XMLInputFactory inputFactory = XMLInputFactory.newInstance();
            InputStream in = read();
            XMLEventReader eventReader = inputFactory.createXMLEventReader(in);
            do {
                if (!eventReader.hasNext()) {
                    break;
                }
                XMLEvent event = eventReader.nextEvent();
                if (event.isStartElement()) {
                    if (event.asStartElement().getName().getLocalPart() != null ? event.asStartElement().getName().getLocalPart().equals("item") : "item" == null) {
                        if (isFeedHeader) {
                            isFeedHeader = false;
                            feed = new Feed(title, description, language, copyright, lastBuildDate, guid);
                        }
                        //event = eventReader.nextEvent();
                    } else if (event.asStartElement().getName().getLocalPart() != null ? event.asStartElement().getName().getLocalPart().equals("title") : "title" == null) {
                        event = eventReader.nextEvent();
                        title = event.asCharacters().getData();
                    } else if (event.asStartElement().getName().getLocalPart() != null ? event.asStartElement().getName().getLocalPart().equals("description") : "description" == null) {
                        event = eventReader.nextEvent();
                        description = event.asCharacters().getData();
                    } else if (event.asStartElement().getName().getLocalPart() != null ? event.asStartElement().getName().getLocalPart().equals("guid") : "guid" == null) {
                        event = eventReader.nextEvent();
                        guid = event.asCharacters().getData();
                    } else if (event.asStartElement().getName().getLocalPart() != null ? event.asStartElement().getName().getLocalPart().equals("language") : "language" == null) {
                        event = eventReader.nextEvent();
                        language = event.asCharacters().getData();
                    } else if (event.asStartElement().getName().getLocalPart() != null ? event.asStartElement().getName().getLocalPart().equals("pubDate") : "pubDate" == null) {
                        event = eventReader.nextEvent();
                        pubdate = event.asCharacters().getData();
                    } else if (event.asStartElement().getName().getLocalPart() != null ? event.asStartElement().getName().getLocalPart().equals("copyright") : "copyright" == null) {
                        event = eventReader.nextEvent();
                        copyright = event.asCharacters().getData();
                    } else if (event.asStartElement().getName().getLocalPart() != null ? event.asStartElement().getName().getLocalPart().equals("lastBuildDate") : "lastBuildDate" == null) {
                        event = eventReader.nextEvent();
                        lastBuildDate = event.asCharacters().getData();
                    }
                } else if (event.isEndElement() && (event.asEndElement().getName().getLocalPart() != null ? event.asEndElement().getName().getLocalPart().equals("item") : "item" == null)) {
                    FeedMessage message = new FeedMessage();
                    message.setDescription(description);
                    message.setGuid(guid);
                    message.setPubDate(pubdate);
                    message.setTitle(title);
                    message.setLastBuild(lastBuildDate);
                    feed.getEntries().add(message);
                    //event = eventReader.nextEvent();
                }
            } while (true);
        } catch (XMLStreamException e) {
            throw new RuntimeException(e);
        }
        return feed;
    }

    private InputStream read() {
        try {
            return url.openStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
