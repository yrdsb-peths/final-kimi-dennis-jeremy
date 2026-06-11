import greenfoot.*;

public class EndScreen extends World
{
    boolean victory;
    String heroType;
    int roundsSurvived;
    int enemiesKilled;

    int hp, maxHp, xp, coin, level, speed, stamina, power;
    int fireballLevel, lightningLevel, iceWaveLevel, gunLevel, swordLevel;

    public EndScreen(
        boolean victory, String heroType,
        int roundsSurvived, int enemiesKilled,
        int hp, int maxHp, int xp, int coin,
        int level, int speed, int stamina, int power,
        int fireballLevel, int lightningLevel, int iceWaveLevel,
        int gunLevel, int swordLevel)
    {
        super(1500, 750, 1);

        this.victory = victory;
        this.heroType = heroType;
        this.roundsSurvived = roundsSurvived;
        this.enemiesKilled = enemiesKilled;
        this.hp = hp;
        this.maxHp = maxHp;
        this.xp = xp;
        this.coin = coin;
        this.level = level;
        this.speed = speed;
        this.stamina = stamina;
        this.power = power;
        this.fireballLevel = fireballLevel;
        this.lightningLevel = lightningLevel;
        this.iceWaveLevel = iceWaveLevel;
        this.gunLevel = gunLevel;
        this.swordLevel = swordLevel;

        drawContent();
    }

    public void act()
    {
        String key = Greenfoot.getKey();

        if(key != null
            && (key.equalsIgnoreCase("space")
                || key.equals(" ")
                || key.equalsIgnoreCase("escape")))
        {
            Greenfoot.setWorld(new TitleScreen());
        }
    }

    private void drawContent()
    {
        GreenfootImage bg = getBackground();
        bg.clear();
        bg.setColor(new Color(20, 20, 40));
        bg.fillRect(0, 0, getWidth(), getHeight());

        bg.setFont(new Font("Arial", true, false, 44));
        bg.setColor(victory ? new Color(120, 255, 140) : new Color(255, 90, 90));
        drawCenteredText(bg, victory ? "VICTORY!" : "GAME OVER", getWidth() / 2, 70);

        bg.setFont(new Font("Arial", false, false, 24));
        bg.setColor(new Color(200, 200, 255));
        drawCenteredText(bg, heroName(), getWidth() / 2, 115);

        int leftX = 120;
        int rightX = 780;
        int y = 175;
        int line = 32;

        drawSection(bg, leftX, y, "Run Summary");
        y += line;
        drawBody(bg, leftX, y, "Rounds Survived: " + roundsSurvived + " / 30");
        y += line;
        drawBody(bg, leftX, y, "Enemies Defeated: " + enemiesKilled);
        y += line * 2;

        drawSection(bg, leftX, y, "Final Attributes");
        y += line;
        drawBody(bg, leftX, y, "Level:   " + level);
        y += line;
        drawBody(bg, leftX, y, "HP:      " + hp + " / " + maxHp);
        y += line;
        drawBody(bg, leftX, y, "Power:   " + power);
        y += line;
        drawBody(bg, leftX, y, "Speed:   " + speed);
        y += line;
        drawBody(bg, leftX, y, "Stamina: " + stamina);
        y += line;
        drawBody(bg, leftX, y, "Coins:   " + coin);
        y += line;
        drawBody(bg, leftX, y, "XP:      " + xp);

        y = 175;
        drawSection(bg, rightX, y, "Weapons");
        y += line;
        drawWeaponLine(bg, rightX, y, "Fireball", fireballLevel);
        y += line;
        drawWeaponLine(bg, rightX, y, "Lightning", lightningLevel);
        y += line;
        drawWeaponLine(bg, rightX, y, "Ice Wave", iceWaveLevel);
        y += line;
        drawWeaponLine(bg, rightX, y, HeroData.signatureWeaponName(heroType), getSignatureWeaponLevel());

        bg.setFont(new Font("Arial", true, false, 24));
        bg.setColor(new Color(255, 220, 80));
        drawCenteredText(bg, "Press SPACE to return to title screen", getWidth() / 2, 700);
    }

    private String heroName()
    {
        if("leon".equals(heroType))
        {
            return "Leon Clovis";
        }

        if("kaine".equals(heroType))
        {
            return "Kaine Velsarth";
        }

        return "Aurea Solvine";
    }

    private int getSignatureWeaponLevel()
    {
        if("leon".equals(heroType))
        {
            return gunLevel;
        }

        if("kaine".equals(heroType))
        {
            return swordLevel;
        }

        return lightningLevel;
    }

    private void drawSection(GreenfootImage bg, int x, int y, String text)
    {
        bg.setFont(new Font("Arial", true, false, 24));
        bg.setColor(new Color(200, 200, 255));
        bg.drawString(text, x, y);
    }

    private void drawBody(GreenfootImage bg, int x, int y, String text)
    {
        bg.setFont(new Font("Arial", false, false, 22));
        bg.setColor(Color.WHITE);
        bg.drawString(text, x, y);
    }

    private void drawWeaponLine(GreenfootImage bg, int x, int y, String name, int level)
    {
        bg.setFont(new Font("Arial", false, false, 22));
        bg.setColor(Color.WHITE);

        if(level == 0)
        {
            bg.drawString(name + ": Not purchased", x, y);
        }
        else
        {
            bg.drawString(name + ": Lv." + level, x, y);
        }
    }

    private void drawCenteredText(GreenfootImage bg, String text, int cx, int y)
    {
        int approxW = text.length() * 16;
        bg.drawString(text, cx - approxW / 2, y);
    }
}
