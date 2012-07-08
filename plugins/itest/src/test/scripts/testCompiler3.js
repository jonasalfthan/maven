print('launched testCompiler3.js');
run();
var a = call();
print('observed \"' + a + '\"');
abstractMethod();

function f1() {
    print('f1')
}

function f2() {
    print('f2')
}

function abstractMethod() {
    print('abstractMethod() implemented by JavaScript');
}

function run() {
    print('run function...')
}

function call() {
    print('call function...')
    'this is my returned value';
}