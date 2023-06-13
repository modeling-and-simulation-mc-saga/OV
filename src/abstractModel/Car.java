package abstractModel;

/**
 * Car class storeing position and speed
 *
 * @author tadaki
 */
public class Car {

    protected double x;//position
    protected double xPrevious; //position at the previous time
    protected double v;//speed
    protected double vPrevious; //speed at the previous time
    protected double dx;//headway distance

    public Car() {
    }

    public void setValues(double xi, double vi) {
        x = xi;
        v = vi;
        xPrevious = x;
        vPrevious = v;
    }

    /**
     * storing current values
     */
    public void saveValues() {
        xPrevious = x;
        vPrevious = v;
    }

    //***** getters and setters
    
    public void setX(double x) {
        this.x = x;
    }

    public void setV(double v) {
        this.v = v;
    }

    public void setDx(double dx) {
        this.dx = dx;
    }

    public double getPosition() {
        return x;
    }

    public double getSpeed() {
        return v;
    }

    public double getPreviousPosition() {
        return xPrevious;
    }

    public double getPreviousSpeed() {
        return vPrevious;
    }

    public double getHeadway() {
        return dx;
    }

}
