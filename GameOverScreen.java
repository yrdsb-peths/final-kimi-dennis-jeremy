import greenfoot.*;

public class GameOverScreen extends World
{
    public GameOverScreen()
    {
        super(1500, 750, 1);

        addObject(new Label("☠️GAME OVER ☠️", 90), 770, 280);
        addObject(new Label("Press [SPACE] to Restart", 50), 750, 380);
    }

    public void act()
    {
        String key = Greenfoot.getKey();

        if(key == null)
        {
            return;
        }

        if(key.equalsIgnoreCase("space"))
        {
            Greenfoot.setWorld(new TitleScreen());
        }
    }
}