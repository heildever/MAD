package com.example.mad.lab2;

/**
 * Created by daniel on 01/04/17.
 */


public class members_class {
    public int icon;
    public String title;
    public String noti;

    //Constructor => in case we dont receive anything
    public members_class(){
        super();
    }

    //Constructor => in case we receive elements
    public members_class(int icon, String title, String noti){
        super();
        this.icon = icon;
        this.title = title;
        this.noti = noti;

    }
}

///////////////////////////////
//Elements will be of this class. Data type: members_class
