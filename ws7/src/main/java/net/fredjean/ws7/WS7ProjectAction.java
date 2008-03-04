/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.fredjean.ws7;

import hudson.model.AbstractProject;
import hudson.model.Actionable;
import hudson.model.ProminentProjectAction;

/**
 *
 * @author fjean
 */
public class WS7ProjectAction extends Actionable implements ProminentProjectAction {
    private final AbstractProject<?, ?> project;
    private final WS7Publisher publisher;

    public WS7ProjectAction(AbstractProject project, WS7Publisher publisher) {
        this.project = project;
        this.publisher = publisher;
    }

    public String getDisplayName() {
        return "Link to " + project.getName() + " (" + getUrlName() + ")";
    }

    public String getSearchUrl() {
        return getUrlName();
    }

    public String getIconFileName() {
        return "network.gif";
    }

    public String getUrlName() {
        return publisher.getUrlToApplication();
    }

}
