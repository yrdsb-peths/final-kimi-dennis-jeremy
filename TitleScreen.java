import greenfoot.*;

public class TitleScreen extends World
{
    public TitleScreen()
    {
        super(1500, 750, 1);

        addObject(new Label("CHOOSE YOUR HERO", 80), 750, 120);

        addObject(new Label("AUREA SOLVINE", 50), 350, 300);
        addObject(new Label("Press A", 40), 350, 380);

        addObject(new Label("LEON CLOVIS", 50), 750, 300);
        addObject(new Label("Press L", 40), 750, 380);

        addObject(new Label("KAINE VELSARTH", 50), 1150, 300);
        addObject(new Label("Press K", 40), 1150, 380);
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
            Greenfoot.setWorld(new MyWorld("kaine"));
        }
    }
}