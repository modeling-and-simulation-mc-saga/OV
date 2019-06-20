package abstractModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.DoubleFunction;
import rungeKutta.DifferentialEquations;
import rungeKutta.RungeKutta;

/**
 * 最適速度交通流モデル
 *
 * @author tadaki
 */
public class OV {

    final private double circuitLength;//コース長
    private int numCars;//車両数
    private List<Car> cars;//車両リスト
    private double t;//時刻
    private DifferentialEquations equation;//微分方程式
    protected double alpha;//感受率
    protected DoubleFunction<Double> ovFunction;//最適速度関数

    /**
     * circuitの長さを指定して初期化
     *
     * @param circuitLength
     * @param alpha
     * @param ovfunction
     */
    public OV(double circuitLength, double alpha,
            DoubleFunction<Double> ovfunction) {
        this.circuitLength = circuitLength;
        this.alpha = alpha;
        this.ovFunction = ovfunction;
    }

    /**
     * 初期状態生成
     */
    public void ovInit() {
        cars = Collections.synchronizedList(new ArrayList<>());
        double dr = circuitLength / numCars;//初期の車頭距離
        for (int i = 0; i < numCars; i++) {
            Car car = new Car();
            car.setValues(i * dr, 0.);
            cars.add(car);
        }
        //一台だけ位置をずらす
        int ii = (int) (0.4 * numCars);
        if (ii >= 0 && ii < cars.size()) {
            cars.get(ii).setValues(ii * dr - 0.2 * dr, 0.);//摂動
        }

        t = 0.;
        //微分方程式を構成
        //偶数番要素は位置、奇数番要素は速度
        equation = (double td, double y[]) -> {
            double dy[] = new double[2 * numCars];
            for (int i = 0; i < numCars; i++) {
                int j = (i + 1) % numCars;
                double headway = y[2 * j] - y[2 * i];
                headway = (headway + circuitLength) % circuitLength;
                dy[2 * i + 1] = alpha * (ovFunction.apply(headway) - y[2 * i + 1]);
                dy[2 * i] = y[2 * i + 1];
            }
            return dy;
        };
    }

    /**
     * 状態更新 : dtをtstepに分割して積分
     *
     * @param dt
     * @param tstep
     */
    public void updateState(double dt, int tstep) {
        //RungeKutta法の利用
        double y[] = new double[2 * numCars];
        for (int i = 0; i < numCars; i++) {
            y[2 * i] = cars.get(i).readPosition() % circuitLength;
            y[2 * i + 1] = cars.get(i).readSpeed();
        }
        double yy[][] = RungeKutta.rkdumb(y, 0, dt, tstep, equation);

        //各車両の位置及び速度として保存
        cars.stream().forEach(c -> c.saveValues());

        for (int i = 0; i < numCars; i++) {
            cars.get(i).setX(yy[2 * i][tstep - 1] % circuitLength);
            cars.get(i).setV(yy[2 * i + 1][tstep - 1]);
            int j = (i + 1) % numCars;
            double headway = yy[2 * j][tstep - 1] - yy[2 * i][tstep - 1];
            headway = (headway + circuitLength) % circuitLength;
            cars.get(i).setDx(headway);
        }

        t += dt;
    }

    public List<Double> getSpeedList() {
        List<Double> list = Collections.synchronizedList(new ArrayList<>());
        cars.stream().forEach((c) -> {
            boolean add = list.add(c.readSpeed());
        });
        return list;
    }

    /**
     * 車両数を変更
     *
     * @param numCars
     */
    public void changeNumCars(int numCars) {
        this.numCars = numCars;
        cars = null;
        ovInit();
    }

    public int getNumCars() {
        return numCars;
    }

    public double getCircuitLength() {
        return circuitLength;
    }

    public Car getCars(int i) {
        return cars.get(i);
    }

    public DoubleFunction<Double> getOvFunction() {
        return ovFunction;
    }

    public double getT() {
        return t;
    }

}
