package analyses;

import java.awt.geom.Point2D;

import java.util.List;
import abstractModel.OV;
import java.util.ArrayList;
import java.util.Collections;


/**
 * Fundamental Diagram
 *
 * @author tadaki
 */
public class Fundamental {

    final private double circuitLength;
    private final int tRelax;//time steps for relaxation
    private final OV ov;
    private final int tstep;
    private final double dt;//time step in Runge-Kutta

    public Fundamental(int tRelex, OV ov, double dt, int tstep) {
        System.out.println(tRelex);
        this.tRelax = tRelex;
        circuitLength = ov.getCircuitLength();
        this.ov = ov;
        this.dt = dt;
        this.tstep = tstep;
    }

    /**
     * Generating fundamental diagram 
     * 
     * Changing the number of cars from numCarFrom to numCarTo
     * 
     * increasing steps of the number of cars
     * 
     * Averages are taken over numRepeat runs
     * 
     * @param numCarFrom
     * @param numCarTo
     * @param numCarStep
     * @param numRepeat 
     * @return
     */
    public List<Point2D.Double> doExec(int numCarFrom, int numCarTo,
            int numCarStep, int numRepeat) {
        int num = numCarFrom;
        List<Point2D.Double> data = Collections.synchronizedList(new ArrayList<>());
        while (num < numCarTo) {
            ov.changeNumCars(num);
            ov.ovInit();
            for (int i = 0; i < tRelax; i++) {
                ov.updateState(dt, tstep);
            }
            List<Double> speedList = ov.getSpeedList();
            double v = 0;
            for (int i = 0; i < numRepeat; i++) {
                v = speedList.stream().reduce(
                        v, (a, _item) -> a + _item);
                ov.updateState(dt, tstep);
            }
            v /= num * numRepeat;
            double density = (double) num / circuitLength;
            data.add(new Point2D.Double(density, density * v));
            num += numCarStep;
        }
        return data;
    }

}
