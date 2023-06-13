package simulations;

import abstractModel.OV;
import analyses.Fundamental;
import analyses.HV;
import java.awt.geom.Point2D;
import java.io.PrintStream;
import java.io.IOException;
import java.util.List;
import java.util.function.DoubleFunction;

/**
 * Simulation using OV model
 *
 * @author tadaki
 */
public class Simulation {

    protected int circuitLength = 1000;
    protected int tmax = 1000;
    protected double alpha = 1.;
    protected final DoubleFunction<Double> ovFunction;
    protected final OV ov;
    protected double dt = 0.1;
    protected int tstep = 100;

    public Simulation(DoubleFunction<Double> ovfunction,
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
        try (PrintStream out = new PrintStream(filename)) {
            for (int t = 0; t < tmax; t++) {
                for (int i = 0; i < ov.getNumCars(); i++) {
                    double x = ov.getCar(i).getPosition();
                    out.println(x+" "+t);
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
        try (PrintStream out = new PrintStream(filename)) {
            data.forEach(p->out.println(p.x+" "+p.y));
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
        relax(tmax);
        List<Point2D.Double> plist = hv.doExec(dt, tstep);
        try (PrintStream out = new PrintStream(filename)) {
            plist.forEach(p->out.println(p.x+" "+p.y));
        }
    }
}
