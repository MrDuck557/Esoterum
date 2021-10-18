package esoterum.content;

import esoterum.entities.units.*;
import esoterum.type.*;
import mindustry.ai.types.*;
import mindustry.ctype.*;
import mindustry.gen.*;
import mindustry.type.*;

public class EsoUnits implements ContentList{
    public static int handsID;
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
