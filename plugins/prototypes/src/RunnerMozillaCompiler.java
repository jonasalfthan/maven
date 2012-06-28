import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

public class RunnerMozillaCompiler {
    public static void main(String... args) {
        Context cx = Context.enter();
        Scriptable scope = cx.initStandardObjects();
        test1 p = new test1();//test1 instanceof org.mozilla.javascript.Script
        p.exec(cx, scope);
        String f = p.getParamOrVarName(0);
        Object obj = p.get("myFun", p);
        System.out.println("obj = " + obj);
        System.out.println(cx.decompileScript(p, 0));
    }
}
