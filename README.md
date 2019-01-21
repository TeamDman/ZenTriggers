# ZenTriggers
A mod that allows for pack creators to set up custom actions in response to certain triggers.

[Example video](https://streamable.com/ce27v)

## Example Script
```ZenScript
import mods.zentriggers.PredicateBuilder;
import mods.zentriggers.Handler;
import mods.zentriggers.events.EntityLivingUpdateEvent;
import crafttweaker.block.IMaterial;
import crafttweaker.block.IBlock;
import crafttweaker.block.IBlockState;

Handler.onEntityUpdate(
    PredicateBuilder.create()
        .isNthTick(20)
        .isInMaterial(IMaterial.water())
    ,function(event as EntityLivingUpdateEvent){
        event.entityLivingBase.health = 1;
    }
);

Handler.onEntityUpdate(
    PredicateBuilder.create()
        .isInBlock(<blockstate:minecraft:lava>.block)
    ,function(event as EntityLivingUpdateEvent){
        event.entityLivingBase.health = 8;
    }
);

Handler.onEntityUpdate(
    PredicateBuilder.create()
        .isNthTick(20)
        .isInDimension(-1)
        .negateLatest()
        .isRandom(0.2)
        .isInBlockState(<blockstate:minecraft:water:level=7> as IBlockState)
    ,function(event as EntityLivingUpdateEvent) {
        event.entityLivingBase.health=7;
    }
);
```