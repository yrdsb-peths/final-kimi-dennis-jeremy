import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Bullet here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Bullet extends Actor
{
    /**
     * Act - do whatever the Bullet wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    Enemy target;
    
    int speed = 8;
    
    public Bullet(Enemy enemy)
    {
        target = enemy;

        GreenfootImage img = new GreenfootImage(10,10);
        img.fillOval(0,0,10,10);
        setImage(img);
    }
    
    public void act()
    {
        // Add your action code here.
        followEnemy();
        hitEnemy();
        removeAtEdge();
    }
    
    public void followEnemy()
    {
        if(target != null)
        {
            turnTowards(target.getX(), target.getY());
            move(speed);
        }
    }

    public void hitEnemy()
    {
        Enemy enemy = (Enemy)getOneIntersectingObject(Enemy.class);

        if(enemy != null)
        {
            getWorld().removeObject(enemy);
            getWorld().removeObject(this);
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
