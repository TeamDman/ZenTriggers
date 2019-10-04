# ZenTriggers
A mod that allows for pack creators to set up custom actions in response to certain triggers.

[Example video](https://streamable.com/ce27v)
[More example](https://streamable.com/zb9d9)

[ILivingEvent](https://github.com/CraftTweaker/CraftTweaker/blob/1.12/CraftTweaker2-API/src/main/java/crafttweaker/api/event/ILivingEvent.java)
[IEntityLivingBase](https://github.com/CraftTweaker/CraftTweaker/blob/1.12/CraftTweaker2-API/src/main/java/crafttweaker/api/entity/IEntityLivingBase.java)
[IEntity](https://github.com/CraftTweaker/CraftTweaker/blob/1.12/CraftTweaker2-API/src/main/java/crafttweaker/api/entity/IEntity.java)

[EntityUpdatePredicateBuilder](https://github.com/TeamDman/ZenTriggers/blob/master/src/main/java/zentriggers/zentriggers/EntityUpdatePredicateBuilder.java)
[WorldTickPredicateBuilder](https://github.com/TeamDman/ZenTriggers/blob/master/src/main/java/zentriggers/zentriggers/WorldTickPredicateBuilder.java)

NOTE: isInMaterial predicate checks at the entity's eye height, not foot level. This might be unintuitive, but it's the method used to check if a player's vision is obscured by lava/water

NOTE: The predicates are short circuit evaluated in the order that they are built in, using the least expensive predicates first will be good for performance.

NOTE: Actions like spawning items should only be done on server side. Use `.isRemote().negateLatest()` in the predicate builder to ensure this.

NOTE: Now that there is also a WorldTickHandler, the original handler is now importable as EntityUpdateHandler if you want. 
## Example Script
```zenscript
import mods.zentriggers.PredicateBuilder;
import mods.zentriggers.Profiler;
import mods.zentriggers.Handler;
import mods.zentriggers.events.EntityLivingUpdateEvent;
import crafttweaker.entity.IEntity;
import crafttweaker.block.IMaterial;
import crafttweaker.block.IBlock;
import crafttweaker.block.IBlockState;

Profiler.start("ZenSummoning");

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
        .isInstanceOf("minecraft:chicken")
        .isInBlockState(<blockstate:minecraft:water:level=7> as IBlockState)
    ,function(event as EntityLivingUpdateEvent) {
        <entity:minecraft:cow>.spawnEntity(event.entity.world,  event.entity.position);
    }
);

Handler.onEntityUpdate(
    PredicateBuilder.create()
        .isInstanceOf(<entity:minecraft:chicken>)
        .isNthTick(10)
        .isInBlockState(<blockstate:minecraft:water:level=0> as IBlockState)
    ,function(event as EntityLivingUpdateEvent) {
        print("a");
        event.entity.world.spawnEntity(<minecraft:stick>.createEntityItem(event.entity.world,  event.entity.position));
        print("b");
    }
);

Handler.onEntityUpdate(
    PredicateBuilder.create()
        .isNthTick(20)
        .isInBlockArea(<blockstate:minecraft:lava>.block,3,3,1,4)
    ,function(event as EntityLivingUpdateEvent) {
        event.entityLivingBase.health = 5;
    }
);

Handler.setRawInterval(10);
Handler.onEntityUpdateRaw(
		PredicateBuilder.create() // no isNthTick needed, raw events are limited by the raw interval
		    .isRemote()
		    .negateLatest()
			.isRandom(0.1)
			.isInstanceOf("minecraft:pig")

		,function(event as EntityLivingUpdateEvent){
            val item = <minecraft:diamond>;
            if(isNull(item)) {
                print("item is null in event");
                return;
            }
            if(isNull(event.entity)){
                print("Entity is null in event");
                return;
            }
            event.entity.world.spawnEntity(item.createEntityItem(event.entity.world, event.entity.position));
            event.entityLivingBase.health -= 5;
	    }
);


print("ZenSummoning took " + Profiler.finish("ZenSummoning"));


```


## Example script using world tick event

```zs
import mods.zentriggers.events.WorldTickEvent;
import mods.zentriggers.WorldTickHandler;
import mods.zentriggers.WorldTickPredicateBuilder;
import mods.zentriggers.world.World;

WorldTickHandler.onWorldTick(
    WorldTickPredicateBuilder.create()
        .isDay()
        .negateLatest()
    ,function(event as WorldTickEvent) {
        event.world.worldTime = (24000 as long);
    }
);

WorldTickHandler.onWorldLoadTick(
    WorldTickPredicateBuilder.create()
        .isDimension(0)
    ,function(event as WorldTickEvent) {
        event.world.worldTime = (34000 as long);
    }
);
// there is also a onWorldUnloadTick method.

WorldTickHandler.setRawInterval(200);
```
