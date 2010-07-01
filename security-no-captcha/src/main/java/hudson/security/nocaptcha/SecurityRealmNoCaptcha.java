/*
 * The MIT License
 *
 * Copyright (c) 2004-2009, Sun Microsystems, Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package hudson.security.nocaptcha;

import static javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;
import hudson.Extension;
import hudson.model.Descriptor;
import hudson.model.Hudson;
import hudson.model.User;
import hudson.security.AuthorizationStrategy;
import hudson.security.GlobalMatrixAuthorizationStrategy;
import hudson.security.HudsonPrivateSecurityRealm;
import hudson.security.SecurityRealm;
import hudson.tasks.Mailer;

import java.io.IOException;

import javax.servlet.ServletException;

import org.acegisecurity.Authentication;
import org.acegisecurity.context.SecurityContextHolder;
import org.acegisecurity.providers.UsernamePasswordAuthenticationToken;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;

/**
 * The only purpose of this realm is to avoid having to enter a CAPTCHA.
 * 
 * @author Jacob Robertson
 */
public class SecurityRealmNoCaptcha extends HudsonPrivateSecurityRealm {

    @DataBoundConstructor
    public SecurityRealmNoCaptcha() {
        super(true);
    }

    /**
     * Creates an user account. Used for self-registration.
     */
    public void doCreateAccount(StaplerRequest req, StaplerResponse rsp) throws IOException, ServletException {
        if(!allowsSignup()) {
            rsp.sendError(SC_UNAUTHORIZED,"User sign up is prohibited");
            return;
        }
        boolean firstUser = !hasSomeUser();
        // set selfRegistration = false to avoid captcha check
        User u = createAccount(req, rsp, false, "signup.jelly");
        if(u!=null) {
            if(firstUser)
                tryToMakeAdmin(u);  // the first user should be admin, or else there's a risk of lock out
            loginAndTakeBack(req, rsp, u);
        }
    }
    
    /**
     * COPIED FROM SUPERCLASS - because it's private!
     * Computes if this Hudson has some user accounts configured.
     *
     * <p>
     * This is used to check for the initial
     */
    protected static boolean hasSomeUser() {
        for (User u : User.getAll())
            if(u.getProperty(Details.class)!=null)
                return true;
        return false;
    }
    
    /**
     * COPIED FROM SUPERCLASS - because it's private!
     * Try to make this user a super-user
     */
    protected void tryToMakeAdmin(User u) {
        AuthorizationStrategy as = Hudson.getInstance().getAuthorizationStrategy();
        if (as instanceof GlobalMatrixAuthorizationStrategy) {
            GlobalMatrixAuthorizationStrategy ma = (GlobalMatrixAuthorizationStrategy) as;
            ma.add(Hudson.ADMINISTER,u.getId());
        }
    }
    
    /**
     * COPIED FROM SUPERCLASS - because it's private!
     * Lets the current user silently login as the given user and report back accordingly.
     */
    protected void loginAndTakeBack(StaplerRequest req, StaplerResponse rsp, User u) throws ServletException, IOException {
        // ... and let him login
        Authentication a = new UsernamePasswordAuthenticationToken(u.getId(),req.getParameter("password1"));
        a = this.getSecurityComponents().manager.authenticate(a);
        SecurityContextHolder.getContext().setAuthentication(a);

        // then back to top
        req.getView(this,"success.jelly").forward(req,rsp);
    }
    
    /**
     * COPIED FROM SUPERCLASS - because it's private!
     * @return
     *      null if failed. The browser is already redirected to retry by the time this method returns.
     *      a valid {@link User} object if the user creation was successful.
     */
    protected User createAccount(StaplerRequest req, StaplerResponse rsp, boolean selfRegistration, String formView) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        // form field validation
        // this pattern needs to be generalized and moved to stapler
        SignupInfo si = new SignupInfo();
        req.bindParameters(si);

        if(selfRegistration && !validateCaptcha(si.captcha))
            si.errorMessage = "Text didn't match the word shown in the image";

        if(si.password1 != null && !si.password1.equals(si.password2))
            si.errorMessage = "Password didn't match";

        if(!(si.password1 != null && si.password1.length() != 0))
            si.errorMessage = "Password is required";

        if(si.username==null || si.username.length()==0)
            si.errorMessage = "User name is required";
        else {
            User user = User.get(si.username);
            if(user.getProperty(Details.class)!=null)
                si.errorMessage = "User name is already taken. Did you forget the password?";
        }

        if(si.fullname==null || si.fullname.length()==0)
            si.fullname = si.username;

        if(si.email==null || !si.email.contains("@"))
            si.errorMessage = "Invalid e-mail address";

        if(si.errorMessage!=null) {
            // failed. ask the user to try again.
            req.setAttribute("data",si);
            req.getView(this, formView).forward(req,rsp);
            return null;
        }

        // register the user
        User user = createAccount(si.username,si.password1);
        user.addProperty(new Mailer.UserProperty(si.email));
        user.setFullName(si.fullname);
        user.save();
        return user;
    }
    
    @Extension
    public static final class DescriptorImpl extends Descriptor<SecurityRealm> {
        public String getDisplayName() {
            return "Hudson's own user database (without a CAPTCHA)";
        }
    }
}
