package by.grsu.eventlink.util.helper;

import java.util.function.Supplier;

public class Conditional {

    private final Boolean value;

    public Conditional(Boolean value) {
        this.value = value;
    }

    public static Conditional fromBoolean(Boolean value) {
        return new Conditional(value);
    }

    public <X extends Throwable> Boolean ifTrueThenThrow(Supplier<? extends X> exceptionSupplier) throws X {
        if (!value) {
            return value;
        } else {
            throw exceptionSupplier.get();
        }
    }

    public <X extends Throwable> Boolean ifFalseThenThrow(Supplier<? extends X> exceptionSupplier) throws X {
        if (value) {
            return value;
        } else {
            throw exceptionSupplier.get();
        }
    }

}
