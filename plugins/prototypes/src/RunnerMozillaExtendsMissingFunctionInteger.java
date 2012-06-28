
public class RunnerMozillaExtendsMissingFunctionInteger {
    public static void main(String... args) {
        test9 p = new test9();
        Integer r = p.myFun();
        System.out.printf("JavaScript function does not return. Java returns (java.lang.Integer) null >> %s", r);
    }
}
