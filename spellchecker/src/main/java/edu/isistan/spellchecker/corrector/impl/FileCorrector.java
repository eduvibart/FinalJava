package edu.isistan.spellchecker.corrector.impl;

import edu.isistan.spellchecker.corrector.Corrector;
import edu.isistan.spellchecker.tokenizer.TokenScanner;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Hashtable;
import java.util.Set;
import java.util.TreeSet;

/**
 * Corrector basado en un archivo.
 * 
 */
public class FileCorrector extends Corrector {

	private Hashtable<String, Set<String>> correctionMap = new Hashtable<String, Set<String>>();

	/**
	 * Clase especial que se utiliza al tener
	 * alg�n error de formato en el archivo de entrada.
	 */
	public static class FormatException extends Exception {
		public FormatException(String msg) {
			super(msg);
		}
	}

	/**
	 * Constructor del FileReader
	 *
	 * Utilice un BufferedReader para leer el archivo de definici�n
	 *
	 * <p>
	 * Cada l�nea del archivo del diccionario tiene el siguiente formato:
	 * misspelled_word,corrected_version
	 *
	 * <p>
	 * Ejemplo:<br>
	 * 
	 * <pre>
	 * aligatur,alligator<br>
	 * baloon,balloon<br>
	 * inspite,in spite<br>
	 * who'ev,who've<br>
	 * ther,their<br>
	 * ther,there<br>
	 * </pre>
	 * <p>
	 * Estas l�neas no son case-insensitive, por lo que todas deber�an generar el
	 * mismo efecto:<br>
	 * 
	 * <pre>
	 * baloon,balloon<br>
	 * Baloon,balloon<br>
	 * Baloon,Balloon<br>
	 * BALOON,balloon<br>
	 * bAlOon,BALLOON<br>
	 * </pre>
	 * <p>
	 * Debe ignorar todos los espacios vacios alrededor de las palabras, por lo
	 * que estas entradas son todas equivalentes:<br>
	 * 
	 * <pre>
	 * inspite,in spite<br>
	 *    inspite,in spite<br>
	 * inspite   ,in spite<br>
	 *  inspite ,   in spite  <br>
	 * </pre>
	 * 
	 * Los espacios son permitidos dentro de las sugerencias.
	 *
	 * <p>
	 * Deber�a arrojar <code>FileCorrector.FormatException</code> si se encuentra
	 * alg�n
	 * error de formato:<br>
	 * 
	 * <pre>
	 * ,correct<br>
	 * wrong,<br>
	 * wrong correct<br>
	 * wrong,correct,<br>
	 * </pre>
	 * <p>
	 *
	 * @param r Secuencia de caracteres
	 * @throws IOException                   error leyendo el archivo
	 * @throws FileCorrector.FormatException error de formato
	 * @throws IllegalArgumentException      reader es null
	 */
	public FileCorrector(Reader r) throws IOException, FormatException {
		if (r == null)
			throw new IllegalArgumentException();
		if (r instanceof FileReader){
			TokenScanner token = new TokenScanner(r);
			String nextToken;
			String value;
			while (token.hasNext()) {
				nextToken = token.next();
				while (!token.isWord(nextToken)) {
					nextToken = token.next();
				}
				value = token.next();
				while (token.hasNext() && !token.isWord(value)) {
					value = token.next();
				}
				if (correctionMap.containsKey(nextToken.toLowerCase())) {
					correctionMap.get(nextToken).add(value);

				} else {
					Set<String> newSet = new TreeSet<String>();
					newSet.add(value);
					correctionMap.put(nextToken.toLowerCase(), newSet);
				}
			}
		}
		else
			throw new FormatException("Reader type must be java.io.BufferReader");
	}

	/**
	 * Construye el Filereader.
	 *
	 * @param filename
	 * @throws IOException
	 * @throws FileCorrector.FormatException
	 * @throws FileNotFoundException
	 */
	public static FileCorrector make(String filename) throws IOException, FormatException {
		Reader r = new FileReader(filename);
		FileCorrector fc;
		try {
			fc = new FileCorrector(r);
		} finally {
			if (r != null) {
				r.close();
			}
		}
		return fc;
	}

	/**
	 * Retorna una lista de correcciones para una palabra dada.
	 * Si la palabra mal escrita no est� en el diccionario el set es vacio.
	 * <p>
	 * Ver superclase.
	 *
	 * @param wrong
	 * @return retorna un conjunto (potencialmente vac�o) de sugerencias.
	 * @throws IllegalArgumentException si la entrada no es una palabra v�lida
	 */

	public Set<String> getCorrections(String wrong) {

		Set<String> corrections = new TreeSet<String>();
		if (wrong != null) {
			try {
				corrections = correctionMap.get(wrong.toLowerCase());
				return corrections;

			} catch (NullPointerException e) {
				System.out.println("nothing");

			} finally {
				return matchCase(wrong, corrections);
			}
		} else {
			throw new IllegalArgumentException();
		}
	}
}
