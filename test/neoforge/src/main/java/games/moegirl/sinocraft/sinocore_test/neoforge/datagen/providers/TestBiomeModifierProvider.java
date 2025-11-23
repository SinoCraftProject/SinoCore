package games.moegirl.sinocraft.sinocore_test.neoforge.datagen.providers;

import games.moegirl.sinocraft.sinocore.data.gen.AbstractBiomeModifierProvider;
import games.moegirl.sinocraft.sinocore_test.datagen.TestKeys;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.levelgen.GenerationStep;

public class TestBiomeModifierProvider extends AbstractBiomeModifierProvider {

    public TestBiomeModifierProvider(PackOutput output, String modId) {
        super(output, modId);
    }

    @Override
    protected void registerBiomeModifiers() {
        add(new Feature(TestKeys.TEST_FEATURE, GenerationStep.Decoration.UNDERGROUND_ORES, BiomeTags.IS_OVERWORLD));
    }
}
