
public class RunnerMozillaExtendsFunctionOverride {
    public static void main(String... args) {
        System.out.println("Run JavaScript content:");
        test10 p = new test10();
        System.out.println("Run JavaScript function which overrides Java method:");
        p.call();
        System.out.println("Run Java method on super call:");
        p.super$call();
    }
}
