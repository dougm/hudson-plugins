package hudson.plugins.emotional_hudson;

import hudson.model.ProminentProjectAction;
import hudson.model.Result;

public final class EmotionalHudsonAction implements ProminentProjectAction {

    private Result result;

    public EmotionalHudsonAction() {}

    public EmotionalHudsonAction(Result result) {
        this.result = result;
    }

    public String getIconFileName() { return null; }
    public String getDisplayName() { return ""; }
    public String getUrlName() { return ""; }

    public Result getResult() { return result; }
}
