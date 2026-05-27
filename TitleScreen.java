import greenfoot.*;

public class TitleScreen extends World
{
    public TitleScreen()
    {
        super(1500, 750, 1);

        showText("Choose Your Character", 750, 250);
        showText("Press A for Aurea Solvine", 750, 330);
        showText("Press L for Leon Clovis", 750, 380);
        showText("Click the screen first, then press a key", 750, 450);
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

        if(key.equalsIgnoreCase("l"))
        {
            Greenfoot.setWorld(new GameWorld("leon"));
        }
    }
}