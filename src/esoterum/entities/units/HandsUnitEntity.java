package esoterum.entities.units;

import arc.math.Angles;
import arc.math.Mathf;
import esoterum.content.EsoUnits;
import esoterum.type.HandsUnitType;
import mindustry.gen.UnitEntity;
import mindustry.graphics.Layer;

public class HandsUnitEntity extends UnitEntity {
    public float handsAngle;
    public float trailTimer;
    public float engineScl;

    @Override
    public void update() {
        super.update();

        if(moving()){
            handsAngle = Angles.moveToward(handsAngle, rotation(), type.rotateSpeed);
        }else {
            handsAngle = Angles.moveToward(handsAngle, angleTo(aimX(), aimY()), type.rotateSpeed);
            if (Angles.angleDist(rotation(), handsAngle) >= getType().maxHandAngle) {
                lookAt(handsAngle);
            }
        }

        if((trailTimer += Mathf.dst(deltaX(), deltaY())) >= getType().trailSpacing){
            getType().hoverEffect.at(x, y, 0, type.groundLayer);
            trailTimer = 0f;
        }

        if(moving()){
            engineScl = Mathf.lerpDelta(engineScl, type.engineSize, 0.2f);
        }else{
            engineScl = Mathf.lerpDelta(engineScl, 0, 0.2f);
        }
    }

    public HandsUnitType getType(){
        return (HandsUnitType) type;
    }

    @Override
    public int classId() {
        return EsoUnits.handsID;
    }
}
