package esoterum.type;

import arc.Core;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import esoterum.content.EsoFx;
import esoterum.graphics.EsoPal;
import mindustry.entities.Effect;
import mindustry.gen.Unit;
import mindustry.graphics.MultiPacker;
import mindustry.type.UnitType;

public class HandsUnitType extends UnitType {
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
    public void load() {
        super.load();

        handsRegion = Core.atlas.find(name + "-hands");
        handsOutlineRegion = Core.atlas.find(name + "-hands-outline", "blank");
    }

    @Override
    public void update(Unit unit) {
        super.update(unit);

        float ang = Angles.angle(unit.x, unit.y, unit.aimX(), unit.aimY());
        if(Angles.angleDist(unit.rotation(), ang) > maxHandAngle && !unit.moving()){
            unit.aimLook(unit.aimX(), unit.aimY());
        }
        unit.splashTimer(unit.splashTimer += Mathf.dst(unit.deltaX(), unit.deltaY()));
        if(unit.splashTimer() >= trailSpacing){
            hoverEffect.at(unit.x, unit.y, 0f, groundLayer);
            unit.splashTimer(0);
        }
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
        float ang = Angles.angle(unit.x, unit.y, unit.aimX(), unit.aimY()) - unit.rotation();
        Draw.rect(handsRegion, unit.x, unit.y, (unit.rotation() + Mathf.clamp(ang, -maxHandAngle, maxHandAngle)) - 90f);
        super.drawBody(unit);
    }

    @Override
    public void drawOutline(Unit unit) {
        float ang = Angles.angle(unit.x, unit.y, unit.aimX(), unit.aimY()) - unit.rotation();
        Draw.rect(handsOutlineRegion, unit.x, unit.y, (unit.rotation() + Mathf.clamp(ang, -maxHandAngle, maxHandAngle)) - 90f);
        super.drawOutline(unit);
    }
}
