package com.vinplay.service;

import org.apache.log4j.Logger;

/**
 * Log Txt
 *
 * @author Administrator
 */
public class LogXY {
  private String msg = null;
  private Logger logger = Logger.getLogger(LogXY.class);

  public String getMsg() {
    return msg;
  }

  public void setMsg(String msg) {
    this.msg = msg;
  }

  public Logger getLogger() {
    return logger;
  }

  public void setLogger(Logger logger) {
    this.logger = logger;
  }
}
