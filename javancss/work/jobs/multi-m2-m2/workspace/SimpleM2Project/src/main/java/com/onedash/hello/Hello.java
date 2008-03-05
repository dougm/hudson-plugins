package com.onedash.hello;

/**
 * A basic hello world application.
 *
 * @author Stephen Connolly
 */
public class Hello {
    /**
     * Says hello.
     *
     * @param name the name of the person we're saying hello to.
     */
    public String sayHello(String name) {
        // just return the hello
        return "Hello " + name;
    }

    public static void main(String[] args) {
        // create the instance to use
        Hello instance = new Hello();
        if (args.length == 1) {
            /*
             * If provided with a name, use it
             */

            System.out.println(instance.sayHello(args[0]));
        } else {

            // If no name use world
            System.out.println(instance.sayHello("world"));
        }
    }
}
