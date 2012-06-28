
public class RunnerMozillaExtendsMissingFunctionObject {
    public static void main(String... args) {
        test7 p = new test7();
        Object r = p.myFun();
        System.out.printf("JavaScript function does not return. Java returns (java.lang.Object) >> %s", r);
    }
}
