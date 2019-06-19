package models;

import abstractModel.OV;
import analyses.Fundamental;
import analyses.HV;
import java.awt.geom.Point2D;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;
import java.util.function.DoubleFunction;
import myLib.FileIO;

/**
 *
 * @author tadaki
 */
public class Simulation {

    protected int length = 1000;
    protected int tmax = 1000;
    protected double alpha = 1.;
    protected final DoubleFunction ovfunction;
    protected final OV ov;
    protected double dt = 0.1;
    protected int tstep = 100;

    public Simulation(DoubleFunction ovfunction, 
            int length, int numCar, double alpha) {
        this.length = length;
        this.alpha = alpha;
        this.ovfunction = ovfunction;
        ov = new OV(length, alpha, ovfunction);
        ov.changeNumCar(numCar);
        for (int t = 0; t < 2 * tmax; t++) {
            ov.updateState(dt, tstep);
        }
    }

    public void setNumCar(int numCar){
        ov.changeNumCar(numCar);
    }
    
    public void relax(int tt){
        for (int t = 0; t < tt; t++) {
            ov.updateState(dt, tstep);
        }
        
    }
    public void spacetime(String filename) throws IOException {
        try (BufferedWriter out = FileIO.openWriter(filename)) {
            for (int t = 0; t < tmax; t++) {
                for (int i = 0; i < ov.getNumCar(); i++) {
                    double x = ov.getCar(i).readPosition();
                    FileIO.writeSSV(out, x, t);
                }
                ov.updateState(dt, tstep);
            }
        }

    }

    public void fundamental(String filename,
            int from, int to, int step, int repeat) throws IOException {
        Fundamental sys = new Fundamental(tmax, ov, dt, tstep);

        List<Point2D.Double> data = sys.doExec(from, to, step, repeat);
        try (BufferedWriter out = FileIO.openWriter(filename)) {
            for (Point2D.Double p : data) {
                FileIO.writeSSV(out, p.x, p.y);
            }
        }
    }

    public void hv(String filename) throws IOException {
        HV hv = new HV(ov);
        //緩和
        relax(tmax);
        List<Point2D.Double> plist = hv.doExec(dt, tstep);
        try (BufferedWriter out = FileIO.openWriter(filename)) {
            for (Point2D.Double p : plist) {
                FileIO.writeSSV(out, p.x, p.y);
            }
        }
    }
}
