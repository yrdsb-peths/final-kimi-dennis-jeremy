import greenfoot.*;

public class MyWorld extends World {
    public MyWorld() {
        super(600, 400, 1);
        
        LeonClovis hero = new LeonClovis();
        addObject(hero, 300, 200);
    }
}
