package rungeKutta;

/**
 * 4次のRunge Kutta法
 *
 * @author tadaki
 */
public class RungeKutta {

    /**
     * One step from x to x + h
     *
     * @param t initial value of independent variable
     * @param y initial values of dependent variables
     * @param h change in independent variable
     * @param eq differential equations
     * @return dependent variables at t + h
     */
    public static double[] rk4(
            double t, double y[], double h, DifferentialEquations eq) {
        int n = y.length;
        double hh = h / 2.;
        double h6 = h / 6.;
        double k1[] = eq.rhs(t, y);
        double xh = t + hh;
        double yt[] = new double[n];
        for (int i = 0; i < n; i++) {  yt[i] = y[i] + hh * k1[i];  }
        double k2[] = eq.rhs(xh, yt);
        for (int i = 0; i < n; i++) {  yt[i] = y[i] + hh * k2[i];  }
        double k3[] = eq.rhs(xh, yt);
        for (int i = 0; i < n; i++) {  yt[i] = y[i] + h * k3[i]; }
        double k4[] = eq.rhs(t + h, yt);
        double yy[] = new double[n];
        for (int i = 0; i < n; i++) {
            yy[i] = y[i] + h6 * (k1[i] + 2. * k2[i] + 2. * k3[i] + k4[i]);
        }
        return yy;
    }

    /**
     * solve by rk4 from x1 to x2 with nstep
     *
     * @param vstart start values of dependent valiables
     * @param t1 initial value of independent valiable
     * @param t2 final value of independent valiable
     * @param nstep the number of steps between t1 and t3
     * @param eq Differential equation
     * @return dependent variables at t2
     */
    public static double[][] rkdumb(
            double vstart[], double t1, double t2, int nstep,
            DifferentialEquations eq) {
        int n = vstart.length;
        double y[][] = new double[n][nstep];
        double v[] = new double[n];
        for (int i = 0; i < n; i++) {
            v[i] = vstart[i];  y[i][0] = v[i];
        }

        double t = t1;
        double h = (t2 - t1) / nstep;
        if ((t + h) == t) {
            System.err.println("too small step");  return y;
        }
        for (int k = 1; k < nstep; k++) {
            double vout[] = rk4(t, v, h, eq);
            t += h;
            for (int i = 0; i < n; i++) {
                v[i] = vout[i];  y[i][k] = v[i];
            }
        }
        return y;
    }
    
    private RungeKutta(){}
}
