
public class RunnerMozillaExtendsMissingFunctionDouble {
    public static void main(String... args) {
        test6 p = new test6();//test6 instanceof AbstractSuperRhino
        double r = p.myAbstractCall(0);
        System.out.printf("This call does not exist: null implementation and returns NaN >> %f", r);
    }
}
