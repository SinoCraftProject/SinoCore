package games.moegirl.sinocraft.sinocore.utility;

@Deprecated(forRemoval = true, since = "1.2.0")
public interface IWrapper<O, T> extends ISelf<T> {

    O getOrigin();

    ISelf<? extends T> newWrapper(O object);

    default T reWrapper(O newObject) {
        return getOrigin() == newObject ? self() : newWrapper(newObject).self();
    }
}
