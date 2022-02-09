package net.immortaldevs.sar.base;

import net.immortaldevs.sar.api.Component;
import net.immortaldevs.sar.api.LarvalComponentData;
import net.immortaldevs.sar.api.Modifier;

import java.util.Collection;

@SuppressWarnings("unused")
public class SimpleComponent extends Component {
    protected final Modifier[] modifiers;

    public SimpleComponent(Collection<Modifier> modifiers) {
        this(modifiers.toArray(Modifier[]::new));
    }

    public SimpleComponent(Modifier... modifiers) {
        this.modifiers = modifiers;
    }

    @Override
    public void init(LarvalComponentData data) {
        for (Modifier modifier : this.modifiers) {
            data.addModifier(modifier);
        }
    }
}
