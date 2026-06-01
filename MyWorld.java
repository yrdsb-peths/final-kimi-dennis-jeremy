import greenfoot.*;

public class MyWorld extends World
{
    private static final int CENTER_X = 400;
    private static final int BATTLE_TEXT_Y = 520;
    private static final int ENEMY_BOTTOM_PADDING = 30;
    private static final int ENEMY_SPAWN_DELAY = 2000;
    private static final int NEXT_WAVE_DELAY = 2000;
    private static final int MAX_ENEMIES_ON_SCREEN = 5;
    private static final int WAVE_SIZE_MULTIPLIER = 4;
    private static final int SHOP_UPGRADE_COST = 10;
    private static final int SHOP_DAMAGE_BONUS = 5;
    private static final String BACKGROUND_IMAGE = "background.png";

    private boolean playerChosen = false;
    private final SimpleTimer enemySpawnTimer = new SimpleTimer();
    private final SimpleTimer nextWaveTimer = new SimpleTimer();
    private final SimpleTimer aureaSkillTimer = new SimpleTimer();
    private int waveNumber = 1;
    private int enemiesThisWave = 1;
    private int enemiesSpawnedThisWave = 0;
    private boolean waitingForNextWave = false;
    private boolean needsStartingEnemies = false;
    private final SimpleTimer shopTimer = new SimpleTimer();

    public LeonClovis leon;
    public KaineVelsarth kaine;
    public AureaSolvine aurea;

    public MyWorld()
    {
        super(800, 600, 1);
        showTitleScreen();
    }

    public MyWorld(String character)
    {
        this();
        choosePlayer(character);
    }

    public void act()
    {
        if(!playerChosen)
        {
            checkPlayerChoice();
        }
        else
        {
            if(needsStartingEnemies)
            {
                spawnStartingEnemies();
                needsStartingEnemies = false;
            }

            spawnEnemies();
            spawnAureaSkill();
            handleShop();
            updateWaveText();
            updateShopText();
        }
    }

    public void checkPlayerChoice()
    {
        String key = Greenfoot.getKey();

        if(key == null)
        {
            return;
        }

        choosePlayer(key);
    }

    private void choosePlayer(String character)
    {
        if(character.equalsIgnoreCase("k") || character.equalsIgnoreCase("kaine"))
        {
            spawnKaine();
        }
        else if(character.equalsIgnoreCase("l") || character.equalsIgnoreCase("leon"))
        {
            spawnLeon();
        }
        else if(character.equalsIgnoreCase("a") || character.equalsIgnoreCase("aurea"))
        {
            spawnAurea();
        }
    }

    public void showTitleScreen()
    {
        drawWorldBackground();

        GreenfootImage titleImage = new GreenfootImage(
            "Spider Skill Battles",
            42,
            new Color(20, 20, 20),
            new Color(245, 245, 245)
        );

        getBackground().drawImage(
            titleImage,
            (getWidth() - titleImage.getWidth()) / 2,
            120
        );

        showText("Press K for Kaine", CENTER_X, 230);
        showText("Press L for Leon", CENTER_X, 260);
        showText("Press A for Aurea", CENTER_X, 290);
        showText("Click the world, then press a key", CENTER_X, 330);
    }

    public void clearTitleScreen()
    {
        drawWorldBackground();
        showText("", CENTER_X, 230);
        showText("", CENTER_X, 260);
        showText("", CENTER_X, 290);
        showText("", CENTER_X, 330);
        showText("", CENTER_X, 520);
        showText("", CENTER_X, 545);
        showText("", CENTER_X, 570);
        showText("", 600, 25);
        showText("", 600, 55);
    }

    private void drawWorldBackground()
    {
        GreenfootImage background;

        try
        {
            background = new GreenfootImage(BACKGROUND_IMAGE);
            background.scale(getWidth(), getHeight());
        }
        catch(IllegalArgumentException exception)
        {
            background = new GreenfootImage(getWidth(), getHeight());
            background.setColor(new Color(245, 245, 245));
            background.fill();
        }

        setBackground(background);
    }

    public void spawnKaine()
    {
        kaine = new KaineVelsarth();
        addObject(kaine, CENTER_X, 300);
        playerChosen = true;
        clearTitleScreen();
        showText(kaine.getStartingLoadoutText(), CENTER_X, 520);
        needsStartingEnemies = true;
    }

    public void spawnAurea()
    {
        aurea = new AureaSolvine();
        addObject(aurea, CENTER_X, 300);
        playerChosen = true;
        clearTitleScreen();
        showText(aurea.getStartingLoadoutText(), CENTER_X, 520);
        needsStartingEnemies = true;
    }

    public void spawnLeon()
    {
        leon = new LeonClovis();
        addObject(leon, CENTER_X, 300);
        playerChosen = true;
        clearTitleScreen();
        showText("Leon starts with ranged attacks.", CENTER_X, 520);
        showText("", CENTER_X, 545);
        showText("", CENTER_X, 570);
        needsStartingEnemies = true;
    }

    public void spawnStartingEnemies()
    {
        startWave();
        spawnEnemyInWave();
        enemySpawnTimer.mark();
    }

    public void spawnEnemies()
    {
        if(waitingForNextWave)
        {
            if(nextWaveTimer.millisElapsed() > NEXT_WAVE_DELAY)
            {
                startWave();
                spawnEnemyInWave();
                enemySpawnTimer.mark();
            }

            return;
        }

        if(enemiesSpawnedThisWave < enemiesThisWave
            && getObjects(Enemy.class).size() < MAX_ENEMIES_ON_SCREEN
            && enemySpawnTimer.millisElapsed() > ENEMY_SPAWN_DELAY)
        {
            spawnEnemyInWave();
            enemySpawnTimer.mark();
        }

        if(enemiesSpawnedThisWave >= enemiesThisWave && getObjects(Enemy.class).isEmpty())
        {
            waveNumber++;
            waitingForNextWave = true;
            nextWaveTimer.mark();
            showText("Wave " + waveNumber + " starts soon", CENTER_X, 545);
            showText("Enemies left: 0 / " + enemiesThisWave, CENTER_X, 570);
        }
    }

    private void startWave()
    {
        enemiesThisWave = getEnemiesForWave();
        enemiesSpawnedThisWave = 0;
        waitingForNextWave = false;
        updateWaveText();
        enemySpawnTimer.mark();
    }

    private int getEnemiesForWave()
    {
        return waveNumber * WAVE_SIZE_MULTIPLIER;
    }

    private void spawnEnemyInWave()
    {
        spawnEnemy();
        enemiesSpawnedThisWave++;
        updateWaveText();
    }

    private void updateWaveText()
    {
        if(waitingForNextWave)
        {
            showText("Wave " + waveNumber + " starts soon", CENTER_X, 545);
            showText("Enemies left: 0 / " + enemiesThisWave, CENTER_X, 570);
            return;
        }

        showText("Wave " + waveNumber, CENTER_X, 545);
        showText("Enemies left: " + getEnemiesLeftInWave() + " / " + enemiesThisWave, CENTER_X, 570);
    }

    private void handleShop()
    {
        if(!Greenfoot.isKeyDown("u") || shopTimer.millisElapsed() <= 300)
        {
            return;
        }

        if(leon != null && leon.coin >= SHOP_UPGRADE_COST)
        {
            leon.coin -= SHOP_UPGRADE_COST;
            leon.gunDamage += SHOP_DAMAGE_BONUS;
            shopTimer.mark();
        }

        if(kaine != null && kaine.coin >= SHOP_UPGRADE_COST)
        {
            kaine.coin -= SHOP_UPGRADE_COST;
            kaine.swordDamage += SHOP_DAMAGE_BONUS;
            shopTimer.mark();
        }

        if(aurea != null && aurea.coin >= SHOP_UPGRADE_COST)
        {
            aurea.coin -= SHOP_UPGRADE_COST;
            aurea.skillDamage += SHOP_DAMAGE_BONUS;
            shopTimer.mark();
        }
    }

    private void updateShopText()
    {
        showText("Shop: Press U - Upgrade weapon (" + SHOP_UPGRADE_COST + " coins)", 590, 25);
        showText("Damage +" + SHOP_DAMAGE_BONUS, 590, 55);
    }

    private int getEnemiesLeftInWave()
    {
        int enemiesDefeated = enemiesSpawnedThisWave - getObjects(Enemy.class).size();
        int enemiesLeft = enemiesThisWave - enemiesDefeated;
        return Math.max(0, enemiesLeft);
    }

    public void spawnEnemy()
    {
        int x = Greenfoot.getRandomNumber(getWidth());
        int maxSpawnY = BATTLE_TEXT_Y - ENEMY_BOTTOM_PADDING;
        int y = Greenfoot.getRandomNumber(maxSpawnY);
        Enemy enemy = new Enemy(x, y);
        enemy.applyLevelScaling(getCurrentHeroLevel());
        enemy.applyWaveScaling(waveNumber);
        addObject(enemy, x, y);
    }

    private void spawnAureaSkill()
    {
        if(aurea == null || aureaSkillTimer.millisElapsed() <= 900)
        {
            return;
        }

        Enemy closest = getClosestEnemy(aurea.getX(), aurea.getY());

        if(closest != null && aurea.hasEquippedFireball())
        {
            Fireball fireball = new Fireball(
                aurea.getX(),
                aurea.getY(),
                closest.getX(),
                closest.getY(),
                aurea.getDamage()
            );
            addObject(fireball, aurea.getX(), aurea.getY());
            aureaSkillTimer.mark();
        }
    }

    public Enemy getClosestEnemy(double x, double y)
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
            double distance = distanceBetween(enemy.getX(), enemy.getY(), x, y);

            if(distance < minDistance)
            {
                minDistance = distance;
                closest = enemy;
            }
        }

        return closest;
    }

    public void giveSelectedPlayerReward(int xp, int coin)
    {
        if(leon != null)
        {
            leon.xp += xp;
            leon.coin += coin;
            leon.checkLevelUp();
        }

        if(kaine != null)
        {
            kaine.gainXp(xp);
            kaine.gainCoin(coin);
        }

        if(aurea != null)
        {
            aurea.gainXP(xp);
            aurea.gainCoin(coin);
        }
    }

    public void damageSelectedPlayer(int damage)
    {
        if(leon != null)
        {
            leon.takeDamage(damage);
        }

        if(kaine != null)
        {
            kaine.takeDamage(damage);
        }

        if(aurea != null)
        {
            aurea.takeHit(damage);
        }
    }

    private double distanceBetween(double x1, double y1, double x2, double y2)
    {
        double dx = x1 - x2;
        double dy = y1 - y2;
        return Math.sqrt(dx * dx + dy * dy);
    }

    private int getCurrentHeroLevel()
    {
        if(leon != null)
        {
            return leon.level;
        }

        if(kaine != null)
        {
            return kaine.level;
        }

        if(aurea != null)
        {
            return aurea.level;
        }

        return 1;
    }
}
