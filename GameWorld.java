import greenfoot.*;

public class GameWorld extends World
{
    public Hero player;
    
    public int round;
    static final int TOTAL_ROUNDS    = 30;
    static final int ROUND_DURATION  = 60 * 60;
    int roundTimer = 0;

    
    int fireballLevel;
    int lightningLevel;
    int iceWaveLevel;
    int gunLevel;
    int swordLevel;

    int enemySpawnTimer = 0;
    int lightningTimer  = 0;
    int fireballTimer   = 0;
    int gunTimer        = 0;
    int swordTimer      = 0;

    static final int SWORD_MELEE_RADIUS = 75;

    int ENEMY_SPAWN_INTERVAL;
    static final int SKILL_INTERVAL     = 90;
    static final int MIN_SPAWN_DISTANCE = 300;
    static final int MAX_ENEMIES        = 30;

    GreenfootImage bgTile;
    int bgOffX = 0, bgOffY = 0;
    int screenCX, screenCY;

    
    int pendingAttributePoints = 0;
    boolean gameOverHandled = false;
    boolean roundEndHandled = false;

    public String heroType = "aurea";
    
    public GameWorld(String heroType)
    {
        this(70, 70, 0, 0,
             1, 4, 3, 3, 10,
             1, heroType,
             HeroData.startingWeapon(heroType, 0),
             HeroData.startingWeapon(heroType, 1),
             HeroData.startingWeapon(heroType, 2),
             HeroData.startingWeapon(heroType, 3),
             HeroData.startingWeapon(heroType, 4));
    }

    public GameWorld(
    int hp, int maxHp, int xp, int coin,
    int level, int speed, int stamina, int power,
    int xpToNextLevel,
    int round,
    String heroType,
    int fireballLevel, int lightningLevel, int iceWaveLevel,
    int gunLevel, int swordLevel)
    {
        super(1500, 750, 1);
        screenCX = getWidth()  / 2;
        screenCY = getHeight() / 2;
    
        this.round          = round;
        this.heroType       = heroType;
        HeroData.heroType   = heroType;
        this.fireballLevel  = fireballLevel;
        this.lightningLevel = lightningLevel;
        this.iceWaveLevel   = iceWaveLevel;
        this.gunLevel       = gunLevel;
        this.swordLevel     = swordLevel;
    
        ENEMY_SPAWN_INTERVAL = Math.max(20, 60 - (round - 1) * 2);
    
        bgTile = new GreenfootImage("background.png");
        if(bgTile.getWidth() <= 0)
        {
            bgTile = new GreenfootImage(128, 128);
            bgTile.setColor(new Color(35, 50, 35));
            bgTile.fill();
        }
        drawBackground(0, 0);
    
        player = createHero(heroType);
    
        player.hp            = hp;
        player.maxHp         = maxHp;
        player.xp            = xp;
        player.coin          = coin;
        player.level         = level;
        player.speed         = speed;
        player.stamina       = stamina;
        player.power         = power;
        player.xpToNextLevel = xpToNextLevel;
        addObject(player, screenCX, screenCY);
    
        if(iceWaveLevel > 0)
        {
            IceWave iw = new IceWave();
            iw.worldX = player.worldX;
            iw.worldY = player.worldY;
            addObject(iw, screenCX, screenCY);
        }
    
        if("kaine".equals(heroType) && swordLevel > 0)
        {
            KaineCompanionSword sword = new KaineCompanionSword();
            addObject(sword, screenCX + 22, screenCY - 6);
        }
    }

    public void act()
    {
        roundTimer++;

        bgOffX = (int)((-player.worldX % bgTile.getWidth()
                        + bgTile.getWidth())  % bgTile.getWidth());
        bgOffY = (int)((-player.worldY % bgTile.getHeight()
                        + bgTile.getHeight()) % bgTile.getHeight());
        drawBackground(bgOffX, bgOffY);

        updateScreenPositions();
        spawnEnemy();
        if(fireballLevel  > 0) spawnFireball();
        if(lightningLevel > 0) spawnLightning();
        if(gunLevel       > 0) spawnGun();
        if(swordLevel     > 0) spawnSwordMelee();

        checkPlayerDead();
        checkRoundEnd();
        drawHUD();
    }

    private void checkRoundEnd()
    {
        if(roundEndHandled || roundTimer < ROUND_DURATION) return;
        roundEndHandled = true;
        goToUpgradeScreen();
    }

    public void addAttributePoint()
    {
        pendingAttributePoints++;
    }

    private void goToUpgradeScreen()
    {
        HeroData.heroType = heroType;
        Hero p = player;
        Greenfoot.setWorld(new UpgradeScreen(
            p.hp, p.maxHp, p.xp, p.coin,
            p.level, p.speed, p.stamina, p.power,
            p.xpToNextLevel, pendingAttributePoints,
            round + 1,
            fireballLevel, lightningLevel, iceWaveLevel,
            gunLevel, swordLevel
        ));
    }

    private void updateScreenPositions()
    {
        double camX = player.worldX;
        double camY = player.worldY;

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
        Hero p = player;
        GreenfootImage bg = getBackground();

        
        drawBar(30, 30, 200, 18, p.hp, p.maxHp,
            new Color(180,40,40), new Color(60,10,10));
        
        drawBar(30, 56, 200, 14, p.xp, p.xpToNextLevel,
            new Color(50,120,220), new Color(15,40,80));

        showText("HP  " + p.hp + " / " + p.maxHp, 240, 39);
        showText("XP  " + p.xp + " / " + p.xpToNextLevel
                 + "   Lv." + p.level, 270, 63);
        showText("Coin: " + p.coin, 80, 90);
        showText("Weapon: " + HeroData.signatureWeaponName(heroType)
                 + " Lv." + getSignatureWeaponLevel(), 80, 115);

        
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
        if(gameOverHandled || !player.isDead) return;
        if(player instanceof AureaSolvine && player.animFrame < 6) return;
        gameOverHandled = true;
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
                    player.worldX, player.worldY,
                    closest.worldX,      closest.worldY,
                    player.getDamage() + (fireballLevel-1) * 5
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
                int sx = (int)(screenCX + (ex - player.worldX));
                int sy = (int)(screenCY + (ey - player.worldY));
                Lightning lt = new Lightning(ex, ey,
                    player.getDamage() + (lightningLevel-1) * 5);
                addObject(lt, sx, sy);
            }
        }
    }

    public void spawnGun()
    {
        gunTimer++;
        if(gunTimer >= SKILL_INTERVAL)
        {
            gunTimer = 0;
            Enemy closest = getClosestEnemy();
            if(closest == null || !(player instanceof LeonClovis)) return;

            LeonClovis leon = (LeonClovis)player;
            leon.gunDamage = player.getDamage() + (gunLevel - 1) * 5;
            Bullet bullet = new Bullet(closest, leon, leon.gunDamage);
            addObject(bullet, screenCX, screenCY);
        }
    }

    public void spawnSwordMelee()
    {
        swordTimer++;
        if(swordTimer >= SKILL_INTERVAL)
        {
            swordTimer = 0;
            int dmg = player.getDamage() + (swordLevel - 1) * 5;
            java.util.List<Enemy> enemies =
                new java.util.ArrayList<>(getObjects(Enemy.class));
            java.util.List<Enemy> defeated = new java.util.ArrayList<>();

            for(Enemy e : enemies)
            {
                if(distanceBetween(e.worldX, e.worldY,
                                  player.worldX, player.worldY) > SWORD_MELEE_RADIUS)
                    continue;

                if(e.takeDamage(dmg))
                {
                    player.gainXP(e.xpDrop);
                    player.gainCoin(e.coinDrop);
                    defeated.add(e);
                }
            }
            for(Enemy e : defeated)
                removeObject(e);
        }
    }

    private int getSignatureWeaponLevel()
    {
        if("leon".equals(heroType)) return gunLevel;
        if("kaine".equals(heroType)) return swordLevel;
        return lightningLevel;
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
                x = player.worldX + Greenfoot.getRandomNumber(1600) - 800;
                y = player.worldY + Greenfoot.getRandomNumber(900)  - 450;
            }
            while(distanceBetween(x, y,
                      player.worldX, player.worldY)
                  < MIN_SPAWN_DISTANCE);

            Enemy e = new Enemy(x, y, round); 
            int sx = (int)(screenCX + (x - player.worldX));
            int sy = (int)(screenCY + (y - player.worldY));
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
                player.worldX, player.worldY);
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

    private Hero createHero(String type)
    {
        if("leon".equals(type))
            return new LeonClovis();
        if("kaine".equals(type))
            return new KaineVelsarth();
        return new AureaSolvine();
    }
}