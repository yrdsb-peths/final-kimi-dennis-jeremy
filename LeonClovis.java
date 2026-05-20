import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class LeonClovis here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class LeonClovis extends Actor
{
    int speed = 4;
    
    GreenfootImage[] leonRight = new GreenfootImage[4];  
    GreenfootImage[] leonLeft = new GreenfootImage[4];
    String facing = "right";
    SimpleTimer animationTimer = new SimpleTimer();

    /**
     * Act - do whatever the LeonClovis wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    
    public LeonClovis()
    {
        for(int i = 0; i < leonRight.length; i++)
        {
            leonRight[i]= new GreenfootImage("images/leon_move/leon" + i + ".png");
            leonRight[i].scale(100,100);
        }
        
        for(int i = 0; i < leonLeft.length; i++)
        {
            leonLeft[i]= new GreenfootImage("images/leon_move/leon" + i + ".png");
            leonLeft[i].mirrorHorizontally();
            leonLeft[i].scale(90,75);
        }
    }
    int imageIndex = 0;
    public void animateLeon()
    {   
        if(animationTimer.millisElapsed() < 100)
        {
            return;
        }
        animationTimer.mark();
        if(facing.equals("right"))
        {
            setImage(leonRight[imageIndex]);
            imageIndex = (imageIndex + 1) % leonRight.length;
        }
        else
        {
            setImage(leonLeft[imageIndex]);
            imageIndex = (imageIndex + 1) % leonLeft.length;
        }
    }
    
    public void act()
    {
        // Add your action code here.
        movePlayer();
        animateLeon();
    }
    
    public void movePlayer()
    {
        if (Greenfoot.isKeyDown("w"))
        {
            setLocation(getX(), getY() - speed);
        }

        if (Greenfoot.isKeyDown("s"))
        {
            setLocation(getX(), getY() + speed);
        }

        if (Greenfoot.isKeyDown("a"))
        {
            setLocation(getX() - speed, getY());
            facing = "left";
        }

        if (Greenfoot.isKeyDown("d"))
        {
            setLocation(getX() + speed, getY());
            facing = "right";
        }
    }   
}