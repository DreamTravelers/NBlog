package com.zxqax.nblog.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 发送邮箱配置类
 * 默认发送人为  liu.cysl@qq.com
 */
@Slf4j
public class EmailUtils {

    /**
     * 检测邮箱是否合法
     *
     * @param email 邮箱
     * @return 合法状态
     */
    public static boolean checkEmail(String email) {
        String rule = "^\\w+((-\\w+)|(\\.\\w+))*\\@[A-Za-z0-9]+((\\.|-)[A-Za-z0-9]+)*\\.[A-Za-z0-9]+$";
        //正则表达式的模式 编译正则表达式
        Pattern p = Pattern.compile(rule);
        //正则表达式的匹配器
        Matcher m = p.matcher(email);
        //进行正则匹配
        return m.matches();
    }


    /**
     * 发送邮箱
     * @param toAdress 收件人邮箱地址
     * @param code 验证码
     */
    public static  boolean sendEmail(String toAdress, String code){
        //设置发件人属性
        Properties properties = new Properties();
        //发送邮箱服务器
        properties.setProperty("mail.host","smtp.qq.com");
        //发送端口
        properties.setProperty("mail.smtp.port","465");
        //是否开启权限控制
        properties.setProperty("mail.smtp.auth","true");
        //发送的协议是简单的邮件传输协议
        properties.setProperty("mail.transport.protocol","smtp");
        properties.setProperty("mail.smtp.ssl.enable","true");

        //建立两点之间的链接
        Session session = Session.getDefaultInstance(properties, new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                //发件人邮件用户名、授权码
                return new PasswordAuthentication("liu.cysl@qq.com", "xkxjszndkhqxhffi");
            }
        });

        //创建邮件对象
        Message message = new MimeMessage(session);

        //设置发件人
        try {
            message.setFrom(new InternetAddress("liu.cysl@qq.com"));
            //设置收件人
            message.setRecipient(Message.RecipientType.TO,new InternetAddress(toAdress));
            //设置主题
            message.setSubject("【NBlog】验证码");

            //设置邮件正文  第二个参数是邮件发送的类型
            message.setContent("<!DOCTYPE>\n" + "<div bgcolor='#cd0000' style='border:1px solid #d9f4ee; font-size:25px; line-height:22px; padding-left:1px;padding-top:5px;   padding-bottom:5px;'>\n" + "   <div style='width:950px;font-family:arial; '>欢迎使用nblog，您的验证码为：<br/><br/><br/><h2 style='color:green'>" + code + "</h2><br/><p style=\"font-size:15px;\">如不是您本人操作，请忽略。版权所有：nblog</p></div>\n" + "       </div>","text/html;charset=UTF-8");

            //发送一封邮件
            Transport transport = session.getTransport();
            transport.connect("liu.cysl@qq.com","xkxjszndkhqxhffi");
            Transport.send(message);
            return true;
        } catch (MessagingException e) {
            log.warn("邮箱发送失败；Info : [ to ：" + toAdress +" ] ");
            log.error(String.valueOf(e));
            return false;
        }
    }
}
