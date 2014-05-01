package com.julienvey.trello.exception;

public class TrelloHttpException extends RuntimeException {

  public TrelloHttpException() {}

  public TrelloHttpException(String message) {
    super(message);
  }

  public TrelloHttpException(String message, Throwable cause) {
    super(message, cause);
  }

  public TrelloHttpException(Throwable cause) {
    super(cause);
  }

}
