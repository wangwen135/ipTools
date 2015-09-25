package com.wwh.iptools;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wwh.iptools.ip.IpAddressGetter;
import com.wwh.iptools.ip.IpEntity;

/**
 * <pre>
 * 发送IP地址到邮箱
 * </pre>
 *
 * @author wwh
 * @date 2015年9月25日 下午2:05:52
 *
 */
public class SendIpAddressToMail {

    private static final Logger log = LoggerFactory.getLogger(SendIpAddressToMail.class);

    /**
     * ip获取器
     */
    private IpAddressGetter ipGet = new IpAddressGetter();

    /**
     * 上一次的IP
     */
    private String lastIp;

    /**
     * 上次邮件发送时间
     */
    private Long lastMailTime;

    /**
     * 检查ip地址 时间间隔
     */
    private Long checkInterval = TimeUnit.MINUTES.toMillis(5);

    /**
     * 不能连上 重试时间间隔
     */
    private Long retryInterval = TimeUnit.MINUTES.toMillis(1);

    /**
     * 最大重试次数
     */
    private int maxRetry = 1000;

    /**
     * 邮件发送间隔时间
     */
    private Long mailSendInterval = TimeUnit.DAYS.toMillis(1);

    public void run() {
        // 重复运行

        IpEntity entity;

        while (true) {
            log.debug("运行...");
            entity = getIpEntity();
            if (lastIp == null || !lastIp.equals(entity.getIpAddress())) {
                log.info("IP变了发送邮件~~");
                sendMail(entity);
                lastIp = entity.getIpAddress();
                lastMailTime = System.currentTimeMillis();
            }
            // 时间到了 准备发邮件
            if (System.currentTimeMillis() - lastMailTime >= mailSendInterval) {
                log.info("间隔时间到了发送邮件~~");
                sendMail(entity);
                lastMailTime = System.currentTimeMillis();
            }

            try {
                Thread.sleep(checkInterval);
            } catch (InterruptedException e) {
            }
        }
    }

    public void sendMail(IpEntity entity) {
        int retryTime = 0;
        while (true) {
            try {
                MailSender.send(entity);
                return;
            } catch (Exception e) {
                log.error("发送邮件异常", e);
            }
            retryTime++;
            if (retryTime >= maxRetry) {
                log.error("！！！！警告 ！！！！\n连续重试了{}次，依然无法发送邮件，系统要退出了", maxRetry);
                System.exit(1);
            }
            try {
                Thread.sleep(retryInterval);
            } catch (InterruptedException e) {
            }
        }
    }

    /**
     * 获取IP，如果失败了会重试
     * 
     * @return
     */
    public IpEntity getIpEntity() {
        int retryTime = 0;
        while (true) {

            try {
                IpEntity entity = ipGet.getIpAddress();
                if (entity != null) {
                    return entity;
                }
            } catch (Exception e) {
                log.error("获取IP异常", e);
                ipGet = new IpAddressGetter();
            }

            retryTime++;
            if (retryTime >= maxRetry) {
                log.error("！！！！警告 ！！！！\n连续重试了{}次，依然无法获取IP信息，系统要退出了", maxRetry);
                System.exit(1);
            }
            try {
                Thread.sleep(retryInterval);
            } catch (InterruptedException e) {
            }
        }
    }

    /**
     * 获取 ipGet
     *
     * @return the ipGet
     */
    public IpAddressGetter getIpGet() {
        return ipGet;
    }

    /**
     * 设置 ipGet
     *
     * @param ipGet
     *            the ipGet to set
     */
    public void setIpGet(IpAddressGetter ipGet) {
        this.ipGet = ipGet;
    }

    /**
     * 获取 lastIp
     *
     * @return the lastIp
     */
    public String getLastIp() {
        return lastIp;
    }

    /**
     * 设置 lastIp
     *
     * @param lastIp
     *            the lastIp to set
     */
    public void setLastIp(String lastIp) {
        this.lastIp = lastIp;
    }

    /**
     * 获取 lastMailTime
     *
     * @return the lastMailTime
     */
    public Long getLastMailTime() {
        return lastMailTime;
    }

    /**
     * 设置 lastMailTime
     *
     * @param lastMailTime
     *            the lastMailTime to set
     */
    public void setLastMailTime(Long lastMailTime) {
        this.lastMailTime = lastMailTime;
    }

    /**
     * 获取 checkInterval
     *
     * @return the checkInterval
     */
    public Long getCheckInterval() {
        return checkInterval;
    }

    /**
     * 设置 checkInterval
     *
     * @param checkInterval
     *            the checkInterval to set
     */
    public void setCheckInterval(Long checkInterval) {
        this.checkInterval = checkInterval;
    }

    /**
     * 获取 retryInterval
     *
     * @return the retryInterval
     */
    public Long getRetryInterval() {
        return retryInterval;
    }

    /**
     * 设置 retryInterval
     *
     * @param retryInterval
     *            the retryInterval to set
     */
    public void setRetryInterval(Long retryInterval) {
        this.retryInterval = retryInterval;
    }

    /**
     * 获取 maxRetry
     *
     * @return the maxRetry
     */
    public int getMaxRetry() {
        return maxRetry;
    }

    /**
     * 设置 maxRetry
     *
     * @param maxRetry
     *            the maxRetry to set
     */
    public void setMaxRetry(int maxRetry) {
        this.maxRetry = maxRetry;
    }

    /**
     * 获取 mailSendInterval
     *
     * @return the mailSendInterval
     */
    public Long getMailSendInterval() {
        return mailSendInterval;
    }

    /**
     * 设置 mailSendInterval
     *
     * @param mailSendInterval
     *            the mailSendInterval to set
     */
    public void setMailSendInterval(Long mailSendInterval) {
        this.mailSendInterval = mailSendInterval;
    }

    public static void main(String[] args) {
        log.info("启动~~");
        SendIpAddressToMail sipmail = new SendIpAddressToMail();
        sipmail.run();
    }

}
