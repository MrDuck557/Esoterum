package esoterum.ui;

public class ManualPages {
    public static ManualPage[][] topics = new ManualPage[][]{
        // signal distribution
        new ManualPage[]{
            new ManualPage(t -> {
                t.addText("behold");
                t.addImage("esoterum-froge", "Fig 1. Froge");
                t.addText("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nulla non iaculis sem. Nunc non magna elit. Ut pharetra posuere nisl. Quisque ut mi blandit, lobortis risus non, blandit odio. Nulla facilisi. Fusce eu arcu ullamcorper, ullamcorper neque in, volutpat lacus. Phasellus congue placerat lectus at faucibus. Proin nulla lacus, faucibus a nibh placerat, tempus posuere tortor. Nulla in velit pharetra justo faucibus dapibus.\n" +
                    "\n" +
                    "Aenean ut ex quis nisl dictum semper eget sed diam. Vivamus dui arcu, varius sed facilisis non, tincidunt vitae ante. Phasellus non lacus eu lacus dictum semper. Etiam sed commodo ipsum. Aenean efficitur semper purus, et euismod arcu rhoncus vitae. Aenean hendrerit orci erat, nec ullamcorper lacus tincidunt non. Donec iaculis nunc aliquet commodo volutpat. Nulla fringilla luctus nulla tempor iaculis. Ut nec ex ac augue euismod venenatis. Praesent leo nisl, rutrum vel posuere et, tincidunt at eros. Donec eget ante velit. Vivamus imperdiet faucibus massa vel iaculis. Nam eget nisi eget massa tempor vehicula vitae sed turpis. Fusce fermentum nibh non erat ornare, non iaculis nunc volutpat. Sed sed fermentum justo. Sed vehicula pellentesque cursus.\n" +
                    "\n" +
                    "Integer eget velit nisi. Suspendisse quis porttitor ante. Maecenas feugiat condimentum convallis. In at sem at dolor ultricies facilisis. Ut in mi at est laoreet auctor ut eu mi. Suspendisse at eleifend risus, nec dignissim lorem. Curabitur vitae ligula a odio tempor porttitor a quis ex. Praesent lacinia dui vitae lacus maximus, eu pulvinar magna luctus. Aenean vitae nunc in mauris interdum varius dictum eu arcu. Quisque vitae suscipit metus, nec consectetur lacus. Aliquam nec dui in sapien lacinia tempus. Duis ligula tortor, faucibus aliquam cursus non, viverra non ante. Pellentesque mauris neque, finibus ornare justo vitae, gravida molestie nulla. Quisque ut ante aliquet, ullamcorper neque non, pretium mauris. Etiam diam lorem, interdum ut porttitor ut, aliquam euismod erat.\n" +
                    "\n" +
                    "Nam nec nibh nulla. Praesent in massa eget nisi commodo sagittis. Curabitur accumsan lobortis lorem. Proin imperdiet lectus tortor, in tincidunt lectus congue scelerisque. Aliquam consectetur, lorem sit amet viverra convallis, mi odio tristique lectus, et efficitur erat felis in purus. Suspendisse quis urna nec arcu pellentesque consequat. Mauris a dolor id erat cursus aliquam nec quis urna. Nam magna arcu, lacinia a congue a, sagittis ac nisl.\n" +
                    "\n" +
                    "Donec mauris ipsum, placerat sed augue ac, varius euismod velit. Vivamus ac blandit sapien. Phasellus congue augue diam, nec tempor odio egestas vel. Vestibulum consectetur purus quis odio placerat, interdum dictum massa blandit. Proin ac molestie purus, sed vulputate quam. In tincidunt interdum neque sed consectetur. Nulla luctus libero id mi aliquam molestie. Nam rhoncus nisl quis leo tempor, quis pulvinar augue varius. Praesent a nulla metus. Nullam ullamcorper consequat ligula, eu feugiat diam. Etiam ac tincidunt odio.");
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
