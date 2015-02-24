package au.com.wsit.mailserverfinder;

/**
 * Created by guyb on 16/02/15.
 */
public final class MailServerDB
{

    public static final String[] HOSTNAME_KEYS = {"imap", "smtp", "web", "pop", "pop3", "mail1", "mail2", "mail", "remote"};

    public static final int[] TCP_PORTS = {25, 110, 143, 465, 587, 993, 995, 443};

    public static final int CONNECT_TIMEOUT = 1000;

}
