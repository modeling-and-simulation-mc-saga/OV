package rungeKutta;

/**
 * Interface for describing a set of differential equations
 *
 * @author tadaki
 */
@FunctionalInterface
public interface DifferentialEquations {

    /**
     *
     * @param t independent variable
     * @param y dependent variables
     * @return RHSs of differential equations
     */
    public double[] rhs(double t, double y[]);

}
