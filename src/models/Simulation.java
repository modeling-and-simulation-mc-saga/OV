package models;

import abstractModel.OV;
import analyses.Fundamental;
import analyses.HV;
import java.awt.geom.Point2D;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;
import java.util.function.DoubleFunction;
import myLib.utils.FileIO;

/**
 * OV モデルのシミュレーション
 *
 * @author tadaki
 */
public class Simulation {

    protected int circuitLength = 1000;
    protected int tmax = 1000;
    protected double alpha = 1.;
    protected final DoubleFunction ovFunction;
    protected final OV ov;
    protected double dt = 0.1;
    protected int tstep = 100;

    public Simulation(DoubleFunction ovfunction,
            int circuitLength, int numCars, double alpha) {
        this.circuitLength = circuitLength;
        this.alpha = alpha;
        this.ovFunction = ovfunction;
        ov = new OV(circuitLength, alpha, ovfunction);
        ov.changeNumCars(numCars);
        for (int t = 0; t < 2 * tmax; t++) {
            ov.updateState(dt, tstep);
        }
    }

    public void setNumCars(int numCars) {
        ov.changeNumCars(numCars);
    }

    public void relax(int tt) {
        for (int t = 0; t < tt; t++) {
            ov.updateState(dt, tstep);
        }

    }

    /**
     * output spacetime diagram to a file
     *
     * @param filename
     * @throws IOException
     */
    public void spacetime(String filename) throws IOException {
        try (BufferedWriter out = FileIO.openWriter(filename)) {
            for (int t = 0; t < tmax; t++) {
                for (int i = 0; i < ov.getNumCars(); i++) {
                    double x = ov.getCars(i).readPosition();
                    FileIO.writeSSV(out, x, t);
                }
                ov.updateState(dt, tstep);
            }
        }

    }

    /**
     * output fundamental diagram to a file
     *
     * @param filename
     * @param numCarsFrom
     * @param numCarsTo
     * @param numCarsChangeStep
     * @param numRepeat
     * @throws IOException
     */
    public void fundamental(String filename,
            int numCarsFrom, int numCarsTo, int numCarsChangeStep, int numRepeat)
            throws IOException {
        Fundamental sys = new Fundamental(tmax, ov, dt, tstep);

        List<Point2D.Double> data = sys.doExec(numCarsFrom, numCarsTo,
                numCarsChangeStep, numRepeat);
        try (BufferedWriter out = FileIO.openWriter(filename)) {
            for (Point2D.Double p : data) {
                FileIO.writeSSV(out, p.x, p.y);
            }
        }
    }

    /**
     * output hv diagram to a file
     *
     * @param filename
     * @throws IOException
     */
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
