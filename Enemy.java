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

        double dx = gw.getPlayerWorldX() - worldX;
        double dy = gw.getPlayerWorldY() - worldY;

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
            gw.givePlayerReward(xpDrop, coinDrop);
            getWorld().removeObject(this);
        }
    }

    public void touchPlayer()
    {
        GameWorld gw = (GameWorld)getWorld();

        double dx = gw.getPlayerWorldX() - worldX;
        double dy = gw.getPlayerWorldY() - worldY;

        double dist = Math.sqrt(dx * dx + dy * dy);

        if(dist < 40 && damageTimer.millisElapsed() > 1000)
        {
            gw.damagePlayer(5);
            damageTimer.mark();
        }
    }
}