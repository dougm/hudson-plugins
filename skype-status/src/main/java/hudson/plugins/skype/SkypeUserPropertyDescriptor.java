package hudson.plugins.skype;

import net.sf.json.JSONObject;

import org.kohsuke.stapler.StaplerRequest;

import hudson.Extension;
import hudson.Util;
import hudson.model.User;
import hudson.model.UserProperty;
import hudson.model.UserPropertyDescriptor;

/**
 * Descriptor for the {@link UserScoreProperty}.
 *
 * @author Erik Ramfelt
 */
@Extension
public class SkypeUserPropertyDescriptor extends UserPropertyDescriptor {

    public SkypeUserPropertyDescriptor() {
        super(SkypeUserProperty.class);
    }

    @Override
    public String getDisplayName() {
      return "Skype";
//        return Messages.User_Property_Title();
    }

    /**
     * Method kept for backward compability.
     * Prior to 1.222 the JSONObject formdata was always null. This method
     * should be removed in the future.
     * @param req request coming from config.jelly
     * @return a UserScoreProperty object
     */
    private SkypeUserProperty newInstanceIfJSONIsNull(StaplerRequest req) throws FormException {
        String skypeId = Util.fixEmptyAndTrim(req.getParameter("skypeId"));
        if (skypeId != null) {
            return new SkypeUserProperty(skypeId);
        }
        return new SkypeUserProperty();
    }

    @Override
    public SkypeUserProperty newInstance(StaplerRequest req, JSONObject formData) throws hudson.model.Descriptor.FormException {
        if (formData == null) {
            return newInstanceIfJSONIsNull(req);
        }
        if (formData.has("skypeId")) { //$NON-NLS-1$
            return req.bindJSON(SkypeUserProperty.class, formData);
        }
        return new SkypeUserProperty();
    }

    @Override
    public UserProperty newInstance(User arg0) {
        return null;
    }
}
