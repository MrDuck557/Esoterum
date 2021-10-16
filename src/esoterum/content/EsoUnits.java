package esoterum.content;

import esoterum.type.HandsUnitType;
import mindustry.ai.types.BuilderAI;
import mindustry.ctype.ContentList;
import mindustry.gen.Unit;
import mindustry.gen.UnitEntity;
import mindustry.type.UnitType;

public class EsoUnits implements ContentList{
    public static UnitType
        //crumb,
        nibble;
        //byte


    @Override
    public void load() {
        nibble = new HandsUnitType("nibble"){{
            defaultController = BuilderAI::new;
            constructor = UnitEntity::create;
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
