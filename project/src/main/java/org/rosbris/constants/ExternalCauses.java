package org.rosbris.constants;

import java.util.HashMap;
import java.util.Map;

public class ExternalCauses
{
    static private Map<Integer, String> causes = initCauses();

    public static boolean isExternalCause(int code)
    {
        return causes.containsKey(code);
    }

    public static String causeText(int code)
    {
        return causes.get(code);
    }

    private static Map<Integer, String> initCauses()
    {
        Map<Integer, String> m = new HashMap<>();

        m.put(160, "Несчастные случаи, связанные с мототранспортом");
        m.put(161, "Автомототранспортный несчастный случай на общественной дороге в результате наезда на пешехода");
        m.put(162, "Автомототранспортные несчастные случаи");
        m.put(163, "Случайные отравления алкоголем");
        m.put(164, "Другие случайные отравления");
        m.put(165, "Несчастные случаи во время лечения");
        m.put(166, "Случайные падения");
        m.put(167, "Несчастные случаи, вызванные огнем");
        m.put(168, "Случайное утопление и погружение в воду");
        m.put(169, "Случайное механическое удушение, закупорка дыхательных путей");
        m.put(170, "Несчастные случаи, вызванные огнестрельным оружием");
        m.put(171, "Несчастные случаи, вызванные электрическим током");
        m.put(172, "Другие несчастные случаи");
        m.put(173, "Самоубийство и самоповреждение");
        m.put(174, "Убийства и преднамеренные повреждения, нанесенные другим лицом и предусмотренные законом вмешательства");
        m.put(175, "Повреждения без уточнения их случайного или преднамеренного характера");
        m.put(187, "Переломы позвоночника Fractures of the spine");
        m.put(188, "Переломы конечностей Fractures of the limbs");
        m.put(189, "Внутричерепные травмы Intracranial injury");
        m.put(190, "Травмы внутренних органов Internal injuries");
        m.put(191, "Открытые раны Open wounds");
        m.put(192, "Последствия проникновения инородных тел");
        m.put(193, "Ожоги ");
        m.put(194, "Неблагоприятные реакции на лекарственные и другие вещества");
        m.put(195, "Все другие повреждения");

        return m;
    }
}
