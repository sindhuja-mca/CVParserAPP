/**
 * 
 */
package com.hsi.parsing.exception;

/**
 * Created by sindhuja
 *
 */
public class ResumeParsingException extends RuntimeException {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  public ResumeParsingException() {
    super();

  }

  public ResumeParsingException(String message, Throwable cause, boolean enableSuppression,
                                boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);

  }

  public ResumeParsingException(String message, Throwable cause) {
    super(message, cause);

  }

  public ResumeParsingException(String message) {
    super(message);

  }

  public ResumeParsingException(Throwable cause) {
    super(cause);

  }
}