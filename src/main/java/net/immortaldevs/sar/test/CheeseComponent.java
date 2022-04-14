package net.immortaldevs.sar.test;

import net.immortaldevs.sar.api.Component;
import net.immortaldevs.sar.api.LarvalComponentData;
import net.immortaldevs.sar.base.AttributeModifierModifier;
import net.immortaldevs.sar.base.EnchantmentLevelModifier;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;

import java.util.UUID;

public class CheeseComponent extends Component {
    protected static final UUID SPEED_MODIFIER_ID = new UUID(
            0xa986dd17_d9cd_461dL, 0x9a2d_f01977b5cb25L);

    @Override
    public void init(LarvalComponentData data) {
        data.addModifier(EnchantmentLevelModifier.of(Enchantments.KNOCKBACK, 12));

        data.addModifier(AttributeModifierModifier.of(EntityAttributes.GENERIC_MOVEMENT_SPEED,
                new EntityAttributeModifier(SPEED_MODIFIER_ID,
                        "cheese cheese cheese cheese cheese",
                        0.25,
                        EntityAttributeModifier.Operation.MULTIPLY_TOTAL)));
    }
}
