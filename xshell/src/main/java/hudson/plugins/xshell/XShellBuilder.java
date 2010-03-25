package hudson.plugins.xshell;

import java.io.IOException;
import java.io.PrintStream;
import java.util.Map;

import hudson.EnvVars;
import hudson.Extension;
import hudson.Launcher;
import hudson.Util;
import hudson.model.AbstractBuild;
import hudson.model.BuildListener;
import hudson.model.Descriptor;
import hudson.tasks.Builder;
import hudson.util.ArgumentListBuilder;

import org.kohsuke.stapler.DataBoundConstructor;

/**
 * XShell Builder Plugin.
 *
 * @author Marco Ambu
 */
public final class XShellBuilder extends Builder {

  @Extension
  public static final XShellDescriptor DESCRIPTOR = new XShellDescriptor();

  /**
   * Set to true for debugging.
   */
  private static final boolean DEBUG = true;

  /**
   * Script home dir.
   */
  private final String script;

  public String getScript() {
      return script;
  }

  @DataBoundConstructor
  public XShellBuilder(final String script) {
    this.script = Util.fixEmptyAndTrim(script);
  }

  @Override
  public Descriptor<Builder> getDescriptor() {
    return DESCRIPTOR;
  }

  @Override
  public boolean perform(final AbstractBuild<?, ?> build, final Launcher launcher, final BuildListener listener)
          throws InterruptedException, IOException {

    ArgumentListBuilder args = new ArgumentListBuilder();
    final EnvVars env = build.getEnvironment(listener);
    if (script != null) {
      args.addTokenized(script);
    }

    if (!launcher.isUnix()) {
      args.add("&&", "exit", "%%ERRORLEVEL%%");
      args = new ArgumentListBuilder().add("cmd.exe", "/C").addQuoted(args.toStringWithQuote());
    }

    if (DEBUG) {
      final PrintStream logger = listener.getLogger();
      for (final Map.Entry<String, String> entry : env.entrySet()) {
          logger.println("(DEBUG) env: key= " + entry.getKey() + " value= " + entry.getValue());
      }
      logger.println("Args: " + args.toStringWithQuote());
      logger.println("Working dir: " + build.getModuleRoot());
    }

    final long startTime = System.currentTimeMillis();
    try {
      final int result = launcher.launch().cmds(args).envs(env).stdout(listener).pwd(build.getModuleRoot())/*.pwd(phpFilePath.getParent())*/.join();
      return result == 0;
    } catch (final IOException e) {
      Util.displayIOException(e, listener);
      final long processingTime = System.currentTimeMillis() - startTime;
      final String errorMessage = Messages.XShell_ExecFailed();
      e.printStackTrace(listener.fatalError(errorMessage));
      return false;
    }
  }

}