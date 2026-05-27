import greenfoot.*;

public class IceWave extends Actor
{
    public double worldX;
    public double worldY;

    int damage = 10;
    int size = 100;

    SimpleTimer hitTimer = new SimpleTimer();

    public IceWave()
    {
        GreenfootImage img = new GreenfootImage(size, size);
        img.setColor(new Color(100, 200, 255, 100));
        img.fillOval(0, 0, size, size);
        img.setColor(Color.CYAN);
        img.drawOval(0, 0, size - 1, size - 1);
        setImage(img);
    }

    public void act()
    {
        GameWorld gw = (GameWorld)getWorld();

        worldX = gw.aureaSolvine.worldX;
        worldY = gw.aureaSolvine.worldY;

        if(hitTimer.millisElapsed() > 500)
        {
            hitEnemies();
            hitTimer.mark();
        }
    }

    public void hitEnemies()
    {
        java.util.List<Enemy> enemies =
            getWorld().getObjects(Enemy.class);

        for(Enemy e : enemies)
        {
            double dx = e.worldX - worldX;
            double dy = e.worldY - worldY;

            double dist = Math.sqrt(dx * dx + dy * dy);

            if(dist < size / 2)
            {
                e.takeDamage(damage);
            }
        }
    }
}