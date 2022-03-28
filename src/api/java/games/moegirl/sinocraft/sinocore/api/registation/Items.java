package games.moegirl.sinocraft.sinocore.api.registation;

import java.lang.annotation.*;

@Deprecated
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.TYPE})
public @interface Items {
    ModItem[] value();
}
