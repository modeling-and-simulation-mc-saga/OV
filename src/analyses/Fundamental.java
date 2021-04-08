package analyses;

import java.awt.geom.Point2D;

import java.util.List;
import abstractModel.OV;

import myLib.utils.Utils;

/**
 * Fundamental Diagram
 *
 * @author tadaki
 */
public class Fundamental {

    final private double circuitLength;
    private final int tRelax;//緩和のためのstep数
    private final OV ov;
    private final int tstep;
    private final double dt;//RUnge-Kuttaの時間幅

    public Fundamental(int tRelex, OV ov, double dt, int tstep) {
        System.out.println(tRelex);
        this.tRelax = tRelex;
        circuitLength = ov.getCircuitLength();
        this.ov = ov;
        this.dt = dt;
        this.tstep = tstep;
    }

    /**
     * 実際に基本図を作る 
     * 
     * 車両数をnumCarFromからnumCarToまで、numCarStep毎に変化
     * 
     * 各車両数に対してnumRepeat回の観測を平均
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
        List<Point2D.Double> data = Utils.createList();
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
