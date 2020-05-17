package com.yuri.commanderscompanion.api;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

public class GeneralHelperTest {

    ArrayList<String> sList1, sList2, sList3, sList1sorted, sList2sorted, sList3sorted;
    ArrayList<Integer> iList1, iList2, iList3, iList1sorted, iList2sorted, iList3sorted;

    ArrayList<Integer> longList, longListSorted;

    @Before
    public void init() {
        sList1 = new ArrayList<>(Arrays.asList("test", "abc", "bcd", "ABC", "TeD"));
        sList1sorted = new ArrayList<>(Arrays.asList("ABC", "abc", "bcd", "TeD", "test"));
        sList2 = new ArrayList<>(Arrays.asList("f", "e", "a", "c", "b", "d"));
        sList2sorted = new ArrayList<>(Arrays.asList("a", "b", "c", "d", "e", "f"));
        sList3 = new ArrayList<>(Arrays.asList("Noam", "Yuri", "Dan", "Ofek", "Yossi"));
        sList3sorted = new ArrayList<>(Arrays.asList("Dan", "Noam", "Ofek", "Yossi", "Yuri"));

        iList1 = new ArrayList<>(Arrays.asList(10, 12, 3, 1, 5, 80, 9));
        iList1sorted = new ArrayList<>(Arrays.asList(1, 3, 5, 9, 10, 12, 80));
        iList2 = new ArrayList<>(Arrays.asList(1000, 500, 600, -1, 300, 0));
        iList2sorted = new ArrayList<>(Arrays.asList(-1, 0, 300, 500, 600, 1000));
        iList3 = new ArrayList<>(Arrays.asList(0, 2, -1, -2, 1));
        iList3sorted = new ArrayList<>(Arrays.asList(-2, -1, 0, 1, 2));

        {
            longList = new ArrayList<>(Arrays.asList(11395, 50034, 53550, 3368, 59075, 42379, 16819,
                    21315, 17236, 19804, 61224, 33208, 9274, 40204, 10703, 47205, 42954, 16857,
                    28679, 5584, 56973, 22646, 7839, 46286, 2750, 16765, 61419, 6576, 30273, 41532,
                    36121, 62864, 36054, 34156, 52320, 4909, 60556, 44045, 58402, 4722, 63976,
                    60653, 59968, 17266, 58112, 8724, 798, 24713, 8339, 61121, 3503, 8549, 62032,
                    53428, 23945, 11315, 42198, 29763, 39381, 1401, 47694, 42140, 1411, 59719, 6123,
                    11075, 62693, 11138, 30019, 42562, 21943, 51138, 14217, 48950, 31446, 56082,
                    48643, 38190, 16054, 12121, 47380, 43073, 47095, 25292, 9285, 37350, 40262,
                    2587, 34526, 49469, 7410, 19180, 47388, 45214, 6323, 7698, 38066, 23894, 19418,
                    22117, 38252, 26933, 61398, 43572, 60296, 48436, 35244, 7999, 37149, 57209,
                    23029, 23821, 19256, 42509, 18210, 8829, 39179, 31199, 4699, 16517, 63990,
                    62835, 26921, 35132, 19017, 23484, 62133, 9157, 59175, 17694, 37730, 36565,
                    18138, 59141, 46663, 61456, 37713, 4015, 10424, 38959, 59192, 32385, 60706,
                    52744, 18449, 4606, 3951, 47199, 33894, 50706, 51087, 39157, 35428, 63338,
                    21023, 41074, 10563, 41602, 893, 1684, 41244, 10929, 12041, 55444, 62978, 42508,
                    12937, 5851, 59994, 34198, 22769, 42051, 2070, 40904, 26985, 632, 7279, 26076,
                    31755, 54066, 40565, 24095, 37368, 54164, 4027, 21378, 46267, 32005, 14898,
                    43567, 36100, 53022, 39675, 50588, 44977, 48862, 3361, 19968, 22806, 18561,
                    50176, 51009, 13859, 26689, 6185, 9305, 12297, 35048, 60500, 3726, 21905, 52374,
                    15700, 61234, 17325, 35749, 61190, 3857, 18941, 13088, 9234, 57872, 24294,
                    65426, 63457, 22386, 37931, 17235, 11559, 49423, 62443, 50242, 31565, 62437,
                    13502, 10106, 17807, 7769, 20868, 41367, 61988, 683, 43165, 7173, 9636, 37201,
                    21122, 33281, 55223, 52916, 50149, 19398, 23071, 773, 43208, 10264, 54985, 6360,
                    12830, 53337, 42705, 47647, 64765, 60755, 52727, 10243, 4332, 1461, 5223, 55620,
                    46290, 17565, 34081, 13348, 60678, 37227, 19759, 62774, 54418, 15618, 27174,
                    17970, 7890, 17095, 35870, 1619, 5526, 44246, 26955, 52246, 40764, 32250, 65165,
                    65424, 65535, 32522, 28209, 31077, 43197, 24982, 37839, 24040, 40209, 36828,
                    51371, 59811, 3879, 40719, 52352, 6598, 29568, 63733, 16696, 23986, 62610, 3429,
                    24026, 25823, 38068, 41735, 14846, 47766, 42375, 64169, 55763, 64703, 26567,
                    51701, 51660, 32929, 37238, 63906, 65305, 24231, 60018, 60829, 8412, 31714,
                    19077, 32175, 39097, 57008, 47527, 3485, 11998, 4679, 41271, 10865, 15292,
                    14481, 24336, 36873, 24896, 37530, 53714, 31944, 5954, 36362, 19836, 60625,
                    41910, 56661, 31717, 5573, 33133, 53953, 1715, 22267, 58326, 3642, 59422, 53864,
                    28982, 4670, 11093, 17060, 13641, 17812, 47444, 35886, 14981, 48427, 24282,
                    49360, 16975, 3212, 58464, 45846, 24986, 8196, 55089, 55256, 23827, 3096, 5787,
                    6060, 44644, 23700, 44041, 4981, 7810, 4058, 8091, 29334, 54489, 29649, 32859,
                    7220, 24165, 29773, 62798, 50011, 1239, 51514, 38655, 44722, 23173, 29096,
                    32598, 16928, 53286, 12145, 47821, 41112, 14955, 58552, 39913, 40360, 37369,
                    61629, 30628, 37410, 44164, 9408, 63014, 48246, 43953, 45178, 46843, 820, 58355,
                    16588, 33201, 49898, 23962, 59619, 25570, 31766, 19880, 2077, 3576, 62747,
                    36017, 29652, 33756, 64613, 10720, 10472, 46636, 31938, 24199, 1917, 269, 64054,
                    43759, 9753, 28277, 31119, 30185, 9232, 57535, 28124, 40327, 60338, 39225,
                    49125, 1695, 53838, 49243, 13198, 25750, 8687, 45733, 58768, 17337, 62703, 9153,
                    45560, 328, 28291, 9248, 32088, 41569, 55271, 4057, 62328, 17683, 512, 26740,
                    28852, 17259, 9238, 13946, 57627, 44528, 23953, 31665, 38278, 30460, 41558,
                    53701, 23732, 36295, 35232, 5640, 4340, 7152, 50327, 41621, 10512, 21795, 21800,
                    54150, 16614, 14911, 14214, 27461, 18595, 14170, 16884, 58863, 63269, 8818,
                    37909, 59595, 10589, 3833, 60242, 23242, 43893, 41452, 60462, 20631, 63288,
                    4600, 6087, 10423, 16031, 2611, 33041, 43633, 25805, 30198, 27222, 28344, 37887,
                    63377, 23553, 2476, 45778, 56851, 19896, 36022, 64136, 62829, 44941, 28614,
                    5482, 40254, 6262, 44306, 15895, 12661, 56457, 23148, 40944, 24021, 33744,
                    59382, 22358, 17943, 55112, 50306, 29696, 35765, 62863, 62745, 50187, 12316,
                    65273, 720, 26599, 43444, 15160, 4514, 42644, 59788, 31534, 51327, 24286, 41682,
                    50163, 2608, 6031, 6164, 40008, 46434, 24866, 9882, 9963, 46891, 51764, 56944,
                    30350, 17253, 26974, 39426, 44016, 52950, 4883, 62898, 7720, 35460, 51724,
                    10154, 54000, 59831, 5866, 25544, 7073, 11790, 56455, 2949, 64376, 9890, 19770,
                    53725, 6535, 31192, 17382, 40192, 9100, 37629, 19857, 19676, 52324, 60362,
                    51217, 18156, 8887, 7194, 44750, 24788, 30901, 37774, 17882, 15340, 50430,
                    17526, 39969, 39463, 2365, 43081, 10936, 18022, 22401, 5398, 62690, 59436,
                    43115, 10466, 45961, 32287, 65355, 59977, 45228, 26261, 36750, 38321, 1360,
                    50777, 8884, 7548, 43434, 29676, 53011, 49194, 9129, 46925, 20541, 22801, 49408,
                    58334, 64367, 54905, 51658, 41931, 64535, 25921, 27152, 42894, 39220, 8074,
                    33566, 50824, 40936, 41384, 65133, 41471, 3012, 6292, 64542, 39618, 21461,
                    37014, 27258, 54145, 49883, 14977, 31464, 59581, 44669, 18280, 56400, 30157,
                    37986, 27801, 29051, 47019, 47524, 493, 13235, 31558, 50847, 15816, 23114,
                    49803, 57579, 44706, 18892, 19866, 6001, 20956, 38141, 7058, 338, 10965, 37212,
                    38964, 5455, 61162, 29812, 54768, 54025, 55109, 5880, 3744, 60881, 51793, 28758,
                    7153, 32103, 58919, 37213, 21428, 52104, 12116, 5421, 52214, 26633, 36939,
                    60674, 9127, 11451, 49327, 39479, 6444, 30299, 24056, 32570, 52749, 50066,
                    64854, 26239, 17641, 11076, 53484, 23640, 59137, 38643, 9674, 18991, 32043,
                    63912, 8354, 7143, 7371, 16448, 58517, 25540, 53019, 17405, 33815, 30952, 63323,
                    45038, 20660, 5293, 30074, 41250, 60868, 24885, 51153, 43056, 23229, 8684, 6594,
                    62212, 20952, 352, 16920, 13687, 41689, 26691, 26443, 16522, 43553, 20377,
                    49252, 61128, 29402, 24756, 23645, 58814, 61960, 64057, 36333, 57880, 40552,
                    7120, 57687, 40821, 65169, 47597, 29655, 9740, 1373, 52912, 41448, 51221, 28463,
                    9440, 14587, 24743, 53655, 49165, 45698, 58537, 31303, 56037, 51390, 64033,
                    7823, 22259, 61932, 47124, 62513, 42840, 52812, 42527, 47203, 60919, 57347,
                    27975, 46564, 54587, 6061, 1814, 61515, 56516, 275, 838, 33656, 15751, 16630,
                    62334, 25721, 27408, 55731, 4363, 26502, 45757, 15264, 20496, 53651, 1712, 6463,
                    3560, 26622, 2410, 24262, 58621, 31348, 4242, 6220, 8192, 32411, 64502, 8938,
                    26822, 26351, 60624, 60059, 51281, 49678, 10510, 47243, 51705, 43414, 18124,
                    27210, 3075, 40819, 63663, 53439, 12048, 23429, 31973, 8730, 52693, 2152, 29344,
                    46155, 19933, 28721, 27441, 20067, 35210, 32883, 8752, 49534, 8029, 34056, 8155,
                    46927, 49422, 10586, 8740, 6006, 2172, 50102, 53534, 55294, 41199, 24942, 43203,
                    39021, 33360, 23431, 12820, 34209, 34004, 62918, 53757, 32201, 42476, 2062,
                    12526, 52238, 46476, 59517, 47582, 7969, 42120, 46373, 31045, 62200, 57925,
                    18888, 63768, 25070, 42544, 1378, 29423, 57639, 62981, 31476, 58372, 47996,
                    9276, 28412, 36248, 35527, 47128, 64046, 11106, 54598, 29916, 32248, 28662,
                    50869, 50526, 25567, 32490, 2397, 7060, 1852, 19918, 7615, 34627, 36744, 40793,
                    29608, 21162, 58445, 12481, 31990, 23130, 22025, 49987, 25818, 37545, 40901,
                    51459));
        }
        {
            longListSorted =new ArrayList<>(Arrays.asList(
                    269, 275, 328, 338, 352, 493, 512, 632, 683, 720, 773, 798, 820, 838, 893, 1239,
                    1360, 1373, 1378, 1401, 1411, 1461, 1619, 1684, 1695, 1712, 1715, 1814, 1852,
                    1917, 2062, 2070, 2077, 2152, 2172, 2365, 2397, 2410, 2476, 2587, 2608, 2611,
                    2750, 2949, 3012, 3075, 3096, 3212, 3361, 3368, 3429, 3485, 3503, 3560, 3576,
                    3642, 3726, 3744, 3833, 3857, 3879, 3951, 4015, 4027, 4057, 4058, 4242, 4332,
                    4340, 4363, 4514, 4600, 4606, 4670, 4679, 4699, 4722, 4883, 4909, 4981, 5223,
                    5293, 5398, 5421, 5455, 5482, 5526, 5573, 5584, 5640, 5787, 5851, 5866, 5880,
                    5954, 6001, 6006, 6031, 6060, 6061, 6087, 6123, 6164, 6185, 6220, 6262, 6292,
                    6323, 6360, 6444, 6463, 6535, 6576, 6594, 6598, 7058, 7060, 7073, 7120, 7143,
                    7152, 7153, 7173, 7194, 7220, 7279, 7371, 7410, 7548, 7615, 7698, 7720, 7769,
                    7810, 7823, 7839, 7890, 7969, 7999, 8029, 8074, 8091, 8155, 8192, 8196, 8339,
                    8354, 8412, 8549, 8684, 8687, 8724, 8730, 8740, 8752, 8818, 8829, 8884, 8887,
                    8938, 9100, 9127, 9129, 9153, 9157, 9232, 9234, 9238, 9248, 9274, 9276, 9285,
                    9305, 9408, 9440, 9636, 9674, 9740, 9753, 9882, 9890, 9963, 10106, 10154, 10243,
                    10264, 10423, 10424, 10466, 10472, 10510, 10512, 10563, 10586, 10589, 10703,
                    10720, 10865, 10929, 10936, 10965, 11075, 11076, 11093, 11106, 11138, 11315,
                    11395, 11451, 11559, 11790, 11998, 12041, 12048, 12116, 12121, 12145, 12297,
                    12316, 12481, 12526, 12661, 12820, 12830, 12937, 13088, 13198, 13235, 13348,
                    13502, 13641, 13687, 13859, 13946, 14170, 14214, 14217, 14481, 14587, 14846,
                    14898, 14911, 14955, 14977, 14981, 15160, 15264, 15292, 15340, 15618, 15700,
                    15751, 15816, 15895, 16031, 16054, 16448, 16517, 16522, 16588, 16614, 16630,
                    16696, 16765, 16819, 16857, 16884, 16920, 16928, 16975, 17060, 17095, 17235,
                    17236, 17253, 17259, 17266, 17325, 17337, 17382, 17405, 17526, 17565, 17641,
                    17683, 17694, 17807, 17812, 17882, 17943, 17970, 18022, 18124, 18138, 18156,
                    18210, 18280, 18449, 18561, 18595, 18888, 18892, 18941, 18991, 19017, 19077,
                    19180, 19256, 19398, 19418, 19676, 19759, 19770, 19804, 19836, 19857, 19866,
                    19880, 19896, 19918, 19933, 19968, 20067, 20377, 20496, 20541, 20631, 20660,
                    20868, 20952, 20956, 21023, 21122, 21162, 21315, 21378, 21428, 21461, 21795,
                    21800, 21905, 21943, 22025, 22117, 22259, 22267, 22358, 22386, 22401, 22646,
                    22769, 22801, 22806, 23029, 23071, 23114, 23130, 23148, 23173, 23229, 23242,
                    23429, 23431, 23484, 23553, 23640, 23645, 23700, 23732, 23821, 23827, 23894,
                    23945, 23953, 23962, 23986, 24021, 24026, 24040, 24056, 24095, 24165, 24199,
                    24231, 24262, 24282, 24286, 24294, 24336, 24713, 24743, 24756, 24788, 24866,
                    24885, 24896, 24942, 24982, 24986, 25070, 25292, 25540, 25544, 25567, 25570,
                    25721, 25750, 25805, 25818, 25823, 25921, 26076, 26239, 26261, 26351, 26443,
                    26502, 26567, 26599, 26622, 26633, 26689, 26691, 26740, 26822, 26921, 26933,
                    26955, 26974, 26985, 27152, 27174, 27210, 27222, 27258, 27408, 27441, 27461,
                    27801, 27975, 28124, 28209, 28277, 28291, 28344, 28412, 28463, 28614, 28662,
                    28679, 28721, 28758, 28852, 28982, 29051, 29096, 29334, 29344, 29402, 29423,
                    29568, 29608, 29649, 29652, 29655, 29676, 29696, 29763, 29773, 29812, 29916,
                    30019, 30074, 30157, 30185, 30198, 30273, 30299, 30350, 30460, 30628, 30901,
                    30952, 31045, 31077, 31119, 31192, 31199, 31303, 31348, 31446, 31464, 31476,
                    31534, 31558, 31565, 31665, 31714, 31717, 31755, 31766, 31938, 31944, 31973,
                    31990, 32005, 32043, 32088, 32103, 32175, 32201, 32248, 32250, 32287, 32385,
                    32411, 32490, 32522, 32570, 32598, 32859, 32883, 32929, 33041, 33133, 33201,
                    33208, 33281, 33360, 33566, 33656, 33744, 33756, 33815, 33894, 34004, 34056,
                    34081, 34156, 34198, 34209, 34526, 34627, 35048, 35132, 35210, 35232, 35244,
                    35428, 35460, 35527, 35749, 35765, 35870, 35886, 36017, 36022, 36054, 36100,
                    36121, 36248, 36295, 36333, 36362, 36565, 36744, 36750, 36828, 36873, 36939,
                    37014, 37149, 37201, 37212, 37213, 37227, 37238, 37350, 37368, 37369, 37410,
                    37530, 37545, 37629, 37713, 37730, 37774, 37839, 37887, 37909, 37931, 37986,
                    38066, 38068, 38141, 38190, 38252, 38278, 38321, 38643, 38655, 38959, 38964,
                    39021, 39097, 39157, 39179, 39220, 39225, 39381, 39426, 39463, 39479, 39618,
                    39675, 39913, 39969, 40008, 40192, 40204, 40209, 40254, 40262, 40327, 40360,
                    40552, 40565, 40719, 40764, 40793, 40819, 40821, 40901, 40904, 40936, 40944,
                    41074, 41112, 41199, 41244, 41250, 41271, 41367, 41384, 41448, 41452, 41471,
                    41532, 41558, 41569, 41602, 41621, 41682, 41689, 41735, 41910, 41931, 42051,
                    42120, 42140, 42198, 42375, 42379, 42476, 42508, 42509, 42527, 42544, 42562,
                    42644, 42705, 42840, 42894, 42954, 43056, 43073, 43081, 43115, 43165, 43197,
                    43203, 43208, 43414, 43434, 43444, 43553, 43567, 43572, 43633, 43759, 43893,
                    43953, 44016, 44041, 44045, 44164, 44246, 44306, 44528, 44644, 44669, 44706,
                    44722, 44750, 44941, 44977, 45038, 45178, 45214, 45228, 45560, 45698, 45733,
                    45757, 45778, 45846, 45961, 46155, 46267, 46286, 46290, 46373, 46434, 46476,
                    46564, 46636, 46663, 46843, 46891, 46925, 46927, 47019, 47095, 47124, 47128,
                    47199, 47203, 47205, 47243, 47380, 47388, 47444, 47524, 47527, 47582, 47597,
                    47647, 47694, 47766, 47821, 47996, 48246, 48427, 48436, 48643, 48862, 48950,
                    49125, 49165, 49194, 49243, 49252, 49327, 49360, 49408, 49422, 49423, 49469,
                    49534, 49678, 49803, 49883, 49898, 49987, 50011, 50034, 50066, 50102, 50149,
                    50163, 50176, 50187, 50242, 50306, 50327, 50430, 50526, 50588, 50706, 50777,
                    50824, 50847, 50869, 51009, 51087, 51138, 51153, 51217, 51221, 51281, 51327,
                    51371, 51390, 51459, 51514, 51658, 51660, 51701, 51705, 51724, 51764, 51793,
                    52104, 52214, 52238, 52246, 52320, 52324, 52352, 52374, 52693, 52727, 52744,
                    52749, 52812, 52912, 52916, 52950, 53011, 53019, 53022, 53286, 53337, 53428,
                    53439, 53484, 53534, 53550, 53651, 53655, 53701, 53714, 53725, 53757, 53838,
                    53864, 53953, 54000, 54025, 54066, 54145, 54150, 54164, 54418, 54489, 54587,
                    54598, 54768, 54905, 54985, 55089, 55109, 55112, 55223, 55256, 55271, 55294,
                    55444, 55620, 55731, 55763, 56037, 56082, 56400, 56455, 56457, 56516, 56661,
                    56851, 56944, 56973, 57008, 57209, 57347, 57535, 57579, 57627, 57639, 57687,
                    57872, 57880, 57925, 58112, 58326, 58334, 58355, 58372, 58402, 58445, 58464,
                    58517, 58537, 58552, 58621, 58768, 58814, 58863, 58919, 59075, 59137, 59141,
                    59175, 59192, 59382, 59422, 59436, 59517, 59581, 59595, 59619, 59719, 59788,
                    59811, 59831, 59968, 59977, 59994, 60018, 60059, 60242, 60296, 60338, 60362,
                    60462, 60500, 60556, 60624, 60625, 60653, 60674, 60678, 60706, 60755, 60829,
                    60868, 60881, 60919, 61121, 61128, 61162, 61190, 61224, 61234, 61398, 61419,
                    61456, 61515, 61629, 61932, 61960, 61988, 62032, 62133, 62200, 62212, 62328,
                    62334, 62437, 62443, 62513, 62610, 62690, 62693, 62703, 62745, 62747, 62774,
                    62798, 62829, 62835, 62863, 62864, 62898, 62918, 62978, 62981, 63014, 63269,
                    63288, 63323, 63338, 63377, 63457, 63663, 63733, 63768, 63906, 63912, 63976,
                    63990, 64033, 64046, 64054, 64057, 64136, 64169, 64367, 64376, 64502, 64535,
                    64542, 64613, 64703, 64765, 64854, 65133, 65165, 65169, 65273, 65305, 65355,
                    65424, 65426, 65535));
        }
    }

    @Test
    public void sortSList1(){
        GeneralHelper.quickSort(sList1, GeneralHelper::stringCompare);

        Assert.assertEquals(sList1sorted, sList1);
    }

    @Test
    public void sortSList2(){
        GeneralHelper.quickSort(sList2, GeneralHelper::stringCompare);

        Assert.assertEquals(sList2sorted, sList2);
    }

    @Test
    public void sortSList3(){
        GeneralHelper.quickSort(sList3, GeneralHelper::stringCompare);

        Assert.assertEquals(sList3sorted, sList3);
    }

    @Test
    public void sortIList2(){
        GeneralHelper.quickSort(iList1, (i1, i2) -> i1 - i2 );

        Assert.assertEquals(iList1sorted, iList1);
    }

    @Test
    public void sortIList1(){
        GeneralHelper.quickSort(iList2, (i1, i2) -> i1 - i2 );

        Assert.assertEquals(iList2sorted, iList2);
    }

    @Test
    public void sortIList3(){
        GeneralHelper.quickSort(iList3, (i1, i2) -> i1 - i2 );

        Assert.assertEquals(iList3sorted, iList3);
    }

    @Test
    public void sortLongIList4(){
        GeneralHelper.quickSort(longList, (i1, i2) -> i1 - i2 );

        Assert.assertEquals(longListSorted, longList);
    }


    // ---- stringCompare test

    @Test
    public void compare_abc_To_bcd(){
        Assert.assertEquals(-1, GeneralHelper.stringCompare("abc", "bcd"));
    }

    @Test
    public void compare_bcd_To_abc(){
        Assert.assertEquals(1, GeneralHelper.stringCompare("bcd", "abc"));
    }

    @Test
    public void compare_abc_To_ABC(){
        Assert.assertEquals(0, GeneralHelper.stringCompare("abc", "ABC"));
    }

    @Test
    public void compare_test_To_Ted(){
        Assert.assertEquals(1, GeneralHelper.stringCompare("test", "Ted"));
    }

    @Test
    public void compare_Ted_To_test(){
        Assert.assertEquals(-1, GeneralHelper.stringCompare("Ted", "test"));
    }
}