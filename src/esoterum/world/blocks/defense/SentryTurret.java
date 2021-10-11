package esoterum.world.blocks.defense;

import arc.Core;
import arc.graphics.Blending;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Lines;
import arc.math.Angles;
import esoterum.graphics.EsoDrawf;
import mindustry.entities.Units;
import mindustry.game.Team;
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
            Draw.color(Color.white);
            if(target != null){
                Lines.stroke(2);
                Lines.circle(target.x(), target.y(), 5);
                Lines.lineAngle(x, y, angleTo(target), range);
                Lines.lineAngle(x, y, 0, range);
                Draw.color(Color.red);
                Lines.lineAngle(x, y, Angles.angleDist(angleTo(target), 0), range);
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

            if(target != null && Angles.angleDist(angleTo(target), startAngle) > detectionCone / 2) target = null;
        }
    }
}
