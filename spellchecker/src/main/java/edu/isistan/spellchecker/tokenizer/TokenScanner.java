package edu.isistan.spellchecker.tokenizer;

import java.util.Iterator;
import java.io.IOException;
import java.io.BufferedReader;
import java.util.NoSuchElementException;
import java.io.Reader;
/**
 * Dado un archivo provee un método para recorrerlo.
 */
public class TokenScanner implements Iterator<String> {
  private final BufferedReader reader;
  private String nextToken;
  private boolean hasNextToken;

  private StringBuilder invalidWordBuilder = new StringBuilder();
  private StringBuilder validWordBuilder = new StringBuilder();

  /**
   * Crea un TokenScanner.
   * <p>
   * Como un iterador, el TokenScanner solo debe leer lo justo y
   * necesario para implementar los métodos next() y hasNext(). 
   * No se debe leer toda la entrada de una.
   * <p>
   *
   * @param in fuente de entrada
   * @throws IOException si hay algún error leyendo.
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
    while (( c = reader.read()) != -1) {
      if (isWordCharacter(c)){
        if (invalidWordBuilder.length()>0) {
          nextToken = invalidWordBuilder.toString();
          invalidWordBuilder.setLength(0);
          hasNextToken = true;
          validWordBuilder.append((char) c);
          return;
        }else{
          validWordBuilder.append((char) c);
        }
      }else {
        if (validWordBuilder.length()>0){
          nextToken=validWordBuilder.toString();
          validWordBuilder.setLength(0);
          hasNextToken = true;
          invalidWordBuilder.append((char) c);
          return;
        }else{
          invalidWordBuilder.append((char) c);
        }
      }
    }
    if (validWordBuilder.length() > 0) {
      nextToken = validWordBuilder.toString();
      validWordBuilder.setLength(0);
      hasNextToken = true;
    } else if (invalidWordBuilder.length() > 0){
      nextToken=invalidWordBuilder.toString();
      invalidWordBuilder.setLength(0);
      hasNextToken=true;
    }else{
      hasNextToken = false;
    }
  }
  /**
   * Determina si un carácer es una caracter válido para una palabra.
   * <p>
   * Un caracter válido es una letra (
   * Character.isLetter) o una apostrofe '\''.
   *
   * @param c 
   * @return true si es un caracter
   */
  public static boolean isWordCharacter(int c) {
    return Character.isLetter(c) || c == 39;
  }


   /**
   * Determina si un string es una palabra válida.
   * Null no es una palabra válida.
   * Un string que todos sus caracteres son válidos es una 
   * palabra. Por lo tanto, el string vacío es una palabra válida.
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
   * @throws NoSuchElementException cuando se alcanzó el final de stream
   */
  public String next() {
    if (!hasNextToken) {
      throw new NoSuchElementException("No hay más tokens disponibles");
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
