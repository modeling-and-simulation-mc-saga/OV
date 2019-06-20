package analyses;

import abstractModel.Car;
import abstractModel.OV;
import java.awt.geom.Point2D;
import java.util.List;
import myLib.Utils;

/**
 * headway-velocity
 *
 * @author tadaki
 */
public class HV {

    private final OV ov;

    public HV(OV ov) {
        this.ov = ov;
    }

    /**
     * 一台の車両の軌跡を追う
     *
     * @param dt
     * @param tstep
     * @return
     */
    public List<Point2D.Double> doExec(double dt,int tstep) {
        List<Point2D.Double> plist = Utils.createList();
        for (int i = 0; i < 10000; i++) {
            ov.updateState(dt,tstep);
            Car car = ov.getCars(0);
            plist.add(new Point2D.Double(car.readHeadway(), car.readSpeed()));
        }
        return plist;
    }

}
