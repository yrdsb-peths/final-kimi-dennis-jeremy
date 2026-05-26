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
        GreenfootImage img = new GreenfootImage(40, 40);
        img.setColor(Color.RED);
        img.fillOval(0, 0, 40, 40);
        setImage(img);
    }
    public void act()
    {
        if(getWorld() == null) return;
        followPlayer();
        if(getWorld() == null) return;
        touchPlayer();
    }
    public void followPlayer()
    {
        MyWorld gw = (MyWorld)getWorld();
        double playerX = gw.leon != null ? gw.leon.getX() : gw.kaine.getX();
        double playerY = gw.leon != null ? gw.leon.getY() : gw.kaine.getY();
        double dx = playerX - worldX;
        double dy = playerY - worldY;
        double dist = Math.sqrt(dx * dx + dy * dy);
        if(dist > 0)
        {
            worldX += dx / dist * speed;
            worldY += dy / dist * speed;
        }
    }
    public boolean takeDamage(int damage)
    {
        hp -= damage;
        if(hp <= 0)
        {
            return true;
        }
        return false;
    }
    public void touchPlayer()
    {
        MyWorld gw = (MyWorld)getWorld();
        double playerX = gw.leon != null ? gw.leon.getX() : gw.kaine.getX();
        double playerY = gw.leon != null ? gw.leon.getY() : gw.kaine.getY();
        double dx = playerX - worldX;
        double dy = playerY - worldY;
        double dist = Math.sqrt(dx * dx + dy * dy);
        if(dist < 40 && damageTimer.millisElapsed() > 1000)
        {
            if(gw.leon != null)
            {
                gw.leon.takeDamage(5);
            }
            damageTimer.mark();
        }
    }
}