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

        // Stats scale with round number
        speed        = 2 + round / 5;
        hp           = 30 + (round - 1) * 8;
        maxHp        = hp;
        attackDamage = 5 + (round - 1) * 2;
        xpDrop       = 3 + round / 3;
        coinDrop     = 2 + round / 5;
    }

    public void act()
    {
        followPlayer();
        attackPlayer();
    }

    public void followPlayer()
    {
        if(getWorld() == null) return;

        if(getWorld() instanceof GameWorld)
        {
            GameWorld gw = (GameWorld) getWorld();
            double dx = gw.player.worldX - worldX;
            double dy = gw.player.worldY - worldY;
            double dist = Math.sqrt(dx * dx + dy * dy);
            if(dist > 0)
            {
                worldX += dx / dist * speed;
                worldY += dy / dist * speed;
            }
            return;
        }

        if(getWorld() instanceof MyWorld)
        {
            MyWorld world = (MyWorld) getWorld();
            Actor target = world.leon != null ? world.leon
                : (world.kaine != null ? world.kaine : world.aurea);
            if(target != null)
            {
                turnTowards(target.getX(), target.getY());
                move(speed);
            }
        }
    }

    public void attackPlayer()
    {
        if(getWorld() == null) return;

        if(getWorld() instanceof GameWorld)
        {
            GameWorld gw = (GameWorld) getWorld();
            Hero p = gw.player;
            attackCooldown++;
            if(attackCooldown >= ATTACK_INTERVAL)
            {
                double dx = p.worldX - worldX;
                double dy = p.worldY - worldY;
                if(Math.sqrt(dx * dx + dy * dy) < 30)
                {
                    p.takeHit(attackDamage);
                    attackCooldown = 0;
                }
            }
        }
    }

    public boolean takeDamage(int damage)
    {
        hp -= damage;
        return hp <= 0;
    }

    public boolean takeDamage(int damage, Hero source)
    {
        boolean died = takeDamage(damage);
        if(died && source != null)
        {
            source.gainXP(xpDrop);
            source.gainCoin(coinDrop);
            if(getWorld() != null)
                getWorld().removeObject(this);
        }
        return died;
    }
}
