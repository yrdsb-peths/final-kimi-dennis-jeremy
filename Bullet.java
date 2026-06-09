import greenfoot.*;

public class Bullet extends Actor
{
    Enemy target;
    Hero player;

    int speed = 8;
    int damage = 10;

    public Bullet(Enemy enemy, Hero shooter, int damage)
    {
        target = enemy;
        player = shooter;
        this.damage = damage;

        GreenfootImage img = new GreenfootImage(40, 8);
        img.setColor(Color.WHITE);
        img.fillRect(0, 0, 40, 8);
        img.setColor(Color.RED);
        img.fillRect(2, 2, 36, 4);
        setImage(img);
    }

    public void act()
    {
        if(getWorld() == null) return;

        followEnemy();
        if(getWorld() == null) return;

        hitEnemy();
        if(getWorld() == null) return;

        removeAtEdge();
    }

    public void followEnemy()
    {
        if(target != null && target.getWorld() != null)
        {
            turnTowards(target.getX(), target.getY());
            move(speed);
        }
        else if(getWorld() != null)
        {
            getWorld().removeObject(this);
        }
    }

    public void hitEnemy()
    {
        Enemy enemy = (Enemy)getOneIntersectingObject(Enemy.class);
        if(enemy == null) return;

        if(getWorld() != null)
            getWorld().addObject(new HitEffect(), getX(), getY());

        World world = getWorld();

        if(world instanceof GameWorld)
        {
            enemy.takeDamage(damage);
        }
        else
        {
            enemy.takeDamage(damage, player);
        }

        if(getWorld() != null)
        {
            getWorld().removeObject(this);
        }
    }

    public void removeAtEdge()
    {
        if(isAtEdge() && getWorld() != null)
            getWorld().removeObject(this);
    }
}
