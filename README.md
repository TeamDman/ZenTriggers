# ZenTriggers
A mod that allows for pack creators to set up custom actions in response to certain triggers.

[Example video](https://streamable.com/ce27v)

[ILivingEvent](https://github.com/CraftTweaker/CraftTweaker/blob/1.12/CraftTweaker2-API/src/main/java/crafttweaker/api/event/ILivingEvent.java)
[IEntityLivingBase](https://github.com/CraftTweaker/CraftTweaker/blob/1.12/CraftTweaker2-API/src/main/java/crafttweaker/api/entity/IEntityLivingBase.java)
[IEntity](https://github.com/CraftTweaker/CraftTweaker/blob/1.12/CraftTweaker2-API/src/main/java/crafttweaker/api/entity/IEntity.java)

[PredicateBuilder 'docs'](https://github.com/TeamDman/ZenTriggers/blob/master/src/main/java/zentriggers/zentriggers/PredicateBuilder.java)
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
