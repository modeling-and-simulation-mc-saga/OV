package models;

import java.io.IOException;
import java.util.function.DoubleFunction;

/**
 *
 * @author tadaki
 */
public class Tanh  {

    public static double tanh(double y) {
        if (y > 0) {
            return (1. - Math.exp(-2 * y)) / (1. + Math.exp(-2 * y));
        } else {
            return (Math.exp(2 * y) - 1.) / (1. + Math.exp(2 * y));
        }
    }

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String args[]) throws IOException {
        int length = 1000;
        int tmax = 1000;
        //高速道路対応
        double vmax = 33.6;
        final double d = 25.;
        final double w = 23.3;
        final double c = 0.913;
        final double alpha = 2.;
        int numCar = 40;
        DoubleFunction<Double> ovfunction
                = x -> 0.5*vmax * (tanh(2. * (x - d) / w) + c);
        Simulation sys = new Simulation(ovfunction, length, numCar, alpha);
        sys.relax(10*tmax);
        sys.spacetime("Tanh-spacetime.txt");
        sys.hv("Tanh-hv.txt");
        tmax*=10;
        sys.tmax=tmax;
//        sys.fundamental(Tanh.class.getSimpleName()+"-fundamental.txt", 5, 100, 5, 10);
    }

}
