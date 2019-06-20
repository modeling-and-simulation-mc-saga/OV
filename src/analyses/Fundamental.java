package analyses;

import java.awt.geom.Point2D;

import java.util.List;
import abstractModel.OV;

import myLib.Utils;

/**
 *
 * @author tadaki
 */
public class Fundamental {

    final private double length;
    private final int tLength;
    private final OV ov;
    private final int tstep;
    private final double dt;

    public Fundamental(int tLength, OV ov, double dt, int tstep) {
        System.out.println(tLength);
        this.tLength = tLength;
        length = ov.getCircuitLength();
        this.ov = ov;
        this.dt = dt;
        this.tstep = tstep;
    }

    public List<Point2D.Double> doExec(int numCarFrom, int numCarTo,
            int numCarStep, int numRepeat) {
        int num = numCarFrom;
        List<Point2D.Double> data = Utils.createList();
        while (num < numCarTo) {
            ov.changeNumCars(num);
            ov.ovInit();
            for (int i = 0; i < tLength; i++) {
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
            double density = (double) num / length;
            data.add(new Point2D.Double(density, density * v));
            num += numCarStep;
        }
        return data;
    }

}
