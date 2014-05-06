package com.android.willen.autoshutdown.server;

public class ServiceStatus
{
  public static ServiceStatus instance = new ServiceStatus();
  private boolean isRunning = false;

  public static ServiceStatus getInstance()
  {
    return instance;
  }

  public boolean isRunning()
  {
    return this.isRunning;
  }

  public void setRunning(boolean paramBoolean)
  {
    this.isRunning = paramBoolean;
  }
}
