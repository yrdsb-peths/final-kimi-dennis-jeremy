import greenfoot.*;

public class GameWorld extends World
{
    public AureaSolvine aureaSolvine;
    
    public int round;
    static final int TOTAL_ROUNDS    = 30;
    static final int ROUND_DURATION  = 60 * 60;
    int roundTimer = 0;

    
    int fireballLevel;
    int lightningLevel;
    int iceWaveLevel;

    int enemySpawnTimer = 0;
    int lightningTimer  = 0;
    int fireballTimer   = 0;

    int ENEMY_SPAWN_INTERVAL;
    static final int SKILL_INTERVAL     = 90;
    static final int MIN_SPAWN_DISTANCE = 300;
    static final int MAX_ENEMIES        = 30;

    GreenfootImage bgTile;
    int bgOffX = 0, bgOffY = 0;
    int screenCX, screenCY;

    
    int pendingAttributePoints = 0;

// 在 GameWorld.java 最上方加一个字段
    public String heroType = "aurea";
    
    // 新增：从标题界面直接开始的构造器
    public GameWorld(String heroType)
    {
        this(70, 70, 0, 0,
             1, 4, 3, 3, 10,
             1,
             0, 0, 0);
        this.heroType = heroType;
        HeroData.heroType = heroType;
    }

    public GameWorld(
    int hp, int maxHp, int xp, int coin,
    int level, int speed, int stamina, int power,
    int xpToNextLevel,
    int round,
    int fireballLevel, int lightningLevel, int iceWaveLevel)
    {
        super(1500, 750, 1);
        screenCX = getWidth()  / 2;
        screenCY = getHeight() / 2;
    
        this.round          = round;
        this.fireballLevel  = fireballLevel;
        this.lightningLevel = lightningLevel;
        this.iceWaveLevel   = iceWaveLevel;
        this.heroType       = HeroData.heroType; // 从全局读取
    
        ENEMY_SPAWN_INTERVAL = Math.max(20, 60 - (round - 1) * 2);
    
        bgTile = new GreenfootImage("background.png");
        drawBackground(0, 0);
    
        // 根据英雄类型创建不同角色
        if(heroType.equals("leon"))
        {
            // Leon 用 AureaSolvine 的数据但换动画（共用系统）
            aureaSolvine = new AureaSolvine("leon");
        }
        else if(heroType.equals("kaine"))
        {
            aureaSolvine = new AureaSolvine("kaine");
        }
        else
        {
            aureaSolvine = new AureaSolvine("aurea");
        }
    
        aureaSolvine.hp            = hp;
        aureaSolvine.maxHp         = maxHp;
        aureaSolvine.xp            = xp;
        aureaSolvine.coin          = coin;
        aureaSolvine.level         = level;
        aureaSolvine.speed         = speed;
        aureaSolvine.stamina       = stamina;
        aureaSolvine.power         = power;
        aureaSolvine.xpToNextLevel = xpToNextLevel;
        addObject(aureaSolvine, screenCX, screenCY);
    
        if(iceWaveLevel > 0)
        {
            IceWave iw = new IceWave();
            iw.worldX = aureaSolvine.worldX;
            iw.worldY = aureaSolvine.worldY;
            addObject(iw, screenCX, screenCY);
        }
    
        // Kaine 自带剑
        if(heroType.equals("kaine"))
        {
            KaineCompanionSword sword = new KaineCompanionSword();
            addObject(sword, screenCX + 22, screenCY - 6);
        }
    }

    public void act()
    {
        roundTimer++;

        bgOffX = (int)((-aureaSolvine.worldX % bgTile.getWidth()
                        + bgTile.getWidth())  % bgTile.getWidth());
        bgOffY = (int)((-aureaSolvine.worldY % bgTile.getHeight()
                        + bgTile.getHeight()) % bgTile.getHeight());
        drawBackground(bgOffX, bgOffY);

        updateScreenPositions();
        spawnEnemy();
        if(fireballLevel  > 0) spawnFireball();
        if(lightningLevel > 0) spawnLightning();

        checkPlayerDead();
        checkRoundEnd();
        drawHUD();
    }

    private void checkRoundEnd()
    {
        if(roundTimer >= ROUND_DURATION)
        {
            goToUpgradeScreen();
        }
    }

    public void addAttributePoint()
    {
        pendingAttributePoints++;
    }

    private void goToUpgradeScreen()
    {
        AureaSolvine p = aureaSolvine;
        Greenfoot.setWorld(new UpgradeScreen(
            p.hp, p.maxHp, p.xp, p.coin,
            p.level, p.speed, p.stamina, p.power,
            p.xpToNextLevel, pendingAttributePoints,
            round + 1,
            fireballLevel, lightningLevel, iceWaveLevel
        ));
    }

    private void updateScreenPositions()
    {
        double camX = aureaSolvine.worldX;
        double camY = aureaSolvine.worldY;

        for(Enemy e : getObjects(Enemy.class))
        {
            int sx = (int)(screenCX + (e.worldX - camX));
            int sy = (int)(screenCY + (e.worldY - camY));
            e.setLocation(sx, sy);
            e.turnTowards(screenCX, screenCY);
        }
        for(Fireball f : getObjects(Fireball.class))
        {
            int sx = (int)(screenCX + (f.worldX - camX));
            int sy = (int)(screenCY + (f.worldY - camY));
            f.setLocation(sx, sy);
        }
        for(Lightning l : getObjects(Lightning.class))
        {
            int sx = (int)(screenCX + (l.worldX - camX));
            int sy = (int)(screenCY + (l.worldY - camY));
            l.setLocation(sx, sy);
        }
        for(IceWave iw : getObjects(IceWave.class))
        {
            iw.setLocation(screenCX, screenCY);
        }
    }

    
    private void drawBackground(int offX, int offY)
    {
        GreenfootImage bg = getBackground();
        bg.clear();
        int tw = bgTile.getWidth();
        int th = bgTile.getHeight();
        for(int x = offX - tw; x < getWidth()  + tw; x += tw)
        for(int y = offY - th; y < getHeight() + th; y += th)
            bg.drawImage(bgTile, x, y);
    }

   
    public void drawHUD()
    {
        AureaSolvine p = aureaSolvine;
        GreenfootImage bg = getBackground();

        
        drawBar(30, 30, 200, 18, p.hp, p.maxHp,
            new Color(180,40,40), new Color(60,10,10));
        
        drawBar(30, 56, 200, 14, p.xp, p.xpToNextLevel,
            new Color(50,120,220), new Color(15,40,80));

        showText("HP  " + p.hp + " / " + p.maxHp, 240, 39);
        showText("XP  " + p.xp + " / " + p.xpToNextLevel
                 + "   Lv." + p.level, 270, 63);
        showText("Coin: " + p.coin, 80, 90);

        
        int secondsLeft = (ROUND_DURATION - roundTimer) / 60;
        showText("Round " + round + " / " + TOTAL_ROUNDS,
                 getWidth()/2, 30);
        showText(secondsLeft + " second left",
                 getWidth()/2, 58);
    }

    private void drawBar(int x, int y, int w, int h,
                         int cur, int max, Color fill, Color bg)
    {
        GreenfootImage c = getBackground();
        c.setColor(bg);   c.fillRect(x, y, w, h);
        int f = Math.max(0, Math.min(w, (int)((double)cur/max*w)));
        c.setColor(fill); c.fillRect(x, y, f, h);
        c.setColor(Color.WHITE); c.drawRect(x, y, w, h);
    }

    
    public void checkPlayerDead()
    {
        if(aureaSolvine.isDead && aureaSolvine.animFrame >= 6)
            Greenfoot.setWorld(new TitleScreen());
    }

    
    public void spawnFireball()
    {
        fireballTimer++;
        if(fireballTimer >= SKILL_INTERVAL)
        {
            fireballTimer = 0;
            Enemy closest = getClosestEnemy();
            if(closest != null)
            {
                Fireball fb = new Fireball(
                    aureaSolvine.worldX, aureaSolvine.worldY,
                    closest.worldX,      closest.worldY,
                    aureaSolvine.getDamage() + (fireballLevel-1) * 5
                );
                addObject(fb, screenCX, screenCY);
            }
        }
    }

    public void spawnLightning()
    {
        lightningTimer++;
        if(lightningTimer >= SKILL_INTERVAL)
        {
            lightningTimer = 0;
            Enemy closest = getClosestEnemy();
            if(closest != null)
            {
                double ex = closest.worldX;
                double ey = closest.worldY;
                int sx = (int)(screenCX + (ex - aureaSolvine.worldX));
                int sy = (int)(screenCY + (ey - aureaSolvine.worldY));
                Lightning lt = new Lightning(ex, ey,
                    aureaSolvine.getDamage() + (lightningLevel-1) * 5);
                addObject(lt, sx, sy);
            }
        }
    }

    public void spawnEnemy()
    {
        enemySpawnTimer++;
        if(enemySpawnTimer >= ENEMY_SPAWN_INTERVAL)
        {
            enemySpawnTimer = 0;
            if(getObjects(Enemy.class).size() >= MAX_ENEMIES) return;

            double x, y;
            do {
                x = aureaSolvine.worldX + Greenfoot.getRandomNumber(1600) - 800;
                y = aureaSolvine.worldY + Greenfoot.getRandomNumber(900)  - 450;
            }
            while(distanceBetween(x, y,
                      aureaSolvine.worldX, aureaSolvine.worldY)
                  < MIN_SPAWN_DISTANCE);

            Enemy e = new Enemy(x, y, round); 
            int sx = (int)(screenCX + (x - aureaSolvine.worldX));
            int sy = (int)(screenCY + (y - aureaSolvine.worldY));
            addObject(e, sx, sy);
        }
    }

    
    public Enemy getClosestEnemy()
    {
        java.util.List<Enemy> enemies = getObjects(Enemy.class);
        if(enemies.isEmpty()) return null;
        Enemy closest = null;
        double minD = Double.MAX_VALUE;
        for(Enemy e : enemies)
        {
            double d = distanceBetween(
                e.worldX, e.worldY,
                aureaSolvine.worldX, aureaSolvine.worldY);
            if(d < minD) { minD = d; closest = e; }
        }
        return closest;
    }

    public double distanceBetween(double x1, double y1,
                                  double x2, double y2)
    {
        double dx = x1-x2, dy = y1-y2;
        return Math.sqrt(dx*dx + dy*dy);
    }
}