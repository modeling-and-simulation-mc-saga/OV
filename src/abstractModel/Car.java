package abstractModel;

/**
 * ある時間間隔で位置と速度を保存するためのクラス
 *
 * @author tadaki
 */
public class Car {

    protected double x;
    protected double xPrevious; //位置
    protected double v;
    protected double vPrevious; //速度
    protected double dx;//車頭距離

    public Car() {
    }

    public void setValues(double xi, double vi) {
        x = xi;
        v = vi;
        xPrevious = x;
        vPrevious = v;
    }

    /**
     * 現在の値を保存する
     */
    public void saveValues() {
        xPrevious = x;
        vPrevious = v;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setV(double v) {
        this.v = v;
    }

    public void setDx(double dx) {
        this.dx = dx;
    }

    public double readPosition() {
        return x;
    }

    public double readSpeed() {
        return v;
    }

    public double readPreviousPosition() {
        return xPrevious;
    }

    public double readPreviousSpeed() {
        return vPrevious;
    }

    public double readHeadway() {
        return dx;
    }

}
