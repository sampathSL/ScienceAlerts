package com.appzone.sciencealerts.service;

import com.appzone.sciencealerts.hibernate.dao.impl.SmsSenderDaoImpl;
import com.appzone.sciencealerts.hibernate.entity.SmsSender;
import java.util.Date;
import org.apache.log4j.Logger;

// Referenced classes of package com.appzone.sciencealerts.service:
//            FinalSms
public class UnRegUser {

    SmsSenderDaoImpl smsSenderDAOImpl;
    FinalSms transaction;
    private static final Logger logger = Logger.getLogger(UnRegUser.class);

    public UnRegUser() {
        smsSenderDAOImpl = new SmsSenderDaoImpl();
        transaction = new FinalSms();
    }

    public void ScienceAlertUnReg(String address) {
        Date date = new Date();
        try {
            SmsSender smsSenderUnreg = smsSenderDAOImpl.findBySmsSenderAddress(address);
            if (smsSenderUnreg != null) {
                smsSenderUnreg.setIsReg("N");
                smsSenderUnreg.setIsActive("N");
                smsSenderUnreg.setIsSchedularActive("N");
                smsSenderUnreg.setLastActiveTime(date);
                smsSenderDAOImpl.delete(smsSenderUnreg);
            } else {
                SmsSender sender = new SmsSender();
                sender.setAddress(address.toString());
                sender.setUserName("UNKNOWN");
                sender.setIsReg("N");
                sender.setIsActive("N");
                sender.setMarks(Long.valueOf(Long.parseLong("2")));
                sender.setIsSchedularActive("N");
                sender.setJoinedDate(date);
                sender.setLastActiveTime(date);
                smsSenderDAOImpl.save(sender);
            }
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        }
    }
}
