import greenfoot.*;

public class MyWorld extends World {
    public MyWorld() {
        super(600, 400, 1);
        
        LeonClovis leon = new LeonClovis();
        addObject(leon, 300, 200);
        
    }
}
