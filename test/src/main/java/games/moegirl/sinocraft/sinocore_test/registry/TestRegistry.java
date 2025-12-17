package games.moegirl.sinocraft.sinocore_test.registry;

import games.moegirl.sinocraft.sinocore.api.registry.IRegRef;
import games.moegirl.sinocraft.sinocore.api.registry.IRegistry;
import games.moegirl.sinocraft.sinocore.api.registry.ITabRegistry;
import games.moegirl.sinocraft.sinocore.api.registry.RegistryManager;
import games.moegirl.sinocraft.sinocore_test.SinoCoreTest;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

import static games.moegirl.sinocraft.sinocore_test.SinoCoreTest.MODID;

public class TestRegistry {

    public static final IRegistry<Item> ITEMS = RegistryManager.create(MODID, Registries.ITEM);
    public static final IRegistry<Block> BLOCKS = RegistryManager.create(MODID, Registries.BLOCK);
    public static final IRegistry<TestRegistryItem> TEST_REGISTRY = RegistryManager.create(MODID, ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(MODID, "test_data")));
    public static final ITabRegistry TABS = RegistryManager.createTab(MODID);

    private static final ResourceKey<CreativeModeTab> VANILLA_BUILDING_BLOCKS_TAB = ResourceKey.create(Registries.CREATIVE_MODE_TAB, ResourceLocation.withDefaultNamespace("building_blocks"));

    public static IRegRef<Item> TEST_ITEM_TAB_1;
    public static IRegRef<Item> TEST_ITEM_TAB_2;

    public static IRegRef<CreativeModeTab> TEST_TAB;

    public static IRegRef<Block> TEST_BLOCK;
    public static IRegRef<BlockItem> TEST_BLOCK_ITEM;
    public static IRegRef<TestRegistryItem> TEST_REGISTRY_ITEM_1;
    public static IRegRef<TestRegistryItem> TEST_REGISTRY_ITEM_2;

    public static void registerAll() {
        register();

        ITEMS.register();
        BLOCKS.register();
        TABS.register();
        TEST_REGISTRY.register();
    }

    private static void register() {
        TEST_TAB = TABS.register("sinocore_test_tab", builder -> {
            builder.sino$icon(() -> TestRegistry.TEST_ITEM_TAB_1.get());
        });

        TEST_ITEM_TAB_1 = ITEMS.register("test_item_tab_1", () -> new Item(new Item.Properties().sino$tab(TEST_TAB)));
        TEST_ITEM_TAB_2 = ITEMS.register("test_item_tab_2", () -> new Item(new Item.Properties().sino$tab(TEST_TAB).sino$tab(VANILLA_BUILDING_BLOCKS_TAB)));

        TEST_BLOCK = BLOCKS.register("test_block", () -> new Block(BlockBehaviour.Properties.of()));
        TEST_BLOCK_ITEM = ITEMS.register("test_block", () -> new BlockItem(TEST_BLOCK.get(), new Item.Properties().sino$tab(TEST_TAB)));
        TEST_REGISTRY_ITEM_1 = TEST_REGISTRY.register("test_hello_world", () -> new TestRegistryItem("Hello, world!"));
        TEST_REGISTRY_ITEM_2 = TEST_REGISTRY.register("test_sino_craft", () -> new TestRegistryItem(SinoCoreTest.MODID));
    }
}
