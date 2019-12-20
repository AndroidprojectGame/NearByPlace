package model;

/**
 * Created by mappsupport on 08-05-2018.
 */

public class MenuModelClass
{

    private String carname;

    public String getCarname() {
        return carname;
    }

    public void setCarname(String carname) {
        this.carname = carname;
    }

    public int getCarimage() {
        return carimage;
    }

    public void setCarimage(int carimage) {
        this.carimage = carimage;
    }

    private int carimage;

    public MenuModelClass(String carname, int carimage) {
        this.carname = carname;
        this.carimage = carimage;
    }
}
