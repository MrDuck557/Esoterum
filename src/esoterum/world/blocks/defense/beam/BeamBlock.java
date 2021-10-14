package esoterum.world.blocks.defense.beam;

import arc.Core;
import arc.graphics.Blending;
import arc.graphics.Color;
import arc.graphics.g2d.*;
import arc.math.Mathf;
import arc.math.geom.Vec2;
import arc.scene.ui.layout.Table;
import arc.util.Time;
import arc.util.Tmp;
import arc.util.io.Reads;
import arc.util.io.Writes;
import esoterum.content.EsoFx;
import esoterum.util.EsoUtil;
import esoterum.world.blocks.binary.BinaryBlock;
import mindustry.Vars;
import mindustry.content.Fx;
import mindustry.core.World;
import mindustry.gen.Icon;
import mindustry.gen.Sounds;
import mindustry.gen.Unit;
import mindustry.graphics.Drawf;
import mindustry.graphics.Layer;
import mindustry.graphics.Pal;
import mindustry.world.Tile;

public class BeamBlock extends BinaryBlock {
    public float beamLength = 40f;
    public float beamDamage = 15f;
    public boolean acceptsBeam = false;

    public TextureRegion lightRegion;
    public TextureRegion glowRegion;
    public boolean drawLight = false;

    public BeamBlock(String name){
        super(name);
        configurable = true;
        rotatedBase = false;
        clipSize = beamLength;
        drawArrow = true;

        config(Float.class, (BeamBuild b, Float f) -> {
            b.beamRotation = f;
        });
    }

    @Override
    public void load() {
        super.load();
        region = Core.atlas.find(size == 1 ? "esoterum-node-base" : "esoterum-base-" + size);
        topRegion = Core.atlas.find(name);
        lightRegion = Core.atlas.find(name + "-light");
        glowRegion = Core.atlas.find(name + "-glow");
    }

    public void unitHit(Unit u, BeamBuild b){
        u.damage(beamDamage * Time.delta);
        if(Mathf.chanceDelta(0.5)){
            EsoFx.beamHit.at(u.x, u.y);
            Sounds.spark.at(b.x, b.y);
        }
    }

    public class BeamBuild extends BinaryBuild {
        public float beamRotation = 0f;
        public float beamDrawLength = 0f;

        public boolean active = false;

        public Vec2 tr = new Vec2();
        Tile hit;

        @Override
        public void draw() {
            Draw.rect(region, x, y);
            Draw.z(Layer.turret);
            Draw.rect(topRegion, x, y, beamRotation + rotdeg());

            if(!drawLight) return;
            Draw.color(active ? Pal.lancerLaser : Color.white);
            Draw.rect(lightRegion, x, y, beamRotation + rotdeg());

            if(!active) return;
            Draw.blend(Blending.additive);
            Draw.rect(glowRegion, x, y, beamRotation + rotdeg());
            Draw.blend();
        }

        @Override
        public void drawSelect() {
            super.drawSelect();
            Drawf.dashCircle(x, y, beamLength, Pal.lancerLaser);
            Tmp.v2.setZero().trns(beamRotation + rotdeg(), beamLength / 2);
            Drawf.dashLine(Pal.lancerLaser, x, y, x + Tmp.v2.x, y + Tmp.v2.y);
        }

        // config
        @Override
        public void buildConfiguration(Table table) {
            table.button(Icon.leftSmall, () -> {
                beamRotation += 15f;
                beamRotation = Mathf.mod(beamRotation, 360f);
                configure(beamRotation);
            });
            table.button(Icon.rightSmall, () -> {
                beamRotation -= 15f;
                beamRotation = Mathf.mod(beamRotation, 360f);
                configure(beamRotation);
            });
        }

        @Override
        public void drawConfigure() {
            super.drawConfigure();
            Drawf.dashCircle(x, y, beamLength, Pal.lancerLaser);
            Tmp.v2.setZero().trns(beamRotation + rotdeg(), beamLength / 2);
            Drawf.dashLine(Pal.lancerLaser, x, y, x + Tmp.v2.x, y + Tmp.v2.y);
        }

        @Override
        public Object config() {
            return beamRotation;
        }

        // beam stuff
        public float beam(float rot, boolean damage){
            tr.set(0f, 0f).trnsExact(rot, beamLength);

            hit = null;

            boolean found = Vars.world.raycast(tileX(), tileY(), World.toTile(x + tr.x), World.toTile(y + tr.y),
            (x, y) -> (hit = Vars.world.tile(x, y)) != null && hit.build != null && hit.build != this && (hit.build.checkSolid() || hit.block().solid)
            );

            float length = found ? Mathf.dst(x, y, hit.worldx(), hit.worldy()) : beamLength;

            if(found && hit.build != null && hit.build instanceof BeamBuild b && b.acceptsBeam()){
                b.signal(true);
            }

            if(damage){
                EsoUtil.linecastUnits(x, y, rot, length, u -> {
                    unitHit(u, this);
                });
            }

            return length;
        }

        public void updateBeam(){
            beamDrawLength = beam(beamRotation + rotdeg(), true);
            active = true;
        }

        public void drawBeam(float rot, float length){
            Draw.z(Layer.turret - 1);
            Draw.blend(Blending.additive);

            Lines.stroke(1.5f);
            Draw.color(Pal.lancerLaser);
            Lines.lineAngle(x, y, rot, length);
            Tmp.v2.trns(rot, length);
            Fill.circle(x + Tmp.v2.x, y + Tmp.v2.y, 2);

            Draw.blend();
        }

        public boolean acceptsBeam(){
            return acceptsBeam;
        }

        @Override
        public void read(Reads read, byte revision) {
            super.read(read, revision);

            if(revision >= 2) beamRotation = read.f();
        }

        @Override
        public void write(Writes write) {
            super.write(write);

            write.f(beamRotation);
        }

        @Override
        public byte version() {
            return 2;
        }
    }
}
