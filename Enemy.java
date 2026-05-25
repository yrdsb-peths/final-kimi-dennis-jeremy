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

    int attackCooldown = 0;
    static final int ATTACK_INTERVAL = 60;
    static final int ATTACK_DAMAGE = 5;

    public Enemy(double worldX, double worldY)
    {
        this.worldX = worldX;
        this.worldY = worldY;
    }

    public void act()
    {
        followPlayer();
        attackPlayer();
    }

    public void followPlayer()
    {
        GameWorld gw = (GameWorld)getWorld();
        double dx = gw.aureaSolvine.worldX - worldX;
        double dy = gw.aureaSolvine.worldY - worldY;
        double dist = Math.sqrt(dx*dx + dy*dy);
        if(dist > 0)
        {
            worldX += dx / dist * speed;
            worldY += dy / dist * speed;
        }
    }

    public void attackPlayer()
    {
        GameWorld gw = (GameWorld)getWorld();
        AureaSolvine p = gw.aureaSolvine;
        attackCooldown++;
        if(attackCooldown >= ATTACK_INTERVAL)
        {
            double dx = p.worldX - worldX;
            double dy = p.worldY - worldY;
            if(Math.sqrt(dx*dx + dy*dy) < 30)
            {
                p.hp -= ATTACK_DAMAGE;
                attackCooldown = 0;
            }
        }
    }

    public boolean takeDamage(int damage)
    {
        hp -= damage;
        return hp <= 0;
    }
}