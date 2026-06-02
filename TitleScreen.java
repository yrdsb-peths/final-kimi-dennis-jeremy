import greenfoot.*;

public class TitleScreen extends World
{
    public TitleScreen()
    {
        super(1500, 750, 1);
        drawUI();
    }

    public void act()
    {
        String key = Greenfoot.getKey();
        if(key == null) return;

        if(key.equalsIgnoreCase("k"))
        {
            HeroData.heroType = "kaine";
            Greenfoot.setWorld(new GameWorld("kaine"));
        }
        else if(key.equalsIgnoreCase("l"))
        {
            HeroData.heroType = "leon";
            Greenfoot.setWorld(new GameWorld("leon"));
        }
        else if(key.equalsIgnoreCase("a"))
        {
            HeroData.heroType = "aurea";
            Greenfoot.setWorld(new GameWorld("aurea"));
        }
    }

    private void drawUI()
    {
        GreenfootImage bg = getBackground();
        bg.setColor(new Color(10, 10, 30));
        bg.fillRect(0, 0, getWidth(), getHeight());

        bg.setColor(Color.WHITE);
        bg.setFont(new Font("Arial", true, false, 48));
        bg.drawString("CHOOSE YOUR HERO", 400, 120);

        bg.setFont(new Font("Arial", false, false, 32));
        
        bg.setColor(new Color(255, 150, 80));
        bg.drawString("[ K ]  Kaine Velsarth", 450, 280);
        
        bg.setColor(new Color(100, 180, 255));
        bg.drawString("[ L ]  Leon Clovis", 450, 380);
        
        bg.setColor(new Color(200, 100, 255));
        bg.drawString("[ A ]  Aurea Solvine", 450, 480);

        bg.setColor(new Color(160, 160, 160));
        bg.setFont(new Font("Arial", false, false, 20));
        bg.drawString("Aurea: Lightning   Leon: Gun   Kaine: Sword", 420, 560);
        bg.drawString("Press the key to start", 570, 600);
    }
}