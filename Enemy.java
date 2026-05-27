import greenfoot.*;

public class Enemy extends Actor
{
    int speed = 2;

    public int hp = 30;
    public int maxHp = 30;

    public int xpDrop = 3;
    public int coinDrop = 2;

    public double worldX;
    public double worldY;

    SimpleTimer damageTimer = new SimpleTimer();

    public Enemy(double worldX, double worldY)
    {
        this.worldX = worldX;
        this.worldY = worldY;

        GreenfootImage img = new GreenfootImage("enemy.png");
        img.scale(60, 60);
        setImage(img);
    }

    public void act()
    {
        followPlayer();
        touchPlayer();
    }

    public void followPlayer()
    {
        GameWorld gw = (GameWorld)getWorld();

        double px = 0;
        double py = 0;

        if(gw.aureaSolvine != null)
        {
            px = gw.aureaSolvine.worldX;
            py = gw.aureaSolvine.worldY;
        }

        if(gw.leonClovis != null)
        {
            px = gw.leonClovis.worldX;
            py = gw.leonClovis.worldY;
        }

        double dx = px - worldX;
        double dy = py - worldY;

        double dist = Math.sqrt(dx * dx + dy * dy);

        if(dist > 0)
        {
            worldX += dx / dist * speed;
            worldY += dy / dist * speed;
        }
    }

    public void takeDamage(int damage)
    {
        hp -= damage;

        if(hp <= 0)
        {
            GameWorld gw = (GameWorld)getWorld();

            if(gw.aureaSolvine != null)
            {
                gw.aureaSolvine.xp += xpDrop;
                gw.aureaSolvine.coin += coinDrop;
            }

            if(gw.leonClovis != null)
            {
                gw.leonClovis.xp += xpDrop;
                gw.leonClovis.coin += coinDrop;
            }

            getWorld().removeObject(this);
        }
    }

    public void touchPlayer()
    {
        GameWorld gw = (GameWorld)getWorld();

        double px = 0;
        double py = 0;

        if(gw.aureaSolvine != null)
        {
            px = gw.aureaSolvine.worldX;
            py = gw.aureaSolvine.worldY;
        }

        if(gw.leonClovis != null)
        {
            px = gw.leonClovis.worldX;
            py = gw.leonClovis.worldY;
        }

        double dx = px - worldX;
        double dy = py - worldY;

        double dist = Math.sqrt(dx * dx + dy * dy);

        if(dist < 40 && damageTimer.millisElapsed() > 1000)
        {
            if(gw.aureaSolvine != null)
            {
                gw.aureaSolvine.takeHit(5);
            }

            if(gw.leonClovis != null)
            {
                gw.leonClovis.takeDamage(5);
            }

            damageTimer.mark();
        }
    }
}