# ZenTriggers
A mod that allows for pack creators to set up custom actions in response to certain triggers.


## Example Script
```ZenScript
import mods.zentriggers.actions.DimensionChangedAction;
import mods.zentriggers.actions.ItemPickupAction;

// creates and adds a listener for when players change dimension from a specific one to a specific one.
// params:
//  string command
//  list<dimid> leaving
//  list<dimid> joining
// transformers:
//  $player -> player that changed dimensions
DimensionChangedAction.create("say welcome to the nether, boi", [0],[-1]);
DimensionChangedAction.create("say okaeri, onii-chan", [-1],[0]);

// creates and adds a listener for when players pick up a specific itemstack
// params:
//  string command
//  list<itemstack> filter
//  string whitelist|blacklist
// transformers:
//  $player -> player that picked up the itemstack
//  $itemstack -> registry name of the picked up itemstack
ItemPickupAction.create("give $player $itemstack", [<minecraft:stick>], "whitelist");
```