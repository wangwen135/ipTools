package com.wwh.iptools;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message.RecipientType;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.wwh.iptools.ip.IpEntity;

/**
 * <pre>
 * 发送邮件，依赖配置文件
 * 每次发邮件重新加载配置文件
 * </pre>
 *
 * @author wwh
 * @date 2015年9月24日 下午1:48:47
 *
 */
public class MailSender {

    public static InternetAddress[] getAddress(String s) throws AddressException {
        if (s == null || "".equals(s.trim())) {
            return null;
        }
        String[] as = s.split(";");
        InternetAddress[] address = new InternetAddress[as.length];
        for (int i = 0; i < as.length; i++) {
            address[i] = new InternetAddress(as[i]);
        }
        return address;
    }

    public static void send(IpEntity entity) throws Exception {
        // 配置发送邮件的环境属性
        final Properties props = new Properties();
        props.load(MailSender.class.getResourceAsStream("/mail.properties"));

        // 构建授权信息，用于进行SMTP进行身份验证
        Authenticator authenticator = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                // 用户名、密码
                String userName = props.getProperty("mail.user");
                String password = props.getProperty("mail.password");
                return new PasswordAuthentication(userName, password);
            }
        };
        // 使用环境属性和授权信息，创建邮件会话
        Session mailSession = Session.getInstance(props, authenticator);
        // 创建邮件消息
        MimeMessage message = new MimeMessage(mailSession);
        // 设置发件人
        InternetAddress form = new InternetAddress(props.getProperty("mail.user"));
        message.setFrom(form);

        // 设置收件人
        // 收件人
        message.setRecipients(RecipientType.TO, getAddress(props.getProperty("mail.send.to")));

        // 抄送
        InternetAddress[] ccs = getAddress(props.getProperty("mail.send.cc"));
        if (ccs != null) {
            message.setRecipients(RecipientType.CC, ccs);
        }

        // 密送
        InternetAddress[] bccs = getAddress(props.getProperty("mail.send.bcc"));
        if (bccs != null) {
            message.setRecipients(RecipientType.BCC, bccs);
        }

        // 设置邮件标题
        message.setSubject(props.getProperty("mail.title"));

        // 设置邮件的内容体
        String mailContent = props.getProperty("mail.content");
        message.setContent(String.format(mailContent, entity.getIpAddress(), entity.getLocation()), "text/html;charset=UTF-8");

        // 发送邮件
        Transport.send(message);
    }
}
