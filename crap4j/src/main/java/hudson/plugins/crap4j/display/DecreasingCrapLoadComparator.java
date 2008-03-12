package hudson.plugins.crap4j.display;

import hudson.plugins.crap4j.model.IMethodCrap;

import java.util.Comparator;

public class DecreasingCrapLoadComparator implements Comparator<IMethodCrap> {
	
	public DecreasingCrapLoadComparator() {
		super();
	}
	
	public int compare(IMethodCrap o1, IMethodCrap o2) {
		return (o2.getCrapLoad() - o1.getCrapLoad());
	}
}
