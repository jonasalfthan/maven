out.println('Started...');
runCommand("cmd", "/C", "date /T");
var x = 5;
java.lang.System.out.println("printed by sharingContext1: " + x);
myFun('sharingContext1');
x;

function myFun(s) {
   java.lang.System.out.print(s);
   java.lang.System.out.print('#myFun: holding x = ');
   java.lang.System.out.println(x);
}