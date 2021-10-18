package esoterum.graphics;

import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.math.geom.*;

import static arc.graphics.g2d.Lines.*;
import static arc.math.Angles.angle;
import static arc.math.Mathf.*;

public class EsoDrawf{
    private static final Vec2 v1 = new Vec2(), v2 = new Vec2();

    public static void curvedLine(float x, float y, float tx, float ty, float w){
        float dst = dst(x, y, tx, ty);
        int seg = Mathf.ceil(dst / 2f);
        float ang = angle(x, y, tx, ty) - 90f;

        for(int i = 0; i < seg; i++){
            float p1 = i / (float)seg, p2 = ((i + 1f) / seg);
            v1.trns(ang, Mathf.sin(p1 * Mathf.PI) * w, Mathf.lerp(0f, dst, p1));
            v2.trns(ang, Mathf.sin(p2 * Mathf.PI) * w, Mathf.lerp(0f, dst, p2));
            Lines.line(
                x + v1.x, y + v1.y,
                x + v2.x, y + v2.y
            );
        }
    }

    public static void arc(float x, float y, float rad, float angle, float arc){
        int seg = Mathf.ceil(circleVertices(rad) * (arc / 360f));
        float a = -arc / seg;

        for(int i = 0; i < seg; i++){
            v1.trns(a * i + angle, rad);
            v2.trns(a * (i + 1) + angle, rad);
            Fill.tri(
                x, y,
                x + v1.x, y + v1.y,
                x + v2.x, y + v2.y
            );
        }
    }

    public static void spotlight(float x, float y, float rad, float angle, float arc, Color color, int shades){
        float step = rad / shades;
        for(int i = 0; i < (shades == 0 ? 1 : shades); i++){
            Draw.color(color);
            Draw.alpha(((float) i / shades) * 0.4f);
            arc(x, y, rad - step * i, angle, arc);
        }
    }
}
