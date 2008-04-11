package org.jvnet.hudson.plugins.fit;

import fit.ColumnFixture;

public class FibonacciFixture extends ColumnFixture {
	public int rang;

	public int valeur() {
		return somme2rangsPrecedents(rang);
	}

	private int somme2rangsPrecedents(int rang) {
		if (rang == 0) {
			return 0;
		} else if (rang == 1) {
			return 1;
		}
		int somme = somme2rangsPrecedents(rang - 1)
				+ somme2rangsPrecedents(rang - 2);
		return somme;
	}
}
