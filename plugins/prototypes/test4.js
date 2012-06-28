function myAbstractCall(i) {
   java.lang.System.out.println('calling JavaScript function "myAbstractCall"...');
   java.lang.System.out.printf("observed key code in JavaScript %.0f\n", i);
   var r = i + 5;
   java.lang.System.out.printf("JavaScript sends modified key code %.0f back to Java\n", r);
   return r;
}