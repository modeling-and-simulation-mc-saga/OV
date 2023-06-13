package simulations;

import java.io.IOException;
import java.util.function.DoubleFunction;

/**
 *
 * @author tadaki
 */
public class Step {

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String args[]) throws IOException {
        int length = 1000;
        int tmax = 10000;
        double alpha = 1.;
        double vmax = 10.;
        double d = 10.;
        int numCar = 100;
        DoubleFunction<Double> ovfunction
                = x -> {
                    double v = 0.;
                    if (x > d) {
                        v = vmax;
                    }
                    return v;
                };
        Simulation sys = new Simulation(ovfunction, length, numCar, alpha);
        sys.relax(10 * tmax);
        sys.spacetime("Step-spacetime.txt");
        sys.hv("Step-hv.txt");
//        sys.fundamental("Step-fundamental.txt", 10, 190, 10, 100);
    }

}
