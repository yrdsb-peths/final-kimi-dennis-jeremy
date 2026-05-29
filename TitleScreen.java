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
        if(Greenfoot.isKeyDown("k"))
        {
            Greenfoot.setWorld(new GameWorld("kaine"));
        }
        if(Greenfoot.isKeyDown("l"))
        {
            Greenfoot.setWorld(new GameWorld("leon"));
        }
    }

    private void drawUI()
    {
        GreenfootImage bg = getBackground();
        bg.setColor(new Color(10, 10, 30));
        bg.fillRect(0, 0, getWidth(), getHeight());

        bg.setColor(Color.WHITE);
        bg.setFont(new Font("Arial", true, false, 48));
        bg.drawString("CHOOSE YOUR HERO", 450, 150);

        bg.setFont(new Font("Arial", false, false, 28));
        bg.setColor(new Color(255, 180, 80));
        bg.drawString("[ K ]  Kaine Velsarth", 500, 320);
        bg.setColor(new Color(100, 180, 255));
        bg.drawString("[ L ]  Leon Clovis  /  Aurea Solvine", 500, 390);

        bg.setColor(new Color(160, 160, 160));
        bg.setFont(new Font("Arial", false, false, 20));
        bg.drawString("Press the key to start", 620, 500);
    }
}
