package com.redfin.hudson;

import static hudson.Util.*;
import hudson.model.BuildableItem;
import hudson.model.Item;
import hudson.scheduler.CronTabList;
import hudson.triggers.Trigger;
import hudson.triggers.TriggerDescriptor;
import hudson.util.FormFieldValidator;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.servlet.ServletException;

import net.sf.json.JSONObject;

import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;

import antlr.ANTLRException;

/** Triggers a build when the data at a particular URL has changed. */
public class UrlChangeTrigger extends Trigger<BuildableItem> {

    URL url;
    String oldMd5;
    
    private static final int readBufferSize = 8*1024;
    /** Extracts the MD5 string from the specified input stream; this MD5 should
     * be a perfect match for the GNU "md5sum" tool.
     */
    private static String getMd5(InputStream stream) throws IOException {
        MessageDigest messageDigest;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        DigestInputStream dis = new DigestInputStream(stream,
                messageDigest);
        byte[] buf = new byte[readBufferSize];
        while (dis.read(buf, 0, readBufferSize) != -1) {
        ;
        }
        dis.close();
        stream.close();
        byte[] fileDigest = messageDigest.digest ();
        return createDigestString(fileDigest);
    }
    
    /** byte array to string */
    private static String createDigestString(byte[] fileDigest) {
        StringBuffer checksumSb = new StringBuffer();
        for (int i = 0; i < fileDigest.length; i++) {
            String hexStr = Integer.toHexString(0x00ff & fileDigest[i]);
            if (hexStr.length() < 2) {
                checksumSb.append("0");
            }
            checksumSb.append(hexStr);
        }
        return checksumSb.toString();
    }

    public UrlChangeTrigger(String url) throws MalformedURLException {
        this(new URL(url));
    }
    
    public UrlChangeTrigger(URL url) {
        this.url = url;
        try {
            this.tabs = CronTabList.create("* * * * *");
        } catch (ANTLRException e) {
            throw new RuntimeException("Bug! couldn't schedule poll");
        }
    }

    @Override
    public void run() {
        try {
            String currentMd5 = getMd5(url.openStream());
            if (!currentMd5.equals(oldMd5)) {
                oldMd5 = currentMd5;
                job.scheduleBuild();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    public URL getUrl() {
        return url;
    }
    
    @Override
    public TriggerDescriptor getDescriptor() {
        return DESCRIPTOR;
    }
    
    /**
     * Descriptor should be singleton.
     */
    public static final DescriptorImpl DESCRIPTOR = new DescriptorImpl();
    
    public static final class DescriptorImpl extends TriggerDescriptor {

        public DescriptorImpl() {
            super(UrlChangeTrigger.class);
        }

        @Override
        public boolean isApplicable(Item item) {
            return true;
        }

        @Override
        public String getDisplayName() {
            return "Build when a URL's content changes";
        }
        
        @Override
        public String getHelpFile() {
            return "/plugin/url-change-trigger/help-whatIsUrlChangeTrigger.html";
        }
        
        /**
         * Performs syntax check.
         */
        public void doCheck(StaplerRequest req, StaplerResponse rsp) throws IOException, ServletException {
            FormFieldValidator validator = new FormFieldValidator(req,rsp,true) {
                protected void check() throws IOException, ServletException {
                    try {
                        new URL(fixNull(request.getParameter("urlChangeTrigger.url")));
                        ok();
                    } catch (MalformedURLException e) {
                        error(e.getMessage());
                    }
                }
            };
            validator.process();
        }
        
        @Override
        public UrlChangeTrigger newInstance(StaplerRequest req, JSONObject formData) throws FormException {
            String url = formData.getString("url");
            try {
                return new UrlChangeTrigger(url);
            } catch (MalformedURLException e) {
                throw new FormException("Invalid URL: " + url, e, "");
            }
        }
        
    }

}
