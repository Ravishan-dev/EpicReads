package com.ravishandev.epicreads.mail;

import com.ravishandev.epicreads.util.Env;
import io.rocketbase.mail.model.HtmlTextEmail;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;

public class VerificationMail extends Mailable {
    private final String to;
    private final String verificationCode;

    public VerificationMail(String to, String verificationCode) {
        this.to = to;
        this.verificationCode = verificationCode;
    }

    @Override
    public void build(Message message) throws MessagingException {
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
        message.setSubject("Email Verification Code" + Env.get("app.name"));

        String appURL = Env.get("app.url");
        String verifyURL = appURL + "/verify-account.html?email=" + to + "&verificationCode=" + verificationCode;

        HtmlTextEmail htmlTextEmail = getEmailTemplateBuilder()
                .header()
                .logo("https://upload.wikimedia.org/wikipedia/commons/e/eb/SmartTradePI.png").logoHeight(40).and()
                .text("WELCOME " + to).h1().center().and()
                .text("Thanks for Register in our Website").center().and()
                .text("To Verify Your Email Please Click On the Button Below").center().and()
                .text("Your Verification Code :" + verificationCode).center().and()
                .text("If You Have any trouble Please Past In this Link In Your Browser").center().and()
                .html("<a href=\"" + verifyURL + "\">" + verifyURL + "</a>").and()
                .copyright(Env.get("app.name")).url(appURL).suffix(". All Right Reserved").and()
                .build();

        message.setContent(htmlTextEmail.getHtml(), "text/html; charset=Utf-8");
    }
}
