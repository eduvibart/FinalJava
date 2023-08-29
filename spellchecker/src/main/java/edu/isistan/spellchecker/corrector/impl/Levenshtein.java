package edu.isistan.spellchecker.corrector.impl;

import java.util.HashSet;
import java.util.Set;

import edu.isistan.spellchecker.corrector.Corrector;
import edu.isistan.spellchecker.corrector.Dictionary;

/**
 *
 * Un corrector inteligente que utiliza "edit distance" para generar
 * correcciones.
 * 
 * La distancia de Levenshtein es el n�mero minimo de ediciones que se deber
 * realizar a un string para igualarlo a otro. Por edici�n se entiende:
 * <ul>
 * <li>insertar una letra
 * <li>borrar una letra
 * <li>cambiar una letra
 * </ul>
 *
 * Una "letra" es un caracter a-z (no contar los apostrofes).
 * Intercambiar letras (thsi -> this) <it>no</it> cuenta como una edici�n.
 * <p>
 * Este corrector sugiere palabras que esten a edit distance uno.
 */
public class Levenshtein extends Corrector {
	private Dictionary dict;

	/**
	 * Construye un Levenshtein Corrector usando un Dictionary.
	 * Debe arrojar <code>IllegalArgumentException</code> si el diccionario es null.
	 *
	 * @param dict
	 */
	public Levenshtein(Dictionary dict) {
		if (dict == null)
			throw new IllegalArgumentException(); // STUB
		else
			this.dict = dict;
	}

	/**
	 * @param s palabra
	 * @return todas las palabras a erase distance uno
	 */
	Set<String> getDeletions(String s) {
		StringBuilder sBuilder = new StringBuilder(s);
		Set<String> suggestions = new HashSet<String>();
		char deletedChar;
		for (int i = 0; i < s.length(); i++) {
			deletedChar = sBuilder.charAt(i);
			sBuilder.deleteCharAt(i);
			if (dict.isWord(sBuilder.toString()))
				suggestions.add(sBuilder.toString());
			sBuilder.insert(i, deletedChar);
		}
		return suggestions;
	}

	/**
	 * @param s palabra
	 * @return todas las palabras a substitution distance uno
	 */
	public Set<String> getSubstitutions(String s) {
		Set<String> suggestions = new HashSet<String>();
		StringBuilder possibleWord = new StringBuilder(s);
		char originalLetter;
		for (int i = 0; i < s.length(); i++) {
			originalLetter = possibleWord.charAt(i);
			for (char c = 'a'; c <= 'z'; c++) {
				possibleWord.setCharAt(i, c);
				if (!possibleWord.toString().equals(s))
					if (dict.isWord(possibleWord.toString()))
						suggestions.add(possibleWord.toString());
			}
			possibleWord.setCharAt(i, originalLetter);
		}
		return suggestions;
	}

	/**
	 * @param s palabra
	 * @return todas las palabras a insert distance uno
	 */
	public Set<String> getInsertions(String s) {
		Set<String> suggestions = new HashSet<String>();
		StringBuilder possibleWord = new StringBuilder(s);
		for (int i = 0; i <= possibleWord.length(); i++) {
			for (char c = 'a'; c <= 'z'; c++) {
				possibleWord.insert(i, c);
				if (dict.isWord(possibleWord.toString()))
					suggestions.add(possibleWord.toString());
				possibleWord.deleteCharAt(i);
			}
		}
		return suggestions;
	}

	public Set<String> getCorrections(String wrong) {
		if (wrong == null)
			throw new IllegalArgumentException();
		else{
			Set<String> suggestions = getInsertions(wrong);
			suggestions.addAll(getDeletions(wrong));
			suggestions.addAll(getSubstitutions(wrong));
			return matchCase(wrong, suggestions);	
		}
	}
}
