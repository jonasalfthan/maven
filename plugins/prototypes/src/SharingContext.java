import org.mozilla.javascript.*;
import org.mozilla.javascript.tools.shell.Global;

import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.*;

public class SharingContext {
    public static void main(String... args) throws IOException, ExecutionException, InterruptedException {
        final Context ctx;
        final Scriptable scope;
        try {
            ctx = Context.enter();
            scope = new Global(ctx);//ImporterTopLevel: importClass importPackages, and setup global properties

            Object out = Context.javaToJS(System.out, scope);
            ScriptableObject.putProperty(scope, "out", out);

            Object result = ctx.evaluateReader(scope, new FileReader("sharingContext1.js"), "sharingContext1", 0, null);
            if (result != Undefined.instance)
                System.out.println("sharingContext1 returned to Java with value " + result);
            result = ctx.evaluateReader(scope, new FileReader("sharingContext2.js"), "sharingContext2", 0, null);
            if (result != Undefined.instance)
                System.out.println("sharingContext2 returned to Java with value " + result);
            Function function = null;
            for (final Object id : scope.getIds()) {
                if (!(id instanceof String)) continue;
                final Object fObj = scope.get((String) id, scope);
                if (fObj instanceof Function) {
                    function = (Function) fObj;
                    if (id.equals("myFun")) {
                        function.call(ctx, scope, scope, new Object[]{"Java Call "});
                        ExecutorService executor = Executors.newSingleThreadExecutor();
                        result = executeAsynchronous(executor, function, scope, "Java Thread Call ").get();
                        if (result != Undefined.instance) System.out.println(result);
                        executor.shutdown();
                    }
                }
            }
            //ctx = Context.enter(); in multithreading
            result = new sharingContext3().call(ctx, scope, scope, /*maybe script properties*/new Object[0]);
            function.call(ctx, scope, scope, new Object[]{"sharingContext3: "});

            //if sharingContext4 is not a Script, then use class sharingContext41
            result = new sharingContext41().call(ctx, scope, scope, /*maybe script properties*/new Object[0]);
            function.call(ctx, scope, scope, new Object[]{"sharingContext4: "});
        } finally {
            Context.exit();
        }
    }

    private static Future executeAsynchronous(ExecutorService executor, Function function,  Scriptable scope, Object... args) {
        final class CurrentCallable implements Callable {
            final Object[] args;
            final Function function;
            final Scriptable scope;

            CurrentCallable(Function function, Scriptable scope, Object... args) {
                this.args = new Object[args.length];
                int i = 0;
                for (Object arg : args)
                    this.args[i++] = arg;
                this.function = function;
                this.scope = scope;
            }

            public Object call() throws Exception {
                try {
                    return function.call(Context.enter(), scope, scope, args);
                } finally {
                    Context.exit();
                }
            }
        }

        return executor.submit(new CurrentCallable(function, scope, args));
    }
}
