import greenfoot.*;

public class UpgradeScreen extends World
{
    int hp, maxHp, xp, coin, level, speed, stamina, power;
    int xpToNextLevel;
    int attributePoints; 
    int nextRound;       

    int fireballLevel;
    int lightningLevel;
    int iceWaveLevel;

    
    static final int WEAPON_BUY_COST    = 10;
    static final int WEAPON_UPGRADE_COST = 15;

    
    String message = "";
    int messageTimer = 0;

    public UpgradeScreen(
        int hp, int maxHp, int xp, int coin,
        int level, int speed, int stamina, int power,
        int xpToNextLevel, int attributePoints,
        int nextRound,
        int fireballLevel, int lightningLevel, int iceWaveLevel)
    {
        super(1500, 750, 1);
        this.hp             = hp;
        this.maxHp          = maxHp;
        this.xp             = xp;
        this.coin           = coin;
        this.level          = level;
        this.speed          = speed;
        this.stamina        = stamina;
        this.power          = power;
        this.xpToNextLevel  = xpToNextLevel;
        this.attributePoints = attributePoints;
        this.nextRound      = nextRound;
        this.fireballLevel  = fireballLevel;
        this.lightningLevel = lightningLevel;
        this.iceWaveLevel   = iceWaveLevel;

        drawUI();
    }

    public void act()
    {
        handleInput();

        if(messageTimer > 0)
        {
            messageTimer--;
            if(messageTimer == 0) message = "";
        }

        drawUI();
    }

    private void handleInput()
    {
        
        if(attributePoints > 0)
        {
            if(Greenfoot.isKeyDown("1")) { power++;   attributePoints--; showMsg("Power +1"); waitRelease(); }
            if(Greenfoot.isKeyDown("2")) { speed++;   attributePoints--; showMsg("Speed +1"); waitRelease(); }
            if(Greenfoot.isKeyDown("3")) { stamina++; maxHp += 10; hp = Math.min(hp + 10, maxHp); attributePoints--; showMsg("Stamina +1, MaxHP +10"); waitRelease(); }
        }

        
        if(Greenfoot.isKeyDown("q")) { buyOrUpgrade("fireball");  waitRelease(); }
        if(Greenfoot.isKeyDown("w")) { buyOrUpgrade("lightning"); waitRelease(); }
        if(Greenfoot.isKeyDown("e")) { buyOrUpgrade("icewave");   waitRelease(); }

        
        if(Greenfoot.isKeyDown("space"))
        {
            startNextRound();
        }
    }

    private void buyOrUpgrade(String weapon)
    {
        int level = getWeaponLevel(weapon);
        int cost  = level == 0 ? WEAPON_BUY_COST : WEAPON_UPGRADE_COST;

        if(coin < cost)
        {
            showMsg("Not enough coin. Need " + cost + " $");
            return;
        }

        coin -= cost;
        setWeaponLevel(weapon, level + 1);
        showMsg(weapon + (level == 0 ? " Purchased！" : " Uplevel to Lv." + (level+1)));
    }

    private int getWeaponLevel(String w)
    {
        if(w.equals("fireball"))  return fireballLevel;
        if(w.equals("lightning")) return lightningLevel;
        return iceWaveLevel;
    }

    private void setWeaponLevel(String w, int lv)
    {
        if(w.equals("fireball"))  fireballLevel  = lv;
        if(w.equals("lightning")) lightningLevel = lv;
        if(w.equals("icewave"))   iceWaveLevel   = lv;
    }

    private void showMsg(String msg)
    {
        message      = msg;
        messageTimer = 90;
    }

    
    private void waitRelease()
    {
        try { Thread.sleep(150); } catch(Exception e) {}
    }

    private void startNextRound()
    {
        if(nextRound > 30)
        {
            Greenfoot.setWorld(new TitleScreen());
            return;
        }

        GameWorld gw = new GameWorld(
            hp, maxHp, xp, coin,
            level, speed, stamina, power,
            xpToNextLevel,
            nextRound,
            fireballLevel, lightningLevel, iceWaveLevel
        );
        Greenfoot.setWorld(gw);
    }

    private void drawUI()
    {
        GreenfootImage bg = getBackground();
        bg.clear();
        bg.setColor(new Color(20, 20, 40));
        bg.fillRect(0, 0, getWidth(), getHeight());
        bg.setColor(Color.WHITE);
        bg.setFont(new Font("Arial", true, false, 36));
        drawCenteredText(bg, "Round " + (nextRound-1) + " end ", getWidth()/2, 60);

        if(nextRound > 30)
            drawCenteredText(bg, "Congratulations！Press blank to the titlescreen.", getWidth()/2, 120);
        else
            drawCenteredText(bg, "When you're ready, press the spacebar to enter the round" + nextRound, getWidth()/2, 120);

        
        bg.setFont(new Font("Arial", false, false, 22));
        bg.setColor(new Color(200, 200, 255));
        int sx = 80, sy = 180;
        bg.drawString("── Player ──", sx, sy);
        bg.setColor(Color.WHITE);
        bg.drawString("HP:      " + hp + " / " + maxHp,        sx, sy+35);
        bg.drawString("Level:    Lv." + level,                   sx, sy+65);
        bg.drawString("XP:      " + xp + " / " + xpToNextLevel, sx, sy+95);
        bg.drawString("Coin:    " + coin,                       sx, sy+125);
        bg.drawString("Speed:   " + speed,                      sx, sy+155);
        bg.drawString("Stamina: " + stamina,                    sx, sy+185);
        bg.drawString("Power:   " + power,                      sx, sy+215);

        
        bg.setColor(new Color(255, 220, 80));
        bg.setFont(new Font("Arial", true, false, 24));
        bg.drawString("Avaluable attribute points: " + attributePoints, sx, sy+265);

        if(attributePoints > 0)
        {
            bg.setFont(new Font("Arial", false, false, 20));
            bg.setColor(new Color(180, 255, 180));
            bg.drawString("Press [1] Power+1   [2] Speed+1   [3] Stamina+1", sx, sy+300);
        }

        
        int wx = 600, wy = 180;
        bg.setFont(new Font("Arial", false, false, 22));
        bg.setColor(new Color(200, 200, 255));
        bg.drawString("── Weapon store ──", wx, wy);

        drawWeaponEntry(bg, wx, wy+40,  "[Q]  Fireball",  fireballLevel);
        drawWeaponEntry(bg, wx, wy+110, "[W]  Lightning", lightningLevel);
        drawWeaponEntry(bg, wx, wy+180, "[E]  IceWave",   iceWaveLevel);

        bg.setFont(new Font("Arial", false, false, 18));
        bg.setColor(new Color(180, 180, 180));
        bg.drawString("Buy: " + WEAPON_BUY_COST + " coin   Upgrade: " + WEAPON_UPGRADE_COST + " coin", wx, wy+270);

        
        if(!message.isEmpty())
        {
            bg.setFont(new Font("Arial", true, false, 26));
            bg.setColor(new Color(255, 220, 80));
            drawCenteredText(bg, message, getWidth()/2, 680);
        }
    }

    private void drawWeaponEntry(GreenfootImage bg, int x, int y, String name, int lv)
    {
        bg.setFont(new Font("Arial", false, false, 20));
        bg.setColor(Color.WHITE);
        bg.drawString(name, x, y);
        if(lv == 0)
        {
            bg.setColor(new Color(180, 180, 180));
            bg.drawString("Not purchased", x, y+28);
        }
        else
        {
            bg.setColor(new Color(100, 220, 100));
            bg.drawString("Lv." + lv + "  damage +" + (lv * 5), x, y+28);
        }
    }

    private void drawCenteredText(GreenfootImage bg, String text, int cx, int y)
    {
        int approxW = text.length() * 16;
        bg.drawString(text, cx - approxW/2, y);
    }
}
