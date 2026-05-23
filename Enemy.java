import greenfoot.*;

public class Enemy extends Actor
{
    int speed = 2;
    int hp = 100;
    public void act()
    {
        followPlayer();
    }

    public void followPlayer()
    {
        GameWorld gameworld = (GameWorld)getWorld();

        AureaSolvine aureaSolvine = gameworld.aureaSolvine;

        turnTowards(aureaSolvine.getX(), aureaSolvine.getY());

        move(speed);
    }
    
    
}
