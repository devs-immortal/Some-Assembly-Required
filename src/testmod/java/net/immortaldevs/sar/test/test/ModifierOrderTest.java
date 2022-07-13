package net.immortaldevs.sar.test.test;

import net.fabricmc.fabric.api.gametest.v1.FabricGameTest;
import net.immortaldevs.sar.api.ComponentNode;
import net.immortaldevs.sar.api.ComponentNodeCollection;
import net.immortaldevs.sar.base.NbtComponentNode;
import net.immortaldevs.sar.test.OrderTestModifier;
import net.immortaldevs.sar.test.SarTestComponents;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.test.GameTest;
import net.minecraft.test.GameTestException;
import net.minecraft.test.TestContext;

import java.util.Objects;

import static net.immortaldevs.sar.test.SarTest.id;

public class ModifierOrderTest {
    @GameTest(templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void testModifierOrder(TestContext context) {
        ItemStack stack = new ItemStack(Items.CARROT);

        ComponentNode a = stack.getOrCreateComponentNode(id("modifier_order_test"),
                SarTestComponents.MODIFIER_ORDER_TEST);
        a.getOrCreateNbt().putString("name", "a");
        stack.updateComponents();

        ComponentNode b = a.getOrCreateComponentNode(id("modifier_order_test"),
                SarTestComponents.MODIFIER_ORDER_TEST);
        b.getOrCreateNbt().putString("name", "b");
        a.updateComponents();

        ComponentNodeCollection nodes = b.getComponentNodes(id("modifier_order_tests"));
        nodes.add(SarTestComponents.MODIFIER_ORDER_TEST);
        nodes.get(0).getOrCreateNbt().putString("name", "c");
        nodes.add(SarTestComponents.MODIFIER_ORDER_TEST);
        nodes.get(1).getOrCreateNbt().putString("name", "d");

        ComponentNode e = new NbtComponentNode(b.getOrCreateSubNbt("child"), b);
        e.setComponent(SarTestComponents.MODIFIER_ORDER_TEST);
        e.getOrCreateNbt().putString("name", "e");
        b.updateComponents();

        ComponentNode f = e.getOrCreateComponentNode(id("modifier_order_test"),
                SarTestComponents.MODIFIER_ORDER_TEST);
        f.getOrCreateNbt().putString("name", "f");
        e.updateComponents();

        f.createComponentNode(id("modifier_test"), SarTestComponents.MODIFIER_TEST);
        f.updateComponents();

        String order = Objects.requireNonNull(stack.getModifier(OrderTestModifier.class)).get();
        if (!"a.adopt(b.adopt(e.adopt(f).merge(c).merge(d)))".equals(order)) {
            throw new GameTestException("Modifiers were loaded in the wrong order: " + order);
        }

        context.complete();
    }
}
