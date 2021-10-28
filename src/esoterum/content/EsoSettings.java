package esoterum.content;

import arc.*;
import arc.scene.*;
import arc.scene.ui.layout.*;
import mindustry.game.EventType.*;
import mindustry.ui.*;
import mindustry.ui.dialogs.*;
import mindustry.ui.dialogs.SettingsMenuDialog.*;

import static arc.Core.*;
import static mindustry.Vars.*;

public class EsoSettings{
    static SettingsTable eso;

    public static void init(){
        BaseDialog dialog = new BaseDialog("@setting.eso-title");
        dialog.addCloseButton();

        eso = new SettingsTable();
        eso.checkPref("eso-junction-variation", false);
        eso.sliderPref("eso-signal-millis", 16, 1, 100, s -> s + " " + bundle.get("eso-millis"));
        eso.sliderPref("eso-signal-nanos", 666666, 1, 1000000, s -> s + " " + bundle.get("eso-nanos"));

        dialog.cont.center().add(eso);

        Events.on(ResizeEvent.class, event -> {
            if(dialog.isShown() && Core.scene.getDialog() == dialog){
                dialog.updateScrollFocus();
            }
        });

        ui.settings.shown(() -> {
            Table settingUi = (Table)((Group)((Group)(ui.settings.getChildren().get(1))).getChildren().get(0)).getChildren().get(0); //This looks so stupid lol
            settingUi.row();
            settingUi.button("@setting.eso-title", Styles.cleart, dialog::show);
        });
    }
}