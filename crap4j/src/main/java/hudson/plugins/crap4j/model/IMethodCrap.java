package hudson.plugins.crap4j.model;

public interface IMethodCrap {

	public abstract String getPackageName();

	public abstract String getClassName();

	public abstract String getMethodName();

	public abstract String getMethodSignature();

	public abstract String getFullMethod();

	public abstract double getCrap();

	public abstract double getComplexity();

	public abstract double getCoveragePercent();

	public abstract int getCrapLoad();
}