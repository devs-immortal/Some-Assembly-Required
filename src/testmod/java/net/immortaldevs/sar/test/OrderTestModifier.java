package net.immortaldevs.sar.test;

import net.immortaldevs.sar.api.Modifier;

@FunctionalInterface
public interface OrderTestModifier extends Modifier<OrderTestModifier> {
    String get();

    @Override
    default Class<OrderTestModifier> getType() {
        return OrderTestModifier.class;
    }

    @Override
    default OrderTestModifier merge(OrderTestModifier that) {
        return () -> this.get() + ".merge(" + that.get() + ')';
    }

    @Override
    default OrderTestModifier adopt(OrderTestModifier that) {
        return () -> this.get() + ".adopt(" + that.get() + ')';
    }
}
