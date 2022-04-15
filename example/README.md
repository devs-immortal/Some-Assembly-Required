# Example: Adding a bomb component
In this example, I'll go through the steps of adding a bomb item which can be attached to other items to cause them to explode when used. Each subdirectory contains the code and assets for the corresponding step.

## 0 - Adding the item
A simple item. This is no different to adding any other item, so I won't go into detail.

## 1 - Adding the component
Next, I'll add a bomb component. Components are stored in the nbt of the ItemStack, and are responsible for adding Modifiers, which I'll explain soon, to the stack. They do so using the `Component::init` method, but for now, I'll just print a message to stderr. The example code can be tested by running `/give @p salmon{components:{bomb:{id:"example:bomb"}}}`. Notice the times when it gets printed - each one of these corresponds to a new ItemStack instance.

## 2 - A modifier
An ItemStack contains a ModifierMap, which is simply a map of `Class<T extends Modifier>` -> `T`. This map can be read by anything with access to the ItemStack, and components can add to it. The map is initialised lazily; when something accesses it for the first time, it will call the `init` method on all components attached to the ItemStack. The components use this to add their modifiers, and then the result is cached with the ItemStack instance. A modifier could be anything, but generally it's a function that gets called by certain hooks in the code. For example, the SAR-builtin UseOnBlockModifier contains a function which is called whenever the item is used on a block. Multiple copies of this modifier can be added to the same ItemStack, and all of them will run in the order they were added. It's not quite right for the bomb component - we want it to trigger any time the item is used - but it's good enough for now. Using the new example code, the item should now create an explosion when used on a block.

## 3 - Rendering
Although the component is functional, I want to see the bomb on the item. SAR contains a simple rendering system for this purpose.

## 4 - A custom modifier
As mentioned earlier, the UseOnBlockModifier isn't quite what I want for the bomb. I'll now add a new modifier, and use mixin to implement it.

## 5 - A crafting recipe
WIP
