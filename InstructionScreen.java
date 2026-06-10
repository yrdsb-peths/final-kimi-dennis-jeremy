import greenfoot.*;

public class InstructionScreen extends World
{
    public InstructionScreen()
    {
        super(1500, 750, 1);

        addObject(new Label("HOW TO PLAY", 80), 750, 80);

        addObject(new Label("Hero Info", 35), 750, 180);

        addObject(new Label("A = Aurea (Magic)", 35), 750, 240);
        addObject(new Label("L = Leon (Gun)", 35), 750, 290);
        addObject(new Label("K = Kaine (Sword)", 35), 750, 340);

        addObject(new Label("W A S D = Move", 35), 750, 420);

        addObject(new Label("Defeat enemies to gain XP and Coins", 35), 750, 470);

        addObject(new Label("Level up to become stronger", 35), 750, 520);

        addObject(new Label("Survive as many waves as possible", 35), 750, 570);

        addObject(new Label("Press [B] to return", 35), 750, 680);
    }

    public void act()
    {
        String key = Greenfoot.getKey();

        if(key == null)
        {
            return;
        }

        if(key.equalsIgnoreCase("b"))
        {
            Greenfoot.setWorld(new TitleScreen());
        }
    }
}