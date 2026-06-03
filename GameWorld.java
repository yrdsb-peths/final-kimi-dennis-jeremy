import greenfoot.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class GameWorld extends World
{
    public Hero player;

    public int round;
    static final int TOTAL_ROUNDS = 30;
    static final int ROUND_DURATION = 60 * 60;
    static final int SWORD_MELEE_RADIUS = 75;
    static final int SKILL_INTERVAL = 90;
    static final int MIN_SPAWN_DISTANCE = 300;
    static final int MAX_ENEMIES = 30;

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
    int enemySpawnInterval;
    int pendingAttributePoints = 0;
    int bgOffX = 0;
    int bgOffY = 0;
    int screenCX;
    int screenCY;

    boolean gameOverHandled = false;
    boolean roundEndHandled = false;

    public String heroType = "aurea";

    private GreenfootImage bgTile;
    private KaineCompanionSword kaineSword;
    private HashSet<Enemy> swordHitEnemies = new HashSet<Enemy>();

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
        enemySpawnInterval = Math.max(20, 60 - (round - 1) * 2);

        loadBackground();
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
        player.worldX = screenCX;
        player.worldY = screenCY;

        if(iceWaveLevel > 0)
        {
            IceWave iceWave = new IceWave();
            iceWave.worldX = player.worldX;
            iceWave.worldY = player.worldY;
            addObject(iceWave, screenCX, screenCY);
        }

        if("kaine".equals(heroType) && swordLevel > 0)
        {
            kaineSword = new KaineCompanionSword();
            addObject(kaineSword, screenCX + 22, screenCY - 6);
        }
    }

    public void act()
    {
        roundTimer++;
        player.updateHero();
        keepPlayerOnScreen();

        drawBackground(0, 0);

        spawnEnemy();

        if(swordLevel > 0)
        {
            spawnSwordMelee();
        }

        updateEnemies();

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

        updateScreenPositions();
        checkPlayerDead();
        checkRoundEnd();
        drawHUD();
    }

    public double getPlayerWorldX()
    {
        return player.worldX;
    }

    public double getPlayerWorldY()
    {
        return player.worldY;
    }

    public void damagePlayer(int damage)
    {
        player.takeHit(damage);
    }

    public void addAttributePoint()
    {
        pendingAttributePoints++;
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

    private void goToUpgradeScreen()
    {
        HeroData.heroType = heroType;
        Greenfoot.setWorld(new UpgradeScreen(
            player.hp, player.maxHp, player.xp, player.coin,
            player.level, player.speed, player.stamina, player.power,
            player.xpToNextLevel, pendingAttributePoints,
            round + 1,
            fireballLevel, lightningLevel, iceWaveLevel,
            gunLevel, swordLevel
        ));
    }

    private void updateScreenPositions()
    {
        player.setLocation((int)player.worldX, (int)player.worldY);

        for(Enemy enemy : getObjects(Enemy.class))
        {
            enemy.setLocation((int)enemy.worldX, (int)enemy.worldY);
            enemy.turnTowards(player.getX(), player.getY());
        }

        for(Weapon weapon : getObjects(Weapon.class))
        {
            weapon.setLocation((int)weapon.worldX, (int)weapon.worldY);
        }
    }

    private void keepPlayerOnScreen()
    {
        player.worldX = Math.max(36, Math.min(getWidth() - 36, player.worldX));
        player.worldY = Math.max(36, Math.min(getHeight() - 36, player.worldY));
        player.setLocation((int)player.worldX, (int)player.worldY);
    }

    private void updateEnemies()
    {
        List<Enemy> enemies = new ArrayList<Enemy>(getObjects(Enemy.class));

        for(Enemy enemy : enemies)
        {
            if(enemy.getWorld() != null)
            {
                enemy.updateForGameWorld(this);
            }
        }
    }

    private void loadBackground()
    {
        try
        {
            bgTile = new GreenfootImage("background.png");
        }
        catch(IllegalArgumentException exception)
        {
            bgTile = new GreenfootImage(128, 128);
            bgTile.setColor(new Color(35, 50, 35));
            bgTile.fill();
        }
    }

    private void drawBackground(int offX, int offY)
    {
        GreenfootImage background = getBackground();

        for(int x = -offX; x < getWidth(); x += bgTile.getWidth())
        {
            for(int y = -offY; y < getHeight(); y += bgTile.getHeight())
            {
                background.drawImage(bgTile, x, y);
            }
        }
    }

    public void drawHUD()
    {
        GreenfootImage background = getBackground();

        drawBar(background, 30, 30, 200, 18, player.hp, player.maxHp,
            new Color(180, 40, 40), new Color(60, 10, 10));
        drawBar(background, 30, 56, 200, 14, player.xp, player.xpToNextLevel,
            new Color(50, 120, 220), new Color(15, 40, 80));

        showText("HP  " + player.hp + " / " + player.maxHp, 240, 39);
        showText("XP  " + player.xp + " / " + player.xpToNextLevel + "   Lv." + player.level, 270, 63);
        showText("Coin: " + player.coin, 80, 90);
        showText("Weapon: " + HeroData.signatureWeaponName(heroType) + " Lv." + getSignatureWeaponLevel(), 100, 115);

        int secondsLeft = Math.max(0, (ROUND_DURATION - roundTimer) / 60);
        showText("Round " + round + " / " + TOTAL_ROUNDS, getWidth() / 2, 30);
        showText(secondsLeft + " seconds left", getWidth() / 2, 58);
    }

    private void drawBar(
        GreenfootImage background,
        int x, int y, int width, int height,
        int value, int max,
        Color fillColor, Color emptyColor)
    {
        int fillWidth = max <= 0 ? 0 : Math.max(0, Math.min(width, value * width / max));

        background.setColor(emptyColor);
        background.fillRect(x, y, width, height);
        background.setColor(fillColor);
        background.fillRect(x, y, fillWidth, height);
        background.setColor(Color.WHITE);
        background.drawRect(x, y, width, height);
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
                Fireball fireball = new Fireball(
                    player.worldX, player.worldY,
                    closest.worldX, closest.worldY,
                    player.getDamage() + (fireballLevel - 1) * 5
                );
                addObject(fireball, player.getX(), player.getY());
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
                Lightning lightning = new Lightning(
                    closest.worldX,
                    closest.worldY,
                    player.getDamage() + (lightningLevel - 1) * 5
                );
                addObject(lightning, (int)closest.worldX, (int)closest.worldY);
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
            addObject(bullet, player.getX(), player.getY());
        }
    }

    public void spawnSwordMelee()
    {
        swordTimer++;

        if(swordTimer < SKILL_INTERVAL)
        {
            return;
        }

        swordTimer = 0;
        List<Enemy> enemies = new ArrayList<Enemy>(getObjects(Enemy.class));
        List<Enemy> defeated = new ArrayList<Enemy>();
        boolean hitAnyEnemy = false;

        for(Enemy enemy : enemies)
        {
            if(swordHitEnemies.contains(enemy)
                || distanceBetween(enemy.worldX, enemy.worldY, player.worldX, player.worldY) > SWORD_MELEE_RADIUS)
            {
                continue;
            }

            swordHitEnemies.add(enemy);
            hitAnyEnemy = true;

            if(enemy.takeDamage(enemy.hp))
            {
                player.gainXP(enemy.xpDrop);
                player.gainCoin(enemy.coinDrop);
                player.checkLevelUp();
                defeated.add(enemy);
            }
        }

        if(hitAnyEnemy && kaineSword != null)
        {
            kaineSword.playActiveSwordSound();
        }

        for(Enemy enemy : defeated)
        {
            if(enemy.getWorld() != null)
            {
                removeObject(enemy);
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

        if(enemySpawnTimer < enemySpawnInterval)
        {
            return;
        }

        enemySpawnTimer = 0;

        if(getObjects(Enemy.class).size() >= MAX_ENEMIES)
        {
            return;
        }

        double x;
        double y;

        do
        {
            x = Greenfoot.getRandomNumber(getWidth());
            y = Greenfoot.getRandomNumber(getHeight());
        }
        while(distanceBetween(x, y, player.worldX, player.worldY) < MIN_SPAWN_DISTANCE);

        Enemy enemy = new Enemy(x, y, round);
        addObject(enemy, (int)x, (int)y);
    }

    public Enemy getClosestEnemy()
    {
        List<Enemy> enemies = getObjects(Enemy.class);

        if(enemies.isEmpty())
        {
            return null;
        }

        Enemy closest = null;
        double minDistance = Double.MAX_VALUE;

        for(Enemy enemy : enemies)
        {
            double distance = distanceBetween(enemy.worldX, enemy.worldY, player.worldX, player.worldY);

            if(distance < minDistance)
            {
                minDistance = distance;
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
