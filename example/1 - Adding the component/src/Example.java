import net.fabricmc.api.ModInitializer;
import net.immortaldevs.sar.api.Component;
import net.immortaldevs.sar.api.LarvalComponentData;
import net.immortaldevs.sar.api.SarRegistries;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public final class Example implements ModInitializer {
    public static final Item BOMB_ITEM = new Item(new Item.Settings().group(ItemGroup.MISC));
    public static final Component BOMB_COMPONENT = new Component() {
        @Override
        public void init(LarvalComponentData data) {
            System.err.println("Hello world!");
        }
    };

    @Override
    public void onInitialize() {
        Registry.register(Registry.ITEM, new Identifier("example", "bomb"), BOMB_ITEM);
        Registry.register(SarRegistries.COMPONENT, new Identifier("example", "bomb"), BOMB_COMPONENT);
    }
}
