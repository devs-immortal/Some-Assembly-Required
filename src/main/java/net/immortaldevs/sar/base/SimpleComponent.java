package net.immortaldevs.sar.base;

import net.immortaldevs.sar.api.Component;
import net.immortaldevs.sar.api.ComponentNode;
import net.immortaldevs.sar.api.Modifier;
import net.immortaldevs.sar.api.ModifierHandler;

@SuppressWarnings("unused")
public class SimpleComponent extends Component {
    protected final Modifier<?>[] modifiers;

    public SimpleComponent(Modifier<?>... modifiers) {
        this.modifiers = modifiers;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    public void addModifiers(ComponentNode node, ModifierHandler handler) {
        for (Modifier<?> modifier : modifiers) handler.add((Modifier) modifier);
    }
}
