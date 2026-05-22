import greenfoot.*;

public class MyWorld extends World {
    public MyWorld() {
        super(600, 400, 1);

        KaineVelsarth kaine = new KaineVelsarth();
        addObject(kaine, 300, 170);
        showText(kaine.getInventoryText(), 300, 330);
        showText(kaine.getEquippedText(), 300, 355);
    }
}
