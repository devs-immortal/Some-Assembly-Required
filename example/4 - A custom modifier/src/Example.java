import net.fabricmc.api.ModInitializer;
import net.immortaldevs.sar.api.Component;
import net.immortaldevs.sar.api.LarvalComponentData;
import net.immortaldevs.sar.api.SarRegistries;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.explosion.Explosion;

public final class Example implements ModInitializer {
    public static final Item BOMB_ITEM = new Item(new Item.Settings().group(ItemGroup.MISC));
    public static final Component BOMB_COMPONENT = new Component() {
        @Override
        public void init(LarvalComponentData data) {
            data.addModifier((FinishUsingModifier) (stack, world, user) -> {
                if (!world.isClient) {
                    world.createExplosion(user,
                            user.getX(),
                            user.getEyeY(),
                            user.getZ(),
                            3f,
                            true,
                            Explosion.DestructionType.DESTROY);
                }

                if (!(user instanceof PlayerEntity player && player.isCreative())) {
                    stack.setCount(0);
                }
            });
        }
    };

    @Override
    public void onInitialize() {
        Registry.register(Registry.ITEM, new Identifier("example", "bomb"), BOMB_ITEM);
        Registry.register(SarRegistries.COMPONENT, new Identifier("example", "bomb"), BOMB_COMPONENT);
    }
}
