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

        worldX = gw.getPlayerWorldX();
        worldY = gw.getPlayerWorldY();

        if(hitTimer.millisElapsed() > 500)
        {
            hitEnemies();
            hitTimer.mark();
        }
    }

    public void hitEnemies()
    {
        for(Enemy e : getWorld().getObjects(Enemy.class))
        {
            double dx = e.worldX - worldX;
            double dy = e.worldY - worldY;

            if(Math.sqrt(dx * dx + dy * dy) < size / 2)
            {
                e.takeDamage(damage);
            }
        }
    }
}