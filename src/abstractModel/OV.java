package abstractModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.DoubleFunction;
import rungeKutta.DifferentialEquations;
import rungeKutta.RungeKutta;

/**
 * Optimal Velocity traffic flow model
 *
 * @author tadaki
 */
public class OV {

    final private double circuitLength;//Circuit length
    private int numCars;//the number of cars
    private List<Car> cars;//car list
    private double t;//time
    private DifferentialEquations equation;
    protected double alpha;//susceptibility
    protected DoubleFunction<Double> ovFunction;//OV function

    /**
     * Initialize by specifying circuit length
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
     * initialize car configuration
     */
    public void ovInit() {
        cars = Collections.synchronizedList(new ArrayList<>());
        //placing cars with equal headway distance
        double dr = circuitLength / numCars;
        for (int i = 0; i < numCars; i++) {
            Car car = new Car();
            car.setValues(i * dr, 0.);
            cars.add(car);
        }
        //only one car changes position slightly
        int ii = (int) (0.4 * numCars);
        if (ii >= 0 && ii < cars.size()) {
            cars.get(ii).setValues(ii * dr - 0.2 * dr, 0.);
        }

        t = 0.;
        //Differential equations
        //even: position, odd:speed
        equation = (double td, double y[]) -> {
            double dy[] = new double[2 * numCars];
            for (int i = 0; i < numCars; i++) {
                int j = (i + 1) % numCars;
                double headway = y[2 * j] - y[2 * i];
                headway = (headway + circuitLength) % circuitLength;
                dy[2 * i + 1] = alpha * (
                        ovFunction.apply(headway) - y[2 * i + 1]);
                dy[2 * i] = y[2 * i + 1];
            }
            return dy;
        };
    }

    /**
     * integrating differential equation
     * 
     * duration dt devided by tstep
     *
     * @param dt
     * @param tstep
     */
    public void updateState(double dt, int tstep) {
        double y[] = new double[2 * numCars];
        for (int i = 0; i < numCars; i++) {
            y[2 * i] = cars.get(i).getPosition() % circuitLength;
            y[2 * i + 1] = cars.get(i).getSpeed();
        }
        double yy[][] = RungeKutta.rkdumb(y, 0, dt, tstep, equation);

        //storing position and speed
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
        cars.forEach(c -> list.add(c.getSpeed()));
        return list;
    }

    /**
     * changing the number of cars
     *
     * @param numCars
     */
    public void changeNumCars(int numCars) {
        this.numCars = numCars;
        cars = null;
        ovInit();
    }

    //***** getters
    
    public int getNumCars() {
        return numCars;
    }

    public double getCircuitLength() {
        return circuitLength;
    }

    public Car getCar(int i) {
        return cars.get(i);
    }

    public DoubleFunction<Double> getOvFunction() {
        return ovFunction;
    }

    public double getT() {
        return t;
    }

}
