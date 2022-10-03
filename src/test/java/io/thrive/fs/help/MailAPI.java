package io.thrive.fs.help;

import javax.mail.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class MailAPI {
    private Properties mailProperties;
    private String user;
    private String password;
    private String host;

    private Store store;
    private int oldMessageCount;

    public MailAPI() throws MessagingException {
        this.user = "vladimir.pavlyukov@thrive.io";
        this.password = "dcmylouazhaxkmut";
        this.host = "imap.yandex.ru";

        // Свойства установки
        this.mailProperties = new Properties();
        this.mailProperties.put("mail.store.protocol", "imaps");  //SSL

        this.store = Session.getInstance(this.mailProperties).getStore();
        this.store.connect(this.host, this.user, this.password);

        Folder inbox = getOpenedFolder("INBOX", Folder.READ_ONLY);

        this.oldMessageCount = inbox.getMessageCount();
    }

    public void printParts(Object o) throws MessagingException, IOException {
        if (o instanceof String) {
            System.out.println("This is a String");
            System.out.println((String) o);
        } else if (o instanceof Multipart) {
            System.out.println("This is a Multipart");
            Multipart mp = (Multipart) o;
            int count = mp.getCount();
            for (int i = 0; i < count; i++) {
                BodyPart bp = mp.getBodyPart(i);
                System.out.println(bp.getContentType());
                System.out.println(bp.getContent());
                printParts(bp);
            }
        } else if (o instanceof InputStream) {
            System.out.println("This is just an input stream");
            InputStream is = (InputStream) o;
            int c;
            while ((c = is.read()) != -1)
                System.out.write(c);
        }
    }

    private Folder getOpenedFolder(String folderName, int mode) throws MessagingException {
        if (folderName == null || folderName.equals("")) {
            folderName = "INBOX";
        }
        if (mode != Folder.READ_ONLY && mode != Folder.READ_WRITE) {
            mode = Folder.READ_ONLY;
        }
        if(!this.store.isConnected()){
            this.store.connect(this.host, this.user, this.password);
        };
        Folder folder = this.store.getFolder(folderName);
        folder.open(mode);
        return folder;
    }

    public int getMessageCount() {
        return this.oldMessageCount;
    }

    public int messageCountRefresh() throws MessagingException {
        Folder folder = getOpenedFolder("INBOX", Folder.READ_ONLY);
        this.oldMessageCount = folder.getMessageCount();
        return this.oldMessageCount;
    }

    public List<Message> getNewMessagesList(boolean refreshingMessageCount) throws MessagingException {
        int oldMessageCount = this.oldMessageCount;
        Folder fld = getOpenedFolder("INBOX", Folder.READ_ONLY);
        int newMessageCount = fld.getMessageCount();
        List<Message> lst = new ArrayList<>();
        if (oldMessageCount == newMessageCount) {
            return lst;
        }
        if (refreshingMessageCount) {
            this.oldMessageCount = newMessageCount;
        }
        for (int i = oldMessageCount + 1; i <= newMessageCount; i++) {
            lst.add(fld.getMessage(i));
        }
        return lst;
    }

    public boolean isNewMessagesPresent() throws MessagingException {
        int oldMessageCount = this.oldMessageCount;
        Folder fld = getOpenedFolder("INBOX", Folder.READ_ONLY);
        int newMessageCount = fld.getMessageCount();
        return oldMessageCount != newMessageCount;
    }

    public static String[] getAddressFrom(Message message) throws MessagingException {
        return Arrays.stream(message.getFrom()).map(Address::toString).toArray(String[]::new);
    }

    public static String[] getAddressTo(Message message) throws MessagingException {
        return Arrays.stream(message.getAllRecipients()).map(Address::toString).toArray(String[]::new);
    }

    public static String getFirstPart(Message message) throws MessagingException, IOException {
        Object o = message.getContent();
        Multipart mp = (Multipart) o;
        BodyPart bp = mp.getBodyPart(0);
        return bp.getContent().toString();
    }

    public String getFluencyStrikersRegistrationLinkFromMail(String mail, int timeoutSec)
            throws InterruptedException, IOException {
        long start = System.currentTimeMillis() / 1000;
        System.out.println("старт: " + start);
        String link = null;
        while ((System.currentTimeMillis() / 1000 - start) < timeoutSec) {
            try {
                if (isNewMessagesPresent()) {
                    List<Message> newMessages = getNewMessagesList(false);
                    System.out.println("получено " + newMessages.size() + " сообщения/е/ий");
                    for (Message message : newMessages) {
                        if ("Registration mail".equals(message.getSubject())) {
                            String[] mails = getAddressTo(message);
                            for (String address : mails) {
                                if (mail.equals(address)) {
                                    String text = getFirstPart(message);
                                    int index = text.indexOf("https://fluency-strikers");
                                    link = text.substring(index);
                                    return link;
                                }
                                ;
                            }
                        }
                    }
                    this.oldMessageCount += newMessages.size();
                }
            } catch (MessagingException e) {
                System.out.println("была ошибка сообщений");
                System.out.println(e.getStackTrace());
            } finally {
                if (link == null) {
                    System.out.println("подождем 3 сек");
                    Thread.sleep(3000);
                }
            }
        }
        return null;
    }
}
