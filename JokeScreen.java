import greenfoot.*;

public class JokeScreen extends World
{
    private final int hp, maxHp, xp, coin;
    private final int level, speed, stamina, power, xpToNextLevel;
    private final String heroType;
    private final int fireballLevel, lightningLevel, iceWaveLevel;
    private final int gunLevel, swordLevel, enemiesKilled;

    private int timer = 0;
    private int phase = 0;

    private static final int PHASE0_DURATION = 60;
    private static final int PHASE1_DURATION = 180;
    private static final int PHASE2_DURATION = 120;

    public JokeScreen(
        int hp, int maxHp, int xp, int coin,
        int level, int speed, int stamina, int power,
        int xpToNextLevel, String heroType,
        int fireballLevel, int lightningLevel, int iceWaveLevel,
        int gunLevel, int swordLevel, int enemiesKilled)
    {
        super(1500, 750, 1);

        this.hp = hp;
        this.maxHp = maxHp;
        this.xp = xp;
        this.coin = coin;
        this.level = level;
        this.speed = speed;
        this.stamina = stamina;
        this.power = power;
        this.xpToNextLevel = xpToNextLevel;
        this.heroType = heroType;
        this.fireballLevel = fireballLevel;
        this.lightningLevel = lightningLevel;
        this.iceWaveLevel = iceWaveLevel;
        this.gunLevel = gunLevel;
        this.swordLevel = swordLevel;
        this.enemiesKilled = enemiesKilled;

        drawPhase();
    }

    public void act()
    {
        timer++;

        int duration = phase == 0 ? PHASE0_DURATION
            : phase == 1 ? PHASE1_DURATION
            : PHASE2_DURATION;

        if(timer >= duration)
        {
            timer = 0;
            phase++;

            if(phase > 2)
            {
                launchRound3();
                return;
            }

            drawPhase();
        }
    }

    private void drawPhase()
    {
        GreenfootImage bg = getBackground();
        bg.setColor(Color.BLACK);
        bg.fillRect(0, 0, getWidth(), getHeight());

        if(phase == 0)
        {
            bg.setFont(new Font("Courier New", true, false, 52));
            bg.setColor(new Color(220, 30, 30));
            drawCentered(bg, "FATAL ERROR", getWidth() / 2, 260);

            bg.setFont(new Font("Courier New", false, false, 22));
            bg.setColor(new Color(200, 200, 200));
            drawCentered(bg, "0x00000000DEADBEEF  UNEXPECTED_KERNEL_EXCEPTION", getWidth() / 2, 340);
            drawCentered(bg, "Collecting error data (100%)...", getWidth() / 2, 380);

            bg.setFont(new Font("Courier New", false, false, 18));
            bg.setColor(new Color(140, 140, 140));
            drawCentered(bg, "Your game will restart.", getWidth() / 2, 430);
        }
        else if(phase == 2)
        {
            bg.setFont(new Font("Arial", true, false, 64));
            bg.setColor(new Color(120, 220, 120));
            drawCentered(bg, "Just kidding  :)", getWidth() / 2, 320);

            bg.setFont(new Font("Arial", false, false, 26));
            bg.setColor(new Color(180, 180, 180));
            drawCentered(bg, "Round 3 starting now...", getWidth() / 2, 410);
        }
    }

    private void launchRound3()
    {
        GameWorld gw = new GameWorld(
            hp, maxHp, xp, coin,
            level, speed, stamina, power,
            xpToNextLevel, 3,
            heroType,
            fireballLevel, lightningLevel, iceWaveLevel,
            gunLevel, swordLevel,
            enemiesKilled
        );

        Greenfoot.setWorld(gw);
    }

    private void drawCentered(GreenfootImage bg, String text, int cx, int y)
    {
        int approxW = text.length() * 16;
        bg.drawString(text, cx - approxW / 2, y);
    }
}