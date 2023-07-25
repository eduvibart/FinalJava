package edu.isistan.spellchecker.tokenizer;

import java.util.Iterator;
import java.io.IOException;
import java.io.BufferedReader;
import java.util.NoSuchElementException;
import java.io.Reader;
/**
 * Dado un archivo provee un m�todo para recorrerlo.
 */
public class TokenScanner implements Iterator<String> {
  private final BufferedReader reader;
  private String nextToken;
  private boolean hasNextToken;

  private StringBuilder spaceBuilder = new StringBuilder();
  private StringBuilder wordBuilder = new StringBuilder();

  /**
   * Crea un TokenScanner.
   * <p>
   * Como un iterador, el TokenScanner solo debe leer lo justo y
   * necesario para implementar los m�todos next() y hasNext(). 
   * No se debe leer toda la entrada de una.
   * <p>
   *
   * @param in fuente de entrada
   * @throws IOException si hay alg�n error leyendo.
   * @throws IllegalArgumentException si el Reader provisto es null
   */
  public TokenScanner(java.io.Reader in) throws IOException {
    if (in == null) {
      throw new IOException("El Reader no puede ser nulo");
    }
    reader = new BufferedReader(in);
    findNextToken();
  }
  private void findNextToken() throws IOException {

    int c;
    while ((c = reader.read()) != -1) {
      if (isWordCharacter(c)) {
        if ((spaceBuilder.length()>0)){
          nextToken=spaceBuilder.toString();
          spaceBuilder.setLength(0);
          hasNextToken = true;
          wordBuilder.append((char) c);
          return;
        }else{
          wordBuilder.append((char) c);
        }
      }
      else if (Character.isWhitespace(c)) {
        if (wordBuilder.length()>0){
          nextToken=wordBuilder.toString();
          wordBuilder.setLength(0);
          hasNextToken = true;
          spaceBuilder.append((char) c);
          return;
        }else{
          spaceBuilder.append((char) c);
        }
      }

    }

    if (wordBuilder.length() > 0) {
      nextToken = wordBuilder.toString();
      wordBuilder.setLength(0);
      hasNextToken = true;
    } else if (spaceBuilder.length() > 0){
      nextToken =spaceBuilder.toString();
      spaceBuilder.setLength(0);
      hasNextToken = true;
    }else{
      hasNextToken = false;
    }
  }
  /**
   * Determina si un car�cer es una caracter v�lido para una palabra.
   * <p>
   * Un caracter v�lido es una letra (
   * Character.isLetter) o una apostrofe '\''.
   *
   * @param c 
   * @return true si es un caracter
   */
  public static boolean isWordCharacter(int c) {
    return Character.isLetter(c) || c == 39;
  }


   /**
   * Determina si un string es una palabra v�lida.
   * Null no es una palabra v�lida.
   * Un string que todos sus caracteres son v�lidos es una 
   * palabra. Por lo tanto, el string vac�o es una palabra v�lida.
   * @param s 
   * @return true si el string es una palabra.
   */
   public static boolean isWord(String s) {
     if ((s == null)||(s.length()==0)) {
       return false;
     }

     for (int i = 0; i < s.length(); i++) {
       char c = s.charAt(i);
       if (!isWordCharacter(c)) {
         return false;
       }
     }

     return true;
   }

  /**
   * Determina si hay otro token en el reader.
   */
  public boolean hasNext() {
    return hasNextToken;
  }

  /**
   * Retorna el siguiente token.
   *
   * @throws NoSuchElementException cuando se alcanz� el final de stream
   */
  public String next() {
    if (!hasNextToken) {
      throw new NoSuchElementException("No hay m�s tokens disponibles");
    }

    String token = nextToken;

    try {
      findNextToken();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    return token;

  }
}
