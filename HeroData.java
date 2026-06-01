public class HeroData
{
    public static String heroType = "aurea"; // "aurea" | "leon" | "kaine"

    /** [fireball, lightning, iceWave, gun, sword] */
    public static int[] startingWeapons(String type)
    {
        int fireball = 0, lightning = 0, iceWave = 0, gun = 0, sword = 0;
        if("aurea".equals(type))
            lightning = 1;
        else if("leon".equals(type))
            gun = 1;
        else if("kaine".equals(type))
            sword = 1;
        return new int[] { fireball, lightning, iceWave, gun, sword };
    }

    /** fireball=0, lightning=1, iceWave=2, gun=3, sword=4 */
    public static int startingWeapon(String type, int index)
    {
        return startingWeapons(type)[index];
    }

    public static String signatureWeaponName(String type)
    {
        if("leon".equals(type)) return "Gun";
        if("kaine".equals(type)) return "Sword";
        return "Lightning";
    }
}