import greenfoot.*;
/**
 * The TitleScreen class displays the main title screen and allows the player to start the game.
 * 
 * @author Denis
 * @version June 2026
 */
public class TitleScreen extends World
{
    public TitleScreen()
    {
        super(1500, 750, 1);
        GreenfootImage bg = new GreenfootImage("Titlescreen.png");
        bg.scale(1500, 750);
        
        setBackground(bg);
    }

    public void act()
    {
        String key = Greenfoot.getKey();
    
        if(key == null)
        {
            return;
        }
    
        if(key.equalsIgnoreCase("a"))
        {
            Greenfoot.setWorld(new GameWorld("aurea"));
        }
        else if(key.equalsIgnoreCase("l"))
        {
            Greenfoot.setWorld(new GameWorld("leon"));
        }
        else if(key.equalsIgnoreCase("k"))
        {
            Greenfoot.setWorld(new GameWorld("kaine"));
        }
        else if(key.equalsIgnoreCase("i"))
        {
            Greenfoot.setWorld(new IntroductionScreen());
        }
    }
}