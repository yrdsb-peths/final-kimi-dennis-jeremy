import greenfoot.*;
public class Bullet extends Actor
{
    Enemy target;
    LeonClovis player;
    int speed = 8;
    int damage = 10;
    public Bullet(Enemy enemy, LeonClovis leon)
    {
        target = enemy;
        player = leon;
    
        damage = leon.gunDamage;
    
        GreenfootImage img = new GreenfootImage(40,8);
    
        img.setColor(Color.WHITE);
        img.fillRect(0,0,40,8);
    
        img.setColor(Color.RED);
        img.fillRect(2,2,36,4);
    
        setImage(img);
    }
    public void act()
    {
        if(getWorld() == null)
        {
            return;
        }
        followEnemy();
        if(getWorld() == null)
        {
            return;
        }
        hitEnemy();
        if(getWorld() == null)
        {
            return;
        }
        removeAtEdge();
    }
    public void followEnemy()
    {
        if(target != null && target.getWorld() != null)
        {
            turnTowards(target.getX(), target.getY());
            move(speed);
        }
        else
        {
            getWorld().removeObject(this);
        }
    }
    public void hitEnemy()
    {
        Enemy enemy = (Enemy)getOneIntersectingObject(Enemy.class);
        if(enemy != null)
        {
            MyWorld gw = (MyWorld)getWorld();
            getWorld().addObject(new HitEffect(), getX(), getY());
            boolean died = enemy.takeDamage(damage);
            if(died)
            {
                player.addReward();
                if(enemy.getWorld() != null){
                    gw.removeObject(enemy);
                }
            }
            if(getWorld() != null){
                getWorld().removeObject(this);
            }
        }
    }
    public void removeAtEdge()
    {
        if(isAtEdge())
        {
            getWorld().removeObject(this);
        }
    }
}