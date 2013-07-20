package com.thoughtworks.trakemoi.models;

public class PunchStatus {

    private Long id;

    private String status;
    private String date;
    private String time;

    public PunchStatus(StatusBuilder statusBuilder) {
        this.status = statusBuilder.status;
        this.date = statusBuilder.date;
        this.time = statusBuilder.time;
        this.id = statusBuilder.id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public static class StatusBuilder{
        private Long id = -1L;
        private String status;
        private String date;
        private String time;

        public PunchStatus build(){
            return new PunchStatus(this);
        }

        public StatusBuilder(String status){
            this.status = setBlankIfNull(status);
        }

        public StatusBuilder withId(Long id){
            this.id = id;
            return this;
        }

        public StatusBuilder withDate(String date){
            this.date = date;
            return this;
        }

        public StatusBuilder withTime(String time){
            this.time = time;
            return this;
        }

        private String setBlankIfNull(String name) {
            return (name == null) ? "" : name;
        }
    }
}