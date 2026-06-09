import greenfoot.*;

public class GameWorld extends World
{
    public Hero player;

    public int round;
    static final int TOTAL_ROUNDS = 30;
    static final int ROUND_DURATION = 60 * 60;

    int roundTimer = 0;

    int fireballLevel;
    int lightningLevel;
    int iceWaveLevel;
    int gunLevel;
    int swordLevel;

    int enemySpawnTimer = 0;
    int lightningTimer = 0;
    int fireballTimer = 0;
    int gunTimer = 0;
    int swordTimer = 0;

    static final int SWORD_MELEE_RADIUS = 75;
    static final int SKILL_INTERVAL = 90;
    static final int MAX_ENEMIES = 30;
    static final int ELITE_WARN_START_FRAMES = 360;
    static final int ELITE_SPAWN_AT_FRAMES = 180;
    static final int OFFSCREEN_MARGIN = 120;
    static final String SOUND_FIREBALL = "fireball.mp3";
    static final String SOUND_LIGHTNING = "lightning.mp3";
    static final String SOUND_GAME_OVER = "game over.mp3";
    static final String SOUND_BACKGROUND = "background.mp3";

    int ENEMY_SPAWN_INTERVAL;

    GreenfootImage bgTile;
    int bgOffX = 0;
    int bgOffY = 0;
    int screenCX;
    int screenCY;

    int pendingAttributePoints = 0;
    int enemiesKilled = 0;
    boolean gameOverHandled = false;
    boolean roundEndHandled = false;
    boolean eliteSpawnedThisRound = false;
    boolean eliteDefeatedThisRound = false;
    GreenfootSound backgroundMusic;

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
             HeroData.startingWeapon(heroType, 4),
             0);
    }

    public GameWorld(
        int hp, int maxHp, int xp, int coin,
        int level, int speed, int stamina, int power,
        int xpToNextLevel,
        int round,
        String heroType,
        int fireballLevel, int lightningLevel, int iceWaveLevel,
        int gunLevel, int swordLevel,
        int enemiesKilled)
    {
        super(1500, 750, 1);

        screenCX = getWidth() / 2;
        screenCY = getHeight() / 2;

        this.round = round;
        this.heroType = heroType;
        HeroData.heroType = heroType;

        this.fireballLevel = fireballLevel;
        this.lightningLevel = lightningLevel;
        this.iceWaveLevel = iceWaveLevel;
        this.gunLevel = gunLevel;
        this.swordLevel = swordLevel;
        this.enemiesKilled = enemiesKilled;

        ENEMY_SPAWN_INTERVAL = Math.max(20, 60 - (round - 1) * 2);

        bgTile = new GreenfootImage("background.png");
        drawBackground(0, 0);

        player = createHero(heroType);

        player.hp = hp;
        player.maxHp = maxHp;
        player.xp = xp;
        player.coin = coin;
        player.level = level;
        player.speed = speed;
        player.stamina = stamina;
        player.power = power;
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

        backgroundMusic = new GreenfootSound(SOUND_BACKGROUND);
        startBackgroundMusic();
    }

    public void act()
    {
        roundTimer++;

        bgOffX = (int)((-player.worldX % bgTile.getWidth() + bgTile.getWidth()) % bgTile.getWidth());
        bgOffY = (int)((-player.worldY % bgTile.getHeight() + bgTile.getHeight()) % bgTile.getHeight());

        drawBackground(bgOffX, bgOffY);
        updateScreenPositions();
        spawnEnemy();
        updateEliteSpawn();

        if(fireballLevel > 0)
        {
            spawnFireball();
        }

        if(lightningLevel > 0)
        {
            spawnLightning();
        }

        if(gunLevel > 0)
        {
            spawnGun();
        }

        if(swordLevel > 0)
        {
            spawnSwordMelee();
        }

        checkPlayerDead();
        checkRoundEnd();
        drawHUD();
    }

    private void startBackgroundMusic()
    {
        backgroundMusic.playLoop();
    }

    private void stopBackgroundMusic()
    {
        backgroundMusic.stop();
    }

    private void checkRoundEnd()
    {
        if(roundEndHandled || roundTimer < ROUND_DURATION)
        {
            return;
        }

        roundEndHandled = true;
        goToUpgradeScreen();
    }

    public void addAttributePoint()
    {
        pendingAttributePoints++;
    }

    private void goToUpgradeScreen()
    {
        Hero p = player;

        Greenfoot.setWorld(new UpgradeScreen(
            p.hp, p.maxHp, p.xp, p.coin,
            p.level, p.speed, p.stamina, p.power,
            p.xpToNextLevel, pendingAttributePoints,
            round + 1,
            fireballLevel, lightningLevel, iceWaveLevel,
            gunLevel, swordLevel,
            enemiesKilled,
            eliteDefeatedThisRound
        ));
    }

    public void handleEnemyDefeat(Enemy e)
    {
        if(e == null || e.getWorld() == null)
        {
            return;
        }

        enemiesKilled++;
        player.gainXP(e.xpDrop);
        player.gainCoin(e.coinDrop);

        if(e instanceof EliteEnemy)
        {
            eliteDefeatedThisRound = true;
        }

        removeObject(e);
    }

    public static int weaponTriggerCount(int level)
    {
        return 1 + level / 5;
    }

    private void updateEliteSpawn()
    {
        if(eliteSpawnedThisRound || roundEndHandled)
        {
            return;
        }

        int framesLeft = ROUND_DURATION - roundTimer;

        if(framesLeft > ELITE_SPAWN_AT_FRAMES)
        {
            return;
        }

        spawnEliteEnemy();
        eliteSpawnedThisRound = true;
    }

    private void spawnEliteEnemy()
    {
        double[] pos = randomOffScreenWorldPosition();
        EliteEnemy elite = new EliteEnemy(pos[0], pos[1], round);

        int sx = (int)(screenCX + (pos[0] - player.worldX));
        int sy = (int)(screenCY + (pos[1] - player.worldY));

        addObject(elite, sx, sy);
    }

    private double[] randomOffScreenWorldPosition()
    {
        double minDistance = Math.sqrt(screenCX * screenCX + screenCY * screenCY) + OFFSCREEN_MARGIN;
        double angle = Math.random() * Math.PI * 2;

        return new double[] {
            player.worldX + Math.cos(angle) * minDistance,
            player.worldY + Math.sin(angle) * minDistance
        };
    }

    public java.util.List<Enemy> getClosestEnemies(int count, java.util.Set<Enemy> exclude)
    {
        java.util.List<Enemy> enemies = new java.util.ArrayList<>(getObjects(Enemy.class));
        java.util.List<Enemy> sorted = new java.util.ArrayList<>();

        for(Enemy enemy : enemies)
        {
            if(enemy.state == Enemy.State.DEATH)
            {
                continue;
            }

            if(exclude != null && exclude.contains(enemy))
            {
                continue;
            }

            sorted.add(enemy);
        }

        sorted.sort((a, b) -> Double.compare(
            distanceBetween(a.worldX, a.worldY, player.worldX, player.worldY),
            distanceBetween(b.worldX, b.worldY, player.worldX, player.worldY)
        ));

        if(sorted.size() <= count)
        {
            return sorted;
        }

        return sorted.subList(0, count);
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

        for(int x = -bgTile.getWidth(); x < getWidth(); x += bgTile.getWidth())
        {
            for(int y = -bgTile.getHeight(); y < getHeight(); y += bgTile.getHeight())
            {
                bg.drawImage(bgTile, x + offX, y + offY);
            }
        }
    }

    public void drawHUD()
    {
        Hero p = player;

        drawBar(30, 30, 200, 18, p.hp, p.maxHp,
            new Color(180, 40, 40), new Color(60, 10, 10));

        drawBar(30, 56, 200, 14, p.xp, p.xpToNextLevel,
            new Color(50, 120, 220), new Color(15, 40, 80));

        showText("HP " + p.hp + " / " + p.maxHp, 250, 39);
        showText("XP " + p.xp + " / " + p.xpToNextLevel + " Lv." + p.level, 280, 63);
        showText("Coin: " + p.coin, 80, 90);
        showText("Weapon: " + HeroData.signatureWeaponName(heroType) + " Lv." + getSignatureWeaponLevel(), 130, 115);

        int secondsLeft = (ROUND_DURATION - roundTimer) / 60;

        showText("Round " + round + " / " + TOTAL_ROUNDS, getWidth() / 2, 30);
        showText(secondsLeft + " seconds left", getWidth() / 2, 58);

        int framesLeft = ROUND_DURATION - roundTimer;

        if(!eliteSpawnedThisRound
            && framesLeft <= ELITE_WARN_START_FRAMES
            && framesLeft > ELITE_SPAWN_AT_FRAMES)
        {
            int eliteSeconds = (framesLeft - ELITE_SPAWN_AT_FRAMES + 59) / 60;
            showText("Elite incoming: " + eliteSeconds, getWidth() / 2, 86);
        }
        else if(eliteSpawnedThisRound && !eliteDefeatedThisRound)
        {
            showText("Elite enemy active!", getWidth() / 2, 86);
        }
    }

    private void drawBar(int x, int y, int w, int h, int value, int max, Color fill, Color back)
    {
        GreenfootImage bg = getBackground();

        bg.setColor(back);
        bg.fillRect(x, y, w, h);

        bg.setColor(fill);

        int fillWidth = 0;

        if(max > 0)
        {
            fillWidth = value * w / max;
        }

        bg.fillRect(x, y, fillWidth, h);

        bg.setColor(Color.WHITE);
        bg.drawRect(x, y, w, h);
    }

    public void checkPlayerDead()
    {
        if(gameOverHandled || !player.isDead)
        {
            return;
        }

        if(player instanceof AureaSolvine && player.animFrame < 6)
        {
            return;
        }

        gameOverHandled = true;
        stopBackgroundMusic();
        Greenfoot.playSound(SOUND_GAME_OVER);

        Hero p = player;
        int roundsSurvived = Math.max(0, round - 1);

        Greenfoot.setWorld(new EndScreen(
            false, heroType, roundsSurvived, enemiesKilled,
            p.hp, p.maxHp, p.xp, p.coin,
            p.level, p.speed, p.stamina, p.power,
            fireballLevel, lightningLevel, iceWaveLevel,
            gunLevel, swordLevel
        ));
    }

    public void spawnFireball()
    {
        fireballTimer++;

        if(fireballTimer >= SKILL_INTERVAL)
        {
            fireballTimer = 0;

            int shots = weaponTriggerCount(fireballLevel);
            java.util.List<Enemy> targets = getClosestEnemies(shots, null);
            int damage = player.getDamage() + (fireballLevel - 1) * 5;
            boolean fired = false;

            for(Enemy target : targets)
            {
                Fireball fb = new Fireball(
                    player.worldX, player.worldY,
                    target.worldX, target.worldY,
                    damage
                );

                addObject(fb, screenCX, screenCY);
                fired = true;
            }

            if(fired)
            {
                Greenfoot.playSound(SOUND_FIREBALL);
            }
        }
    }

    public void spawnLightning()
    {
        lightningTimer++;

        if(lightningTimer >= SKILL_INTERVAL)
        {
            lightningTimer = 0;

            int shots = weaponTriggerCount(lightningLevel);
            java.util.List<Enemy> targets = getClosestEnemies(shots, null);
            int damage = player.getDamage() + (lightningLevel - 1) * 5;
            boolean fired = false;

            for(Enemy target : targets)
            {
                int sx = (int)(screenCX + (target.worldX - player.worldX));
                int sy = (int)(screenCY + (target.worldY - player.worldY));

                Lightning lt = new Lightning(target.worldX, target.worldY, damage);
                addObject(lt, sx, sy);
                fired = true;
            }

            if(fired)
            {
                Greenfoot.playSound(SOUND_LIGHTNING);
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

            if(closest == null || !(player instanceof LeonClovis))
            {
                return;
            }

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

            java.util.List<Enemy> enemies = new java.util.ArrayList<>(getObjects(Enemy.class));

            for(Enemy e : enemies)
            {
                if(distanceBetween(e.worldX, e.worldY, player.worldX, player.worldY) > SWORD_MELEE_RADIUS)
                {
                    continue;
                }

                e.takeDamage(dmg);
            }
        }
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

    public void spawnEnemy()
    {
        enemySpawnTimer++;

        if(enemySpawnTimer >= ENEMY_SPAWN_INTERVAL)
        {
            enemySpawnTimer = 0;

            if(getObjects(Enemy.class).size() >= MAX_ENEMIES)
            {
                return;
            }

            double[] pos = randomOffScreenWorldPosition();
            double x = pos[0];
            double y = pos[1];

            Enemy e = new Enemy(x, y, round);

            int sx = (int)(screenCX + (x - player.worldX));
            int sy = (int)(screenCY + (y - player.worldY));

            addObject(e, sx, sy);
        }
    }

    public Enemy getClosestEnemy()
    {
        java.util.List<Enemy> enemies = getObjects(Enemy.class);

        if(enemies.isEmpty())
        {
            return null;
        }

        Enemy closest = null;
        double minDistance = Double.MAX_VALUE;

        for(Enemy enemy : enemies)
        {
            double d = distanceBetween(enemy.worldX, enemy.worldY, player.worldX, player.worldY);

            if(d < minDistance)
            {
                minDistance = d;
                closest = enemy;
            }
        }

        return closest;
    }

    public double distanceBetween(double x1, double y1, double x2, double y2)
    {
        double dx = x1 - x2;
        double dy = y1 - y2;

        return Math.sqrt(dx * dx + dy * dy);
    }

    private Hero createHero(String type)
    {
        if("leon".equals(type))
        {
            return new LeonClovis();
        }

        if("kaine".equals(type))
        {
            return new KaineVelsarth();
        }

        return new AureaSolvine();
    }
}