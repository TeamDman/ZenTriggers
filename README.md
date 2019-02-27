# ZenTriggers
A mod that allows for pack creators to set up custom actions in response to certain triggers.

[Example video](https://streamable.com/ce27v)

[PredicateBuilder 'docs'](https://github.com/TeamDman/ZenTriggers/blob/master/src/main/java/zentriggers/zentriggers/PredicateBuilder.java)
## Example Script
```ZenScript
import mods.zentriggers.PredicateBuilder;
import mods.zentriggers.Handler;
import mods.zentriggers.events.EntityLivingUpdateEvent;
import crafttweaker.entity.IEntity;
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

Handler.onEntityUpdate(
    PredicateBuilder.create()
        .isInstanceOf(<entity:minecraft:chicken>)
        .isNthTick(10)
        .isInBlockState(<blockstate:minecraft:water:level=0> as IBlockState)
    ,function(event as EntityLivingUpdateEvent) {
        <entity:minecraft:cow>.spawnEntity(event.entity.world,  event.entity.position);
    }
);
```