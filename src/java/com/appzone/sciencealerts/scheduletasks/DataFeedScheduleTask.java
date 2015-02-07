package com.appzone.sciencealerts.scheduletasks;

import com.appzone.sciencealerts.datafeeds.*;
import com.appzone.sciencealerts.hibernate.dao.impl.ScienceAlertsDaoImpl;
import com.appzone.sciencealerts.hibernate.entity.ScienceAlerts;
import com.appzone.sciencealerts.properties.PropertyFileReader;
import java.text.SimpleDateFormat;
import java.util.*;
import org.apache.log4j.Logger;

public class DataFeedScheduleTask {

    private static final Logger logger = Logger.getLogger(DataFeedScheduleTask.class);
    SimpleDateFormat dateFormat;

    public DataFeedScheduleTask() {
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    }

    public void FindAlerts(String url) {
        Calendar oldDay = Calendar.getInstance();
        oldDay.add(5, Integer.parseInt(PropertyFileReader.getValue("FEEDS_SET_DATE")));
        String checkDate = dateFormat.format(oldDay.getTime());
        logger.info("===========================");
        logger.info((new StringBuilder()).append("Starting - ").append(checkDate).append("-").append(url).toString());
        logger.info("===========================");
        try {
            RSSFeedParser parser = new RSSFeedParser(url);
            Feed feed = parser.readFeed();
            Iterator nextItem = feed.getEntries().iterator();
            do {
                if (!nextItem.hasNext()) {
                    break;
                }
                FeedMessage message = (FeedMessage) nextItem.next();
                if (!message.getTitle().toLowerCase().contains("video") && !message.getTitle().toLowerCase().contains("picture") && !message.getTitle().toLowerCase().contains("gotta-see video") && !message.getTitle().toLowerCase().contains("in picture") && !message.getTitle().toLowerCase().contains("sex") && !message.getTitle().toLowerCase().contains("punk") && !message.getTitle().toLowerCase().contains("zombie") && !message.getTitle().toLowerCase().contains("zombies") && !message.getTitle().toLowerCase().contains("killer") && !message.getTitle().toLowerCase().contains("podcast:") && !message.getTitle().toLowerCase().contains("ass") && message.getPubDate() != null && oldDay.getTime().before(new Date(message.getPubDate()))) {
                    ScienceAlerts scienceAlerts = new ScienceAlerts();
                    ScienceAlertsDaoImpl scienceAlertsDaoImpl = new ScienceAlertsDaoImpl();
                    if (!scienceAlertsDaoImpl.CheckUniqueTitle(message.getTitle().replaceAll("'", "").replaceAll("\"", "").replaceAll("&", "AND").trim().toUpperCase())) {
                        scienceAlerts.setTitle(message.getTitle().replaceAll("'", "").replaceAll("\"", "").replaceAll("&", "AND").trim().toUpperCase());
                        scienceAlerts.setPublishDate(new Date(message.getPubDate().toString()));
                        scienceAlerts.setImportDate(new Date((new Date()).getTime()));
                        String totalSmsStr = (new StringBuilder()).append(message.getTitle().replaceAll("'", "").replaceAll("\"", "").replaceAll("&", "AND").trim().toUpperCase()).append("-").append(message.getDescription().replaceAll("'", "").replaceAll("\"", "").replaceAll("&", "and").trim()).toString();
                        totalSmsStr = totalSmsStr.trim();
                        int totalSmsLength = totalSmsStr.trim().length();
                        if (totalSmsLength > Integer.parseInt(PropertyFileReader.getValue("MIN_LENGTH")) && totalSmsLength < Integer.parseInt(PropertyFileReader.getValue("MAX_LENGTH"))) {
                            scienceAlerts.setScheduled("N");
                            scienceAlerts.setSms(totalSmsStr);
                            scienceAlertsDaoImpl.save(scienceAlerts);
                        }
                    }
                }
            } while (true);
            logger.info("============End of data feeding===============");
        } catch (Exception ex) {
            logger.error(ex);
            ex.printStackTrace();
        }
    }
}
