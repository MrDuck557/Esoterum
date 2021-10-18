package esoterum.content;

import arc.func.*;
import arc.struct.*;
import arc.struct.ObjectMap.*;
import esoterum.entities.units.*;
import esoterum.type.*;
import mindustry.ai.types.*;
import mindustry.ctype.*;
import mindustry.gen.*;
import mindustry.type.*;

public class EsoUnits implements ContentList{
    //Steal from Progressed Materials which stole from Endless Rusting which stole from Progressed Materials in the past which stole from BetaMindy
    private static final Entry<Class<? extends Entityc>, Prov<? extends Entityc>>[] types = new Entry[]{
        prov(HandsUnitEntity.class, HandsUnitEntity::new)
    };

    private static final ObjectIntMap<Class<? extends Entityc>> idMap = new ObjectIntMap<>();

    /**
     * Internal function to flatmap {@code Class -> Prov} into an {@link Entry}.
     * @author GlennFolker
     */
    private static <T extends Entityc> Entry<Class<T>, Prov<T>> prov(Class<T> type, Prov<T> prov){
        Entry<Class<T>, Prov<T>> entry = new Entry<>();
        entry.key = type;
        entry.value = prov;
        return entry;
    }

    /**
     * Setups all entity IDs and maps them into {@link EntityMapping}.
     * @author GlennFolker
     */

    private static void setupID(){
        for(
            int i = 0,
            j = 0,
            len = EntityMapping.idMap.length;

            i < len;

            i++
        ){
            if(EntityMapping.idMap[i] == null){
                idMap.put(types[j].key, i);
                EntityMapping.idMap[i] = types[j].value;

                if(++j >= types.length) break;
            }
        }
    }

    /**
     * Retrieves the class ID for a certain entity type.
     * @author GlennFolker
     */
    public static <T extends Entityc> int classID(Class<T> type){
        return idMap.get(type, -1);
    }

    public static UnitType
        //crumb,
        nibble;
        //byte


    @Override
    public void load(){
        handsID = EntityMapping.register("nibble", HandsUnitEntity::new);
        nibble = new HandsUnitType("nibble"){{
            defaultController = BuilderAI::new;
            isCounted = false;

            hovering = true;

            flying = true;
            mineTier = 0;
            buildSpeed = 1.2f;
            drag = 0.05f;
            speed = 2.9f;
            rotateSpeed = 9f;
            accel = 0.1f;
            itemCapacity = 60;
            health = 200f;
            hitSize = 9f;
            rotateShooting = true;
            lowAltitude = true;
            commandLimit = 4;
        }};
    }
}
