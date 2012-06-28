
public class RunnerMozillaExtendsMissingFunctionString {
    public static void main(String... args) {
        test8 p = new test8();
        String r = p.myFun();
        System.out.printf("JavaScript function does not return. Java returns (java.lang.String) \"undefined\" >> %s", r);
    }
}
