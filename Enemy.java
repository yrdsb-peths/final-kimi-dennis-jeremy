import greenfoot.*;

public class Enemy extends Actor
{
    public int speed;
    public int hp;
    public int maxHp;
    public int xpDrop;
    public int coinDrop;
    public int attackDamage;

    public double worldX;
    public double worldY;

    int attackCooldown = 0;
    static final int ATTACK_INTERVAL = 60;

    public Enemy(double worldX, double worldY)
    {
        this(worldX, worldY, 1);
    }

    public Enemy(double worldX, double worldY, int round)
    {
        this.worldX = worldX;
        this.worldY = worldY;

        speed = 2 + round / 5;
        hp = 30 + (round - 1) * 8;
        maxHp = hp;
        attackDamage = 5 + (round - 1) * 2;
        xpDrop = 3 + round / 3;
        coinDrop = 2 + round / 5;
    }

    public void act()
    {
        if(getWorld() == null)
        {
            return;
        }

        if(getWorld() instanceof GameWorld)
        {
            followGameWorldPlayer();
            attackGameWorldPlayer();
        }
        else if(getWorld() instanceof MyWorld)
        {
            followMyWorldPlayer();
            attackMyWorldPlayer();
        }
    }

    private void followGameWorldPlayer()
    {
        GameWorld world = (GameWorld)getWorld();

        double dx = world.player.worldX - worldX;
        double dy = world.player.worldY - worldY;
        double dist = Math.sqrt(dx * dx + dy * dy);

        if(dist > 0)
        {
            worldX += dx / dist * speed;
            worldY += dy / dist * speed;
        }
    }

    private void attackGameWorldPlayer()
    {
        GameWorld world = (GameWorld)getWorld();

        double dx = world.player.worldX - worldX;
        double dy = world.player.worldY - worldY;
        double dist = Math.sqrt(dx * dx + dy * dy);

        attackCooldown++;

        if(dist < 40 && attackCooldown >= ATTACK_INTERVAL)
        {
            world.player.takeHit(attackDamage);
            attackCooldown = 0;
        }
    }

    private void followMyWorldPlayer()
    {
        MyWorld world = (MyWorld)getWorld();

        Actor player = null;

        if(world.leon != null)
        {
            player = world.leon;
        }
        else if(world.kaine != null)
        {
            player = world.kaine;
        }
        else if(world.aurea != null)
        {
            player = world.aurea;
        }

        if(player == null)
        {
            return;
        }

        int dx = player.getX() - getX();
        int dy = player.getY() - getY();
        double dist = Math.sqrt(dx * dx + dy * dy);

        if(dist > 0)
        {
            int newX = getX() + (int)(dx / dist * speed);
            int newY = getY() + (int)(dy / dist * speed);
            setLocation(newX, newY);

            worldX = newX;
            worldY = newY;
        }
    }

    private void attackMyWorldPlayer()
    {
        MyWorld world = (MyWorld)getWorld();

        Actor player = null;

        if(world.leon != null)
        {
            player = world.leon;
        }
        else if(world.kaine != null)
        {
            player = world.kaine;
        }
        else if(world.aurea != null)
        {
            player = world.aurea;
        }

        if(player == null)
        {
            return;
        }

        int dx = player.getX() - getX();
        int dy = player.getY() - getY();
        double dist = Math.sqrt(dx * dx + dy * dy);

        attackCooldown++;

        if(dist < 40 && attackCooldown >= ATTACK_INTERVAL)
        {
            world.damageSelectedPlayer(attackDamage);
            attackCooldown = 0;
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
            {
                getWorld().removeObject(this);
            }
        }

        return died;
    }
}