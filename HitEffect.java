import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class HitEffect here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class HitEffect extends Actor
{
    /**
     * Act - do whatever the HitEffect wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    int timer = 10;
    
    public HitEffect()
    {
        GreenfootImage img = new GreenfootImage(20, 20);
        img.setColor(Color.ORANGE);
        img.fillOval(0,0,20,20);
        
        setImage(img);
    }
    
    public void act()
    {
        // Add your action code here.
        timer--;
        
        getImage().setTransparency(timer * 25);
        
        if(timer <= 0)
        {
            getWorld().removeObject(this);
        }
    }
}
