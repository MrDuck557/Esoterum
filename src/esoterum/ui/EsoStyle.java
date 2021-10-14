package esoterum.ui;

import arc.scene.ui.TextButton;
import arc.scene.ui.TextButton.*;
import mindustry.ui.Fonts;

import static arc.Core.atlas;

public class EsoStyle {
    public static TextButton.TextButtonStyle pianoBlack, pianoWhite;
    public static TextButtonStyle[] rests = new TextButtonStyle[5];

    public static void init(){
        // ninepatches by @sk7725
        pianoBlack = new TextButton.TextButtonStyle(
            atlas.drawable("esoterum-keyb"),
            atlas.drawable("esoterum-keyb-down"),
            atlas.drawable("esoterum-keyb-checked"),
            Fonts.def
        );

        pianoWhite = new TextButton.TextButtonStyle(
            atlas.drawable("esoterum-keyw"),
            atlas.drawable("esoterum-keyw-down"),
            atlas.drawable("esoterum-keyw-checked"),
            Fonts.outline
        );

        //Rests by MEEP. (Not actual ninepatches, do not resize the button)
        for(int i = 0; i < 5; i++){
            rests[i] = new TextButtonStyle(
                atlas.drawable("esoterum-rest-" + i),
                atlas.drawable("esoterum-rest-" + i + "-down"),
                atlas.drawable("esoterum-rest-" + i + "-checked"),
                Fonts.def
            );
        }
    }
}
