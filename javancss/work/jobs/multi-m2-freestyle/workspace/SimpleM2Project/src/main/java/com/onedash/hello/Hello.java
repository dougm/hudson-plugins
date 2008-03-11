package com.onedash.hello;

/**
 * A basic hello world application.
 *
 * @author Stephen Connolly
 */
public class Hello {
    public String sayHello(String name) {
        return "Hello " + name;
    }

    public static void main(String[] args) {
        Hello instance = new Hello();
        if (args.length == 1) {
            System.out.println(instance.sayHello(args[0]));
        } else {
            System.out.println(instance.sayHello("world"));
        }
    }
}
