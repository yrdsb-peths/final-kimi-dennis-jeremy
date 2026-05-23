import greenfoot.*;

public class Enemy extends Actor
{
    int speed = 2;
    public int hp = 30;
    public int maxHp = 30;

    int attackCooldown = 0;
    static final int ATTACK_INTERVAL = 60; 
    static final int ATTACK_DAMAGE = 5;

    public int xpDrop = 3;
    public int coinDrop = 2;

    public void act()
    {
        followPlayer();
        attackPlayer();
    }

    public void followPlayer()
    {
        GameWorld gameworld = (GameWorld)getWorld();
        AureaSolvine player = gameworld.aureaSolvine;
        turnTowards(player.getX(), player.getY());
        move(speed);
    }

    public void attackPlayer()
    {
        GameWorld gameworld = (GameWorld)getWorld();
        AureaSolvine player = gameworld.aureaSolvine;

        attackCooldown++;

        
        if(attackCooldown >= ATTACK_INTERVAL)
        {
            AureaSolvine hit =
                (AureaSolvine)getOneIntersectingObject(AureaSolvine.class);

            if(hit != null)
            {
                hit.hp -= ATTACK_DAMAGE;
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
