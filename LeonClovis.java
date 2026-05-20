import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class LeonClovis here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class LeonClovis extends Actor
{
    /**
     * Act - do whatever the LeonClovis wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act()
    {
        // Add your action code here.
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
        }

        if (Greenfoot.isKeyDown("d"))
        {
            setLocation(getX() + speed, getY());
        }
    }   
}