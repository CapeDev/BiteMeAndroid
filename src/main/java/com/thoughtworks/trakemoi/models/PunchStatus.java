package com.thoughtworks.trakemoi.models;

public class PunchStatus {

    private Long id;
    private String status;
    private String zoneName;
    private String date;
    private String time;

    public Long getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }

    public String getZoneName() {
        return zoneName;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public void setId(long id) {
        this.id = id;
    }

    public PunchStatus(StatusBuilder statusBuilder) {
        this.status = statusBuilder.status;
        this.zoneName = statusBuilder.zoneName;
        this.date = statusBuilder.date;
        this.time = statusBuilder.time;
        this.id = statusBuilder.id;
    }

    public static class StatusBuilder{
        private Long id = -1L;
        private String status;
        private String date;
        private String time;
        public String zoneName;

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

        public StatusBuilder withZoneName(String zoneName){
            this.zoneName = zoneName;
            return this;
        }

        private String setBlankIfNull(String name) {
            return (name == null) ? "" : name;
        }
    }
}