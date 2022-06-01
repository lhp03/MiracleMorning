package swdm2016.gachon.mr_demo;

import java.io.Serializable;

public class RoutineDetail implements Serializable {

    private int id;
    private String title;
    private String type;
    private int hour;
    private int minutes;
    private int runtime;
    private int isDone;
    private int routine_id;

    public RoutineDetail(String title, String type, int hour, int minutes, int isDone, int routine_id) {
        this.title = title;
        this.type = type;
        this.hour = hour;
        this.minutes = minutes;
        this.isDone = isDone;
        this.routine_id = routine_id;
    }

    public RoutineDetail(String title, String type, int runtime, int isDone, int routine_id) {
        this.title = title;
        this.type = type;
        this.runtime = runtime;
        this.isDone = isDone;
        this.routine_id = routine_id;
    }


    public RoutineDetail(int id, String title, String type, int hour, int minutes, int isDone, int routine_id) {
        this.id = id;
        this.title = title;
        this.type = type;
        this.hour = hour;
        this.minutes = minutes;
        this.isDone = isDone;
        this.routine_id = routine_id;
    }

    public RoutineDetail(int id, String title, String type, int runtime, int isDone, int routine_id) {
        this.id = id;
        this.title = title;
        this.type = type;
        this.runtime = runtime;
        this.isDone = isDone;
        this.routine_id = routine_id;
    }

    public String getTitle() {
        return title;
    }

    public String getType() {
        return type;
    }

    public int getHour() {
        return hour;
    }

    public int getMinutes() {
        return minutes;
    }

    public int getRuntime() {
        return runtime;
    }

    public int getIsDone() { return isDone; };

    public int getRoutine_id() {return routine_id; }

    public int getId() { return id; }

    public void setId(int id) {
        this.id = id;
    }

    public void setIsDone(int isDone) {
        this.isDone = isDone;
    }

    public void setRoutine_id(int id) { this.routine_id = routine_id;}

    public String toString() {
        return String.format("%d %s %s %d %d %d %d %d\n", id, title, type, hour, minutes, runtime, isDone, routine_id);
    }
}
