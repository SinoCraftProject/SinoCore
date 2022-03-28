package games.moegirl.sinocraft.sinocore.api.registation;

import net.minecraft.world.effect.MobEffectInstance;

import java.lang.annotation.*;

@Deprecated
@Retention(RetentionPolicy.RUNTIME)
public @interface Effect {
    Class<? extends MobEffectInstance> effect();

    float duration();
}
