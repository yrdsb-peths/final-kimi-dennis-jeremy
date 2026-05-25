import greenfoot.*;

public class MyWorld extends World {
    public MyWorld() {
        super(600, 400, 1);
        
        LeonClovis leon = new LeonClovis();
        addObject(leon, 400, 300);

        Enemy enemy1 = new Enemy();
        addObject(enemy1, 600, 300);

        Enemy enemy2 = new Enemy();
        addObject(enemy2, 200, 200);

        Enemy enemy3 = new Enemy();
        addObject(enemy3, 500, 500);
        
    }
}
