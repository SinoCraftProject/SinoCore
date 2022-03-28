package games.moegirl.sinocraft.sinocore.api.registation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Deprecated
@Retention(RetentionPolicy.RUNTIME)
public @interface Food {
    int nutrition() default Integer.MIN_VALUE;

    double saturationModifier() default Double.MIN_EXPONENT;

    boolean isMeat() default false;
    boolean canAlwaysEat() default false;
    boolean fastFood() default false;

    Effect[] effects() default { };
}
