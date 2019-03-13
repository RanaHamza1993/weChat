package com.example.wechat.model;

public class Messages {
   private String message;
    private long time;
   private boolean seen;
   private String type,from;



   public Messages(){

   }
    public Messages(String message, long time, boolean seen, String type,String from) {
        this.message = message;
        this.time = time;
        this.seen = seen;
        this.type = type;
        this.from=from;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public boolean isSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
