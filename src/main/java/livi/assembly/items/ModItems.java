package livi.assembly.items;

import livi.assembly.Assembly;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.function.Function;



public class ModItems {

    public static final Item BELT = registerItem("belt", Item::new);
    public static final Item SHOCKER = registerItem("schocker", Item::new);
    public static final Item BREAKER = registerItem("breaker", Item::new);
    public static final Item FARMER = registerItem("farmer", Item::new);


    public static final RegistryKey<ItemGroup> ASSEMBLY_ITEM_GROUP_KEY = RegistryKey.of(Registries.ITEM_GROUP.getKey(), Identifier.of(Assembly.MOD_ID, "item_group"));
    public static final ItemGroup ASSEMBLY_ITEM_GROUP = FabricItemGroup.builder()
            .icon(() -> new ItemStack(Items.STONE_SLAB))
            .displayName(Text.translatable("itemGroup.fabric_docs_reference"))
            .build();


    private static Item registerItem(String name, Function<Item.Settings, Item> function) {
        return Registry.register(Registries.ITEM, Identifier.of(Assembly.MOD_ID, name),
                function.apply(new Item.Settings().registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(Assembly.MOD_ID, name)))));
    }

    public static void register_items(){
        Assembly.LOGGER.info("initializing items for: "+Assembly.MOD_ID);

        Registry.register(Registries.ITEM_GROUP, ASSEMBLY_ITEM_GROUP_KEY, ASSEMBLY_ITEM_GROUP);

        ItemGroupEvents.modifyEntriesEvent(ASSEMBLY_ITEM_GROUP_KEY).register(entries ->{
            entries.add(BELT);
            entries.add(SHOCKER);
            entries.add(BREAKER);
            entries.add(FARMER);

        });


    }
}
