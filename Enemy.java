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
        speed        = 2  + round / 5;
        hp           = 30 + (round - 1) * 8;
        maxHp        = hp;
        attackDamage = 5  + (round - 1) * 2;
        xpDrop       = 3  + round / 3;
        coinDrop     = 2  + round / 5;
    }

    public void act()
    {
        if(getWorld() == null)
        {
            return;
        }

        followPlayer();

        if(getWorld() == null)
        {
            return;
        }

        touchPlayer();
    }

    public void followPlayer()
    {
        if(getWorld() == null) return;
        GameWorld gw = (GameWorld)getWorld();
        double dx = gw.player.worldX - worldX;
        double dy = gw.player.worldY - worldY;
        double dist = Math.sqrt(dx*dx + dy*dy);
        if(dist > 0)
        {
            GameWorld world = (GameWorld)currentWorld;

            moveToward(
                world.getPlayerWorldX(),
                world.getPlayerWorldY()
            );

            return;
        }

        if(currentWorld instanceof MyWorld)
        {
            MyWorld world = (MyWorld)currentWorld;

            Actor player =
                world.leon != null
                ? world.leon
                : world.kaine;

            if(player == null)
            {
                player = world.aurea;
            }

            if(player != null)
            {
                moveToward(
                    player.getX(),
                    player.getY()
                );
                setLocation((int)worldX, (int)worldY);
            }
        }
    }

    private void moveToward(
        double targetX,
        double targetY)
    {
        if(getWorld() == null) return;
        GameWorld gw = (GameWorld)getWorld();
        Hero p = gw.player;
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

    public void touchPlayer()
    {
        World currentWorld = getWorld();

        if(currentWorld instanceof GameWorld)
        {
            damageGameWorldPlayer(
                (GameWorld)currentWorld
            );

            return;
        }

        if(currentWorld instanceof MyWorld)
        {
            damageSelectedPlayerOnTouch(
                (MyWorld)currentWorld
            );
        }
    }

    private void damageGameWorldPlayer(
        GameWorld world)
    {
        double dx =
            world.getPlayerWorldX()
            - worldX;

        double dy =
            world.getPlayerWorldY()
            - worldY;

        double distance =
            Math.sqrt(dx * dx + dy * dy);

        if(distance < 40
            && damageTimer.millisElapsed() > 1000)
        {
            world.damagePlayer(damage);
            damageTimer.mark();
        }
    }

    private void damageSelectedPlayerOnTouch(
        MyWorld world)
    {
        if(world.leon != null
            && isCloseTo(world.leon))
        {
            damageSelectedPlayer(world);
        }
        else if(world.kaine != null
            && isCloseTo(world.kaine))
        {
            damageSelectedPlayer(world);
        }
        else if(world.aurea != null && isCloseTo(world.aurea))
        {
            damageSelectedPlayer(world);
        }
    }

    private boolean isCloseTo(Actor actor)
    {
        int dx = getX() - actor.getX();
        int dy = getY() - actor.getY();
        return Math.sqrt(dx * dx + dy * dy) < TOUCH_DAMAGE_RANGE;
    }

    private void damageSelectedPlayer(
        MyWorld world)
    {
        if(damageTimer.millisElapsed() > 1000)
        {
            world.damageSelectedPlayer(damage);
            damageTimer.mark();
        }
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
