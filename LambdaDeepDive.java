import java.util.*;
import java.util.function.*;

public class LambdaDeepDive {

    // ============================================================
    // 1. CUSTOM FUNCTIONAL INTERFACE
    // ============================================================

    @FunctionalInterface
    interface Calculator {
        int calculate(int a, int b);
    }

    // ============================================================
    // 2. LAMBDA BASICS
    // ============================================================

    static void lambdaBasics() {
        Calculator add = (a, b) -> a + b;
        Calculator multiply = (a, b) -> {
            int result = a * b;
            return result;
        };
        System.out.println("Add      : " + add.calculate(10, 20));
        System.out.println("Multiply : " + multiply.calculate(10, 20));
    }
    // ============================================================
    // 3. BUILT-IN FUNCTIONAL INTERFACES
    // ============================================================
    static void functionalInterfaces() {
        // Predicate<T>
        // T -> boolean
        Predicate<Integer> isEven =x -> x % 2 == 0;
        System.out.println("Is 10 even? " + isEven.test(10));
        // Function<T, R>
        // T -> R
        Function<String, Integer> length =str -> str.length();
        System.out.println(
                "Length: " + length.apply("Vishal")
        );
        // Consumer<T>
        // T -> void
        Consumer<String> printer =str -> System.out.println("Consumer: " + str);
        printer.accept("Hello Lambda");
        // Supplier<T>
        // () -> T
        Supplier<Double> randomNumber =() -> Math.random();
        System.out.println( "Random: " + randomNumber.get()
        );
        // UnaryOperator<T>
        // T -> T
        UnaryOperator<Integer> square =x -> x * x;
        System.out.println(  "Square: " + square.apply(5)
        );
        // BinaryOperator<T>
        // (T,T) -> T
        BinaryOperator<Integer> sum = (a, b) -> a + b;
        System.out.println(  "Sum: " + sum.apply(10, 20)
        );
    }
    // ============================================================
    // 4. TARGET TYPING
    // ============================================================
    static void targetTyping() {
        Function<Integer, Integer> doubleValue =x -> x * 2;

        System.out.println(  "Double: " + doubleValue.apply(10));
    }


    // ============================================================
    // 5. EFFECTIVELY FINAL
    // ============================================================

    static void variableCapture() {

        int multiplier = 10;

        /*
         * multiplier is effectively final.
         *
         * Lambda can capture it.
         */

        Function<Integer, Integer> multiply =
                x -> x * multiplier;

        System.out.println(
                "Result: " + multiply.apply(5)
        );


        /*
         * This would NOT compile:
         *
         * multiplier++;
         *
         * because captured local variables must be
         * final or effectively final.
         */
    }


    // ============================================================
    // 6. THIS INSIDE LAMBDA
    // ============================================================

    int value = 100;

    void thisInsideLambda() {
        Runnable task = () -> {
            System.out.println( "this.value = " + this.value
            );
        };

        task.run();
    }
    // ============================================================
    // 7. METHOD REFERENCE
    // ============================================================

    static void methodReference() {

        List<String> names = List.of( "Vishal",  "Rahul",   "Amit" );
        // Lambda
        names.forEach(    name -> System.out.println(name)   );
        // Method reference
        names.forEach(   System.out::println  );
    }


    // ============================================================
    // 8. DIFFERENT METHOD REFERENCE TYPES
    // ============================================================
    static void methodReferenceTypes() {
        // --------------------------------------------------------
        // STATIC METHOD REFERENCE
        // --------------------------------------------------------
        Function<String, Integer> parse =  Integer::parseInt;
        System.out.println(
                parse.apply("100")
        );
        // Equivalent lambda:
        Function<String, Integer> parse2 =  s -> Integer.parseInt(s);
        // --------------------------------------------------------
        // INSTANCE METHOD OF PARTICULAR OBJECT
        // -------------------------------------------------------

        String prefix = "Hello ";

        Function<String, String> addPrefix = prefix::concat;

        System.out.println(
                addPrefix.apply("Vishal")
        );
        // --------------------------------------------------------
        // INSTANCE METHOD OF ARBITRARY OBJECT
        // --------------------------------------------------------
        Function<String, String> upper =  String::toUpperCase;

        System.out.println(upper.apply("hello")
        );
        // Equivalent:
        Function<String, String> upper2 =   s -> s.toUpperCase();
        // --------------------------------------------------------
        // CONSTRUCTOR REFERENCE
        // --------------------------------------------------------
        Supplier<ArrayList<String>> listCreator =  ArrayList::new;

        ArrayList<String> list = listCreator.get();
        list.add("Java");
        System.out.println(list);
    }
    // ============================================================
    // 9. PREDICATE COMPOSITION
    // ============================================================
    static void predicateComposition() {
        Predicate<Integer> positive =  x -> x > 0;

        Predicate<Integer> even = x -> x % 2 == 0;
        Predicate<Integer> positiveEven =
        positive.and(even);
        System.out.println(   positiveEven.test(10)
        );
        System.out.println(   positiveEven.test(-10)
        );
        Predicate<Integer> positiveOrEven =
                positive.or(even);

        System.out.println(
                positiveOrEven.test(-10)
        );

        Predicate<Integer> odd =
                even.negate();

        System.out.println(
                odd.test(5)
        );
    }


    // ============================================================
    // 10. FUNCTION COMPOSITION
    // ============================================================

    static void functionComposition() {

        Function<Integer, Integer> multiplyByTwo =
                x -> x * 2;

        Function<Integer, Integer> addTen =
                x -> x + 10;


        /*
         * andThen:
         *
         * first multiplyByTwo
         * then addTen
         */

        Function<Integer, Integer> pipeline =
                multiplyByTwo.andThen(addTen);


        /*
         * 5
         * ↓
         * 5 * 2 = 10
         * ↓
         * 10 + 10 = 20
         */

        System.out.println(
                pipeline.apply(5)
        );


        /*
         * compose:
         *
         * addTen first
         * multiplyByTwo second
         */

        Function<Integer, Integer> reversePipeline =
                multiplyByTwo.compose(addTen);


        /*
         * 5
         * ↓
         * 5 + 10 = 15
         * ↓
         * 15 * 2 = 30
         */

        System.out.println(
                reversePipeline.apply(5)
        );
    }


    // ============================================================
    // 11. STREAM + LAMBDA
    // ============================================================

    static void streamsWithLambda() {

        List<Integer> numbers =
                List.of(1, 2, 3, 4, 5, 6);


        /*
         * filter()
         *
         * expects Predicate<T>
         *
         * T -> boolean
         */

        List<Integer> evenNumbers =
                numbers.stream()
                        .filter(x -> x % 2 == 0)
                        .toList();


        System.out.println(
                "Even: " + evenNumbers
        );


        /*
         * map()
         *
         * expects Function<T,R>
         *
         * T -> R
         */

        List<Integer> doubled =
                numbers.stream()
                        .map(x -> x * 2)
                        .toList();


        System.out.println(
                "Doubled: " + doubled
        );


        /*
         * forEach()
         *
         * expects Consumer<T>
         */

        numbers.stream()
                .forEach(x ->
                        System.out.println(
                                "Number: " + x
                        )
                );
    }


    // ============================================================
    // 12. LAMBDA + STREAM PIPELINE
    // ============================================================

    static void completePipeline() {

        List<String> names = List.of(
                "Vishal",
                "Amit",
                "Rahul",
                "Vikas",
                "Ankit"
        );


        List<String> result =
                names.stream()

                        // Keep names starting with V
                        .filter(name ->
                                name.startsWith("V"))

                        // Convert to uppercase
                        .map(name ->
                                name.toUpperCase())

                        // Sort
                        .sorted()

                        // Collect
                        .toList();


        System.out.println(result);
    }


    // ============================================================
    // 13. PRIMITIVE SPECIALIZATION
    // ============================================================

    static void primitiveSpecialization() {

        /*
         * Function<Integer,Integer>
         *
         * Can involve boxing/unboxing.
         */

        Function<Integer, Integer> normal =
                x -> x * x;


        /*
         * IntUnaryOperator works directly with int.
         */

        IntUnaryOperator primitive =
                x -> x * x;


        System.out.println(
                normal.apply(10)
        );

        System.out.println(
                primitive.applyAsInt(10)
        );
    }


    // ============================================================
    // 14. LAMBDA WITH THREAD
    // ============================================================

    static void lambdaWithThread() {

        Runnable task = () -> {

            System.out.println(
                    "Running in: " +
                    Thread.currentThread().getName()
            );
        };


        Thread thread =
                new Thread(task);

        thread.start();
    }


    // ============================================================
    // 15. LAMBDA + COMPLETABLE FUTURE
    // ============================================================
    static void lambdaWithFuture() {
    }
    // ============================================================
    // 16. LAMBDA VS ANONYMOUS CLASS
    // ============================================================

    static void lambdaVsAnonymousClass() {

        // Anonymous class

        Runnable anonymous =
                new Runnable() {

                    @Override
                    public void run() {

                        System.out.println(
                                "Anonymous class"
                        );
                    }
                };


        // Lambda

        Runnable lambda =
                () -> System.out.println(
                        "Lambda"
                );


        anonymous.run();
        lambda.run();


        /*
         * Important:
         *
         * Lambda is NOT simply an anonymous class.
         *
         * Modern Java generally uses invokedynamic
         * for lambda implementation.
         */
    }


    // ============================================================
    // 17. INVOKEDYNAMIC — JVM LEVEL
    // ============================================================

    static void invokedynamicConcept() {

        Runnable r =
                () -> System.out.println(
                        "Hello JVM"
                );


        r.run();
    }


    // ============================================================
    // 18. CUSTOM HIGHER-ORDER METHOD
    // ============================================================

    static int operate(
            int a,  int b,  Calculator calculator) {

        return calculator.calculate(a, b);
    }
    static void higherOrderFunction() {
        int addition =  operate(   10, 20,
                        (a, b) -> a + b
                );

        int multiplication =
                operate(    10,
                        20,
                        (a, b) -> a * b
                );


        System.out.println(
                "Addition: " + addition
        );

        System.out.println(
                "Multiplication: " + multiplication
        );
    }


    // ============================================================
    // MAIN
    // ============================================================

    public static void main(String[] args) {

        System.out.println(
                "\n===== LAMBDA BASICS ====="
        );
        lambdaBasics();


        System.out.println(
                "\n===== FUNCTIONAL INTERFACES ====="
        );
        functionalInterfaces();


        System.out.println(
                "\n===== TARGET TYPING ====="
        );
        targetTyping();


        System.out.println(
                "\n===== VARIABLE CAPTURE ====="
        );
        variableCapture();


        System.out.println(
                "\n===== THIS INSIDE LAMBDA ====="
        );

        LambdaDeepDive obj =
                new LambdaDeepDive();

        obj.thisInsideLambda();


        System.out.println(
                "\n===== METHOD REFERENCE ====="
        );
        methodReference();


        System.out.println(
                "\n===== METHOD REFERENCE TYPES ====="
        );
        methodReferenceTypes();


        System.out.println(
                "\n===== PREDICATE COMPOSITION ====="
        );
        predicateComposition();


        System.out.println(
                "\n===== FUNCTION COMPOSITION ====="
        );
        functionComposition();


        System.out.println(
                "\n===== STREAMS ====="
        );
        streamsWithLambda();


        System.out.println(
                "\n===== COMPLETE STREAM PIPELINE ====="
        );
        completePipeline();


        System.out.println(
                "\n===== PRIMITIVE SPECIALIZATION ====="
        );
        primitiveSpecialization();


        System.out.println(
                "\n===== THREAD ====="
        );
        lambdaWithThread();


        System.out.println(
                "\n===== ANONYMOUS CLASS VS LAMBDA ====="
        );
        lambdaVsAnonymousClass();


        System.out.println(
                "\n===== INVOKEDYNAMIC ====="
        );
        invokedynamicConcept();


        System.out.println(
                "\n===== HIGHER ORDER FUNCTION ====="
        );
        higherOrderFunction();
    }
}