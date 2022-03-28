package games.moegirl.sinocraft.sinocore.api.util;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Utils for Supplier
 */
public class Suppliers {

    /**
     * A supplier with decorator
     *
     * @param constructor object constructor
     * @param decorator   decorator
     * @param <T>         type
     * @return supplier
     */
    public static <T> Supplier<T> decorate(Supplier<T> constructor, Consumer<T> decorator) {
        return () -> {
            T value = constructor.get();
            decorator.accept(value);
            return value;
        };
    }

    /**
     * A supplier with decorator
     *
     * @param constructor object constructor
     * @param decorator   decorator
     * @param <T>         type
     * @return supplier
     */
    public static <T> Supplier<T> decorate(Supplier<T> constructor, Function<T, T> decorator) {
        return () -> decorator.apply(constructor.get());
    }

    public static <T, R> Supplier<R> curry(Function<T, R> function, T value) {
        return () -> function.apply(value);
    }
}
