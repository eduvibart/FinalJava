package edu.isistan.spellchecker.corrector.impl;

import java.util.HashSet;
import java.util.Set;

import edu.isistan.spellchecker.corrector.Corrector;
import edu.isistan.spellchecker.corrector.Dictionary;

/**
 * Este corrector sugiere correciones cuando dos letras adyacentes han sido
 * cambiadas.
 * <p>
 * Un error com�n es cambiar las letras de orden, e.g.
 * "with" -> "wiht". Este corrector intenta dectectar palabras con exactamente
 * un swap.
 * <p>
 * Por ejemplo, si la palabra mal escrita es "haet", se debe sugerir
 * tanto "heat" como "hate".
 * <p>
 * Solo cambio de letras contiguas se considera como swap.
 */
public class SwapCorrector extends Corrector {

	private Dictionary dict;

	/**
	 * Construcye el SwapCorrector usando un Dictionary.
	 *
	 * @param dict
	 * @throws IllegalArgumentException si el diccionario provisto es null
	 */
	public SwapCorrector(Dictionary dict) {
		if (dict == null)
			throw new IllegalArgumentException();
		else
			this.dict = dict;
	}

	/**
	 * 
	 * Este corrector sugiere correciones cuando dos letras adyacentes han sido
	 * cambiadas.
	 * <p>
	 * Un error com�n es cambiar las letras de orden, e.g.
	 * "with" -> "wiht". Este corrector intenta dectectar palabras con exactamente
	 * un swap.
	 * <p>
	 * Por ejemplo, si la palabra mal escrita es "haet", se debe sugerir
	 * tanto "heat" como "hate".
	 * <p>
	 * Solo cambio de letras contiguas se considera como swap.
	 * <p>
	 * Ver superclase.
	 *
	 * @param wrong
	 * @return retorna un conjunto (potencialmente vac�o) de sugerencias.
	 * @throws IllegalArgumentException si la entrada no es una palabra v�lida
	 */
	public Set<String> getCorrections(String wrong) {
		char aux;
		Set<String> suggestions = new HashSet<String>();
		char[] word = wrong.toCharArray();
		for (int i = 0; i < word.length - 1; i++) {
			aux = word[i];
			word[i] = word[i + 1];
			word[i + 1] = aux;
			String possibleWord = new String(word);
			if (dict.isWord(possibleWord))
				suggestions.add(possibleWord);
			word[i + 1] = word[i];
			word[i] = aux;
		}
		return matchCase(wrong, suggestions);
	}
}
