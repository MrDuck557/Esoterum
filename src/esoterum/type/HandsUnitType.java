package esoterum.type;

import arc.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.util.*;
import esoterum.content.*;
import esoterum.entities.units.*;
import esoterum.graphics.*;
import mindustry.*;
import mindustry.entities.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.world.blocks.environment.*;

public class HandsUnitType extends UnitType{
    public TextureRegion handsRegion, handsOutlineRegion;
    public float maxHandAngle = 45f;
    public float trailSpacing = 16f;
    public Effect hoverEffect;

    public HandsUnitType(String name){
        super(name);
        outlineColor = EsoPal.esoOutline;
        hoverEffect = EsoFx.unitHover;
    }

    @Override
    public void load(){
        super.load();
        constructor = HandsUnitEntity::new;

        handsRegion = Core.atlas.find(name + "-hands");
        handsOutlineRegion = Core.atlas.find(name + "-hands-outline", "blank");
    }

    @Override
    public void createIcons(MultiPacker packer) {
        super.createIcons(packer);
        if(outlines){
            // why is makeOutline private :(
            if(!packer.has(name + "-hands-outline")){
                PixmapRegion handsBase = Core.atlas.getPixmap(name + "-hands");
                Pixmap result = Pixmaps.outline(handsBase, outlineColor, outlineRadius);
                if(Core.settings.getBool("linear", true)){
                    Pixmaps.bleed(result);
                }
                packer.add(MultiPacker.PageType.main, name + "-hands-outline", result);
            }
        }
    }

    @Override
    public void draw(Unit unit) {
        super.draw(unit);
    }

    @Override
    public void drawBody(Unit unit) {
        if(unit instanceof HandsUnitEntity u){
            Draw.rect(handsRegion, unit.x, unit.y, u.handsAngle - 90f);
        }
        super.drawBody(unit);
    }

    @Override
    public void drawEngine(Unit unit) {
        if(unit instanceof HandsUnitEntity u) {
            float offset = engineOffset / 2f + engineOffset / 2f * unit.elevation;
            Draw.color(Pal.lancerLaser);
            Fill.circle(
                unit.x + Angles.trnsx(unit.rotation + 180, offset),
                unit.y + Angles.trnsy(unit.rotation + 180, offset),
                (u.engineScl + Mathf.absin(Time.time, 2f, u.engineScl / 4f)) * unit.elevation
            );
            Draw.blend(Blending.additive);
            Fill.light(
                unit.x + Angles.trnsx(unit.rotation + 180, offset),
                unit.y + Angles.trnsy(unit.rotation + 180, offset),
                15, ((u.engineScl * 1.3f) + Mathf.absin(Time.time, 2f, u.engineScl / 4f)) * unit.elevation,
                Pal.lancerLaser, Color.clear
            );
            Draw.blend();
            Draw.reset();
        }
    }

    @Override
    public void drawOutline(Unit unit) {
        if(unit instanceof HandsUnitEntity u){
            Draw.rect(handsOutlineRegion, unit.x, unit.y, u.handsAngle - 90f);
        };
        super.drawOutline(unit);
    }

    @Override
    public void drawShadow(Unit unit) {
        float e = Math.max(unit.elevation, visualElevation) * (1f - unit.drownTime);
        float x = unit.x + shadowTX * e, y = unit.y + shadowTY * e;
        Floor floor = Vars.world.floorWorld(x, y);

        float dest = floor.canShadow ? 1f : 0f;
        unit.shadowAlpha = unit.shadowAlpha < 0 ? dest : Mathf.approachDelta(unit.shadowAlpha, dest, 0.11f);
        Draw.color(Pal.shadow, Pal.shadow.a * unit.shadowAlpha);

        Draw.rect(shadowRegion, unit.x + shadowTX * e, unit.y + shadowTY * e, unit.rotation - 90);
        if(unit instanceof HandsUnitEntity u){
            Draw.rect(handsRegion, unit.x + shadowTX * e, unit.y + shadowTY * e, u.handsAngle - 90);
        }
        Draw.color();
    }
}
