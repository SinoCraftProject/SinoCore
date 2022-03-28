package games.moegirl.sinocraft.sinocore.api.registation;

import net.minecraft.world.item.Rarity;

import java.lang.annotation.*;

@Deprecated
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(Items.class)
@Target({ElementType.FIELD, ElementType.TYPE})
public @interface ModItem {
    String name();

    Food food() default @Food();

    int maxStack() default 64;

    int maxDurability() default Integer.MIN_VALUE;

    String tab() default "";

    Rarity rarity() default Rarity.COMMON;

    boolean isFireResistant() default false;

    boolean isNoRepair() default true;

    boolean isCraftRemainItem() default false;
}
