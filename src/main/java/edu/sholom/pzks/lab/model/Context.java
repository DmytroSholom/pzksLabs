package edu.sholom.pzks.lab.model;

/**
 * Created by Dmytro on 31.05.2015.
 */
public class Context {
    private final static Context instance = new Context();

    private AutoGraphParams autoGraphParams;

    private Context(){
    }

    public static Context getInstance(){
        return instance;
    }
}
