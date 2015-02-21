package au.com.wsit.mailserverfinder;

/**
 * Created by guyb on 16/02/15.
 */
public final class MailServerDB
{

    public static final String[] HOSTNAME_KEYS = {"remote", "imap", "smtp", "mail", "web", "pop", "pop3", "mail1", "mail2"};

    public static final String[] TCP_PORT_KEYS = {"IMAP_PLAIN", "IMAP_SSL", "POP3_PLAIN", "POP3_SSL", "SMTP_PLAIN", "SMTP_TLS", "SMTP_SSL", "EXCHANGE"};

    public static final int[] TCP_PORTS = {25, 110, 143, 465, 587, 993, 995, 443};

    public static final int CONNECT_TIMEOUT = 2000;



}
