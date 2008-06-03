package hudson.plugins.crap4j.calculation;

import hudson.plugins.crap4j.display.ICrapComparison;
import hudson.plugins.crap4j.model.IMethodCrap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

public class CrapDataComparer implements ICrapComparison {
	
	private final List<IMethodCrap> newMethods;
	private final List<IMethodCrap> fixedMethods;
	private final List<IMethodCrap> unchangedMethods;

	public CrapDataComparer(IMethodCrap[] oldCrapMethods, IMethodCrap[] newCrapMethods) {
		super();
		this.newMethods = new ArrayList<IMethodCrap>();
		this.fixedMethods = new ArrayList<IMethodCrap>();
		this.unchangedMethods = new ArrayList<IMethodCrap>();
		performComparison(oldCrapMethods, newCrapMethods);
	}
	
	private Map<String, IMethodCrap> createCrapMethodMapFor(IMethodCrap... crapMethods) {
		Map<String, IMethodCrap> result = new HashMap<String, IMethodCrap>();
		for (IMethodCrap methodCrap : crapMethods) {
			result.put(getUniqueNameOf(methodCrap), methodCrap);
		}
		return result;
	}
	
	private String getUniqueNameOf(IMethodCrap method) {
		StringBuilder result = new StringBuilder();
		result.append(method.getPackageName());
		result.append(".");
		result.append(method.getClassName());
		result.append(".");
		result.append(method.getMethodName());
		result.append(":");
		result.append(method.getMethodSignature());
		return result.toString();
	}
	
	private void performComparison(IMethodCrap[] oldCrapMethods, IMethodCrap[] newCrapMethods) {
		Map<String, IMethodCrap> oldCrapMethodMap = createCrapMethodMapFor(oldCrapMethods);
		Map<String, IMethodCrap> newCrapMethodMap = createCrapMethodMapFor(newCrapMethods);
		Set<Entry<String, IMethodCrap>> entrySet = oldCrapMethodMap.entrySet();
		for (Entry<String, IMethodCrap> entry : entrySet) {
			if (newCrapMethodMap.containsKey(entry.getKey())) {
				this.unchangedMethods.add(entry.getValue());
				newCrapMethodMap.remove(entry.getKey());
			} else {
				this.fixedMethods.add(entry.getValue());
			}
		}
		this.newMethods.addAll(newCrapMethodMap.values());
	}
	
	//@Override
	public IMethodCrap[] getFixedCrapMethods() {
		return getAsArray(this.fixedMethods);
	}
	
	private IMethodCrap[] getAsArray(List<IMethodCrap> methodList) {
		return methodList.toArray(new IMethodCrap[methodList.size()]);
	}
	
	//@Override
	public IMethodCrap[] getNewCrapMethods() {
		return getAsArray(this.newMethods);
	}
	
	//@Override
	public IMethodCrap[] getUnchangedCrapMethods() {
		return getAsArray(this.unchangedMethods);
	}
}
