package games.moegirl.sinocraft.sinocore.api.data.loot;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.loot.BlockLoot;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @author skyinr
 */
public abstract class LootTableProviderBase extends LootTableProvider {
    protected final String modID;

    public LootTableProviderBase(DataGenerator pGenerator, String modID) {
        super(pGenerator);
        this.modID = modID;
    }

    @Nonnull
    @Override
    public String getName() {
        return super.getName() + ":" + modID;
    }

    /**
     * Return the block loot table
     *
     * @return Block loot table
     */
    public abstract BlockLoot getBlockLootTable();

    @Nonnull
    @Override
    protected List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootContextParamSet>> getTables() {
        ImmutableList.Builder<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootContextParamSet>> builder = new ImmutableList.Builder<>();
        BlockLoot blockLootTable = getBlockLootTable();
        if (blockLootTable != null) {
            builder.add(Pair.of(() -> blockLootTable, LootContextParamSets.BLOCK));
        }
        return builder.build();
    }

    @Override
    protected void validate(Map<ResourceLocation, LootTable> map, ValidationContext validationtracker) {
        // skyinr: Do not validate against all registered loot tables
    }
}
