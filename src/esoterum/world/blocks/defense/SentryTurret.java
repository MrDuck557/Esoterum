package esoterum.world.blocks.defense;

import arc.Core;
import arc.graphics.Blending;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Lines;
import arc.math.Angles;
import arc.math.Mathf;
import arc.util.Time;
import esoterum.graphics.EsoDrawf;
import mindustry.Vars;
import mindustry.entities.Units;
import mindustry.game.Team;
import mindustry.gen.Sounds;
import mindustry.graphics.Layer;
import mindustry.graphics.Pal;
import mindustry.world.blocks.defense.turrets.PowerTurret;

public class SentryTurret extends PowerTurret {
    public float detectionCone = 45f;
    public float swayScl = 15f;
    public float swayMag = 0f;
    public SentryTurret(String name){
        super(name);
        consumesPower = true;
    }

    @Override
    public void load() {
        super.load();

        baseRegion = Core.atlas.find(size == 1 ? "esoterum-node-base" : "esoterum-base-" + size);
    }

    public class SentryBuild extends PowerTurretBuild {
        public float startAngle = 0f;
        public boolean wasLocked = false;

        @Override
        public void draw(){
            super.draw();
            Draw.z(Layer.turret - 1);

            Draw.blend(Blending.additive);
            Draw.color(Color.red);
            Lines.stroke(1);
            if(target == null){
                Lines.lineAngle(x, y, size * 4,rotation, range);
            }
            Draw.blend();
        }

        @Override
        public void updateTile(){
            super.updateTile();
            if((target != null) && !wasLocked) onDetect();
            wasLocked = (target != null);
            if(target == null){
                turnToTarget(startAngle + Mathf.sin(swayScl, swayMag));
            }
        }

        public void onDetect(){

        }

        @Override
        protected boolean validateTarget(){
            return !Units.invalidateTarget(target, canHeal() ? Team.derelict : team, x, y, range) || isControlled() || logicControlled();
        }

        @Override
        protected void findTarget() {
            if(target != null)return;
            if(targetAir && !targetGround){
                target = Units.bestEnemy(team, x, y, range, e -> !e.dead() && !e.isGrounded(), unitSort);
            }else{
                target = Units.bestTarget(team, x, y, range, e -> !e.dead() && (e.isGrounded() || targetAir) && (!e.isGrounded() || targetGround), b -> true, unitSort);

                if(target == null && canHeal()){
                    target = Units.findAllyTile(team, x, y, range, b -> b.damaged() && b != this);
                }
            }

            if(target != null && Angles.angleDist(angleTo(target), rotation + startAngle + Mathf.sin(swayScl, swayMag)) > detectionCone / 2) target = null;
        }
    }
}
