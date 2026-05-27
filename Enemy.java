import greenfoot.*;

public class Enemy extends Actor
{
    public int speed;
    public int hp;
    public int maxHp;
    public int xpDrop;
    public int coinDrop;

    public double worldX;
    public double worldY;

    int attackCooldown = 0;
    static final int ATTACK_INTERVAL = 60;
    public int attackDamage;

    public Enemy(double worldX, double worldY, int round)
    {
        this.worldX = worldX;
        this.worldY = worldY;

        // 每轮提升属性
        speed        = 2  + round / 5;
        hp           = 30 + (round - 1) * 8;
        maxHp        = hp;
        attackDamage = 5  + (round - 1) * 2;
        xpDrop       = 3  + round / 3;
        coinDrop     = 2  + round / 5;
    }

    public void act()
    {
        followPlayer();
        attackPlayer();
    }

    public void followPlayer()
    {
        if(getWorld() == null) return;
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
        if(getWorld() == null) return;
        GameWorld gw = (GameWorld)getWorld();
        AureaSolvine p = gw.aureaSolvine;
        attackCooldown++;
        if(attackCooldown >= ATTACK_INTERVAL)
        {
            double dx = p.worldX - worldX;
            double dy = p.worldY - worldY;
            if(Math.sqrt(dx*dx + dy*dy) < 30)
            {
                p.takeHit(attackDamage);
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