package edu.calstatela.cpham24.eloteroman.DisplayActivities.data;

/**
 * Created by Johnson on 7/30/2017.
 */

public class Vender {

    private String name;
    private String CName;
    private String desc;
    private String workHour;
    private boolean work;


    public Vender(String na, String cn, String de, String wo, String ye) {
        this.name = na;
        this.CName = cn;
        this.desc = de;
        this.workHour = wo;
        this.work = Boolean.parseBoolean(ye);
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCName() { return CName; }

    public void setCName(String CName) { this.CName = CName; }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getWorkHour() {
        return workHour;
    }

    public void setWorkHour(String workHour) {
        this.workHour = workHour;
    }

    public boolean getWork() {return work; }

    public void setWork(boolean ye) {this.work = ye; }


}
