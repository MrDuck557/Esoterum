package esoterum.ui;

// TODO make an actual manual
public class ManualPages {
    public static ManualPage[][] topics = new ManualPage[][]{
        // signal distribution
        new ManualPage[]{
            new ManualPage(t -> {
                t.addText("behold");
                t.addImage("esoterum-froge", "Fig 1. Froge");
                t.addText("i see");
            }),
            new ManualPage(t -> {
                t.addText("behold again");
                t.addImage("esoterum-froge", "Fig 2. Froge");
                t.addText("i see");
            }),
        },
        // signal sources
        new ManualPage[]{
            new ManualPage(t -> {
                t.addText("sources topic");
            }),
            new ManualPage(t -> {
                t.addText("i am stupid");
            }),
        },
        // gates
        new ManualPage[]{
            new ManualPage(t -> {
                t.addText("gates topic");
            }),
            new ManualPage(t -> {
                t.addText("i am stupid");
            }),
        },
        // circuits
        new ManualPage[]{
            new ManualPage(t -> {
                t.addText("circuits topic");
            }),
            new ManualPage(t -> {
                t.addText("i am stupid");
            }),
        },
    };
}
