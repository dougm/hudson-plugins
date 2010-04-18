package hudson.plugins.skype;

import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.export.Exported;
import org.kohsuke.stapler.export.ExportedBean;

import hudson.model.User;
import hudson.model.UserProperty;

/**
 * 
 * @author Erik Ramfelt
 */
@ExportedBean(defaultVisibility = 999)
public class SkypeUserProperty extends UserProperty {

    private String skypeId;

    public SkypeUserProperty() {
        skypeId = null;
    }

    @DataBoundConstructor
    public SkypeUserProperty(String skypeId) {
        this.skypeId = skypeId;
    }

    @Exported
    public User getUser() {
        return user;
    }

    @Exported
    public String getSkypeId() {
        return skypeId;
    }

    @Override
    public String toString() {
        return String.format("SkypeUserProperty [SkypeId=%s, user=%s]", skypeId, user); //$NON-NLS-1$
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof SkypeUserProperty))
            return false;
        SkypeUserProperty other = (SkypeUserProperty) obj;
        if (skypeId.equals(other.skypeId))
            return false;
        return true;
    }
}
