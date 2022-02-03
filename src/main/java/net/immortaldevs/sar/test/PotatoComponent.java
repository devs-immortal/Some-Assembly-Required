package net.immortaldevs.sar.test;

import net.immortaldevs.sar.api.Component;
import net.immortaldevs.sar.api.LarvalComponentData;

public class PotatoComponent extends Component {
    @Override
    public void init(LarvalComponentData data) {
        data.loadChild("filling");
    }
}
