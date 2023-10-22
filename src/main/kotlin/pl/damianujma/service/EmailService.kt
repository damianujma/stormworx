package pl.damianujma.service

import java.util.*
import javax.mail.Message
import javax.mail.MessagingException
import javax.mail.PasswordAuthentication
import javax.mail.Session
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

fun main(text: String, emailTo: String, city: String) {
    val userName = System.getenv("STORMWORX_EMAIL");
    val password = System.getenv("STORMWORX_PASSWORD");

    val subject = "Stormworx - $city weather forecast"

    val props = Properties()
    putIfMissing(props, "mail.smtp.host", System.getenv("STORMWORX_SMTP"))
    putIfMissing(props, "mail.smtp.port", System.getenv("STORMWORX_SMTP_PORT"))
    putIfMissing(props, "mail.smtp.auth", "true")
    putIfMissing(props, "mail.smtp.starttls.enable", "true")

    val session = Session.getDefaultInstance(props, object : javax.mail.Authenticator() {
        override fun getPasswordAuthentication(): PasswordAuthentication {
            return PasswordAuthentication(userName, password)
        }
    })

    session.debug = true

    try {
        val mimeMessage = MimeMessage(session)
        mimeMessage.setFrom(InternetAddress(userName))
        mimeMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailTo, false))
        mimeMessage.setText(text)
        mimeMessage.subject = subject
        mimeMessage.sentDate = Date()

        val smtpTransport = session.getTransport("smtp")
        smtpTransport.connect()
        smtpTransport.sendMessage(mimeMessage, mimeMessage.allRecipients)
        smtpTransport.close()
    } catch (messagingException: MessagingException) {
        messagingException.printStackTrace()
    }
}

private fun putIfMissing(props: Properties, key: String, value: String) {
    if (!props.containsKey(key)) {
        props[key] = value
    }
}