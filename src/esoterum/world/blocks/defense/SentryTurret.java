package esoterum.world.blocks.defense;

import arc.Core;
import arc.graphics.Blending;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Lines;
import arc.math.Angles;
import esoterum.graphics.EsoDrawf;
import mindustry.entities.Units;
import mindustry.graphics.Pal;
import mindustry.world.blocks.defense.turrets.PowerTurret;

public class SentryTurret extends PowerTurret {
    public float detectionCone = 45f;
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

        @Override
        public void draw() {
            super.draw();
            Draw.blend(Blending.additive);
            EsoDrawf.spotlight(x, y, range, rotation + detectionCone / 2, detectionCone, Pal.accent, (int) range / 8);
            Draw.blend();
            if(target != null){
                Lines.stroke(2);
                Lines.circle(target.x(), target.y(), 5);
            }
        }

        @Override
        public void updateTile() {
            super.updateTile();

            if(target == null){
                turnToTarget(startAngle);
            }
        }

        @Override
        protected void findTarget() {
            if(targetAir && !targetGround){
                target = Units.bestEnemy(team, x, y, range, e -> !e.dead() && !e.isGrounded(), unitSort);
            }else{
                target = Units.bestTarget(team, x, y, range, e -> !e.dead() && (e.isGrounded() || targetAir) && (!e.isGrounded() || targetGround), b -> true, unitSort);

                if(target == null && canHeal()){
                    target = Units.findAllyTile(team, x, y, range, b -> b.damaged() && b != this);
                }
            }

            if(target != null && Angles.within(angleTo(target), rotation, detectionCone / 2f)) target = null;
        }
    }
}
