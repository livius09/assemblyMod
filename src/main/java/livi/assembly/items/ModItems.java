package livi.assembly.items;

import livi.assembly.Assembly;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

import java.util.function.Function;



public class ModItems {

    public static final Item BELT = registerItem("belt", Item::new);
    public static final Item SPEEDBELT = registerItem("Speedbelt", Item::new);
    public static final Item SHOCKER = registerItem("schocker", Item::new);
    public static final Item BREAKER = registerItem("breaker", Item::new);


    private static Item registerItem(String name, Function<Item.Settings, Item> function) {
        return Registry.register(Registries.ITEM, Identifier.of(Assembly.MOD_ID, name),
                function.apply(new Item.Settings().registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(Assembly.MOD_ID, name)))));
    }

    public static void register_items(){
        Assembly.LOGGER.info("initializing items for: "+Assembly.MOD_ID);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.REDSTONE).register(entries ->{
            entries.add(BELT);
            entries.add(SPEEDBELT);

        });


    }
}
