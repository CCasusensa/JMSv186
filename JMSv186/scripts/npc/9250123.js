// ビジター マンホールくん
// test

// 整形
var face_list = Array(
	// 男
	20000,
	20001,
	20002,
	20003,
	20004,
	20005,
	20006,
	20007,
	20008,
	20009,
	20010,
	20011,
	20012,
	20013,
	20014,
	20016,
	20017,
	20018,
	20019,
	20020,
	20021,
	20022,
	20024,
	20025,
	20027,
	20028,
	20029,
	20030,
	20031,
	20032,
	20033,
	20035,
	20038,
	20040,
	// 女
	21000,
	21001,
	21002,
	21003,
	21004,
	21005,
	21006,
	21007,
	21008,
	21009,
	21010,
	21011,
	21012,
	21013,
	21014,
	21016,
	21017,
	21018,
	21019,
	21020,
	21021,
	21023,
	21024,
	21026,
	21027,
	21028,
	21029,
	21030,
	21031,
	21033,
	21036,
	21038
);
function Surgery() {
	cm.sendStyle("整形", face_list);
}

// 調髪
var hair_list = Array(
	// 男
	30000,
	//30010, // 特殊
	30020,
	30030,
	30040,
	30050,
	30060,
	//30070, // 特殊
	//30080, // 特殊
	//30090, // 特殊
	30100,
	30110,
	30120,
	30130,
	30140,
	30150,
	30160,
	30170,
	30180,
	30190,
	30200,
	30210,
	30220,
	30230,
	30240,
	30250,
	30260,
	30270,
	30280,
	30290,
	30300,
	30310,
	30320,
	30330,
	30340,
	30350,
	30360,
	30370,
	30400,
	30410,
	30420,
	30430,
	30440,
	30450,
	30460,
	30470,
	30480,
	30490,
	30510,
	30520,
	30530,
	30540,
	30550,
	30560,
	30570,
	30580,
	30590,
	30600,
	30610,
	30620,
	30630,
	30640,
	30650,
	30660,
	30670,
	30680,
	30690,
	30700,
	30710,
	30720,
	30730,
	30740,
	30750,
	30760,
	30770,
	30780,
	30790,
	30800,
	30810,
	30820,
	30830,
	30840,
	30850,
	30860,
	30870,
	30880,
	30890,
	30900,
	30910,
	30920,
	30930,
	30940,
	30950,
	30960,
	30970,
	30980,
	30990,
	31000,
	31010,
	31020,
	31030,
	31040,
	31050,
	31060,
	31070,
	31080,
	31090,
	31100,
	31110,
	31120,
	31130,
	31140,
	31150,
	31160,
	31170,
	31180,
	31190,
	31200,
	31210,
	31220,
	31230,
	31240,
	31250,
	31260,
	31270,
	31280,
	31290,
	31300,
	31310,
	31320,
	31330,
	31340
);

var hair_list2 = Array(
	31350,
	31400,
	31410,
	31420,
	31430,
	31440,
	31450,
	31460,
	31470,
	31480,
	31490,
	31510,
	31520,
	31530,
	31540,
	31550,
	31560,
	31570,
	31580,
	31590,
	31600,
	31610,
	31620,
	31630,
	31640,
	31650,
	31660,
	31670,
	31680,
	31690,
	31700,
	31710,
	31720,
	31730,
	31740,
	31750,
	31760,
	31770,
	31780,
	31790,
	31800,
	31810,
	31820,
	31830,
	31840,
	31850,
	31860,
	31870,
	31880,
	31890,
	31900,
	31910,
	31920,
	31930,
	31940,
	31950,
	31960,
	31970,
	31980,
	31990,
	32000,
	32010,
	32020,
	32030,
	32040,
	33010,
	33020,
	33030,
	33050,
	33060,
	33070,
	33080,
	33090,
	33110,
	33120,
	33130,
	33140,
	33150,
	33160,
	33170,
	33240,
	33260,
	34000,
	34010,
	34020,
	34030,
	34040,
	34060,
	34070,
	34080,
	34090,
	34100,
	34110,
	34120,
	34130,
	34140,
	34150,
	34160,
	34170,
	34240,
	34250,
	34260
);
function ChangeHair() {
	cm.sendStyle("調髪", hair_list);
}
function ChangeHair2() {
	cm.sendStyle("調髪2", hair_list2);
}

// 染毛
var new_hair_color = 0;
var hair_color_list = new Array();
function HairDyeing() {
	var my_hair_color = Math.floor((cm.getPlayerStat("HAIR") / 10)) * 10;
	hair_color_list = new Array();

	for (var i = 0; i < 8; i++) {
		hair_color_list[i] = my_hair_color + i;
	}
	cm.sendStyle("染毛", hair_color_list);
}

// スキンケア
var skin_list = Array(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11);
function SkinCare() {
	cm.sendStyle("スキンケア", skin_list);
}

var status = -1;
var old_selection = -1;
function action(mode, type, selection) {
	if (mode == 1) {
		status++;
	} else {
		status--;
	}

	switch (status) {
		case 0:
			{
				//cm.sendYesNo("退出しますか？");
				var text = "デバッグメニュー\r\nパワーエリクサーが必要です\r\n";
				text += "#L" + 1 + "##r整形#k#l\r\n";
				text += "#L" + 2 + "##r調髪#k#l\r\n";
				text += "#L" + 3 + "##r染毛#k#l\r\n";
				text += "#L" + 4 + "##rスキンケア#k#l\r\n";
				text += "#L" + 5 + "##r目の色#k#l\r\n";
				text += "#L" + 6 + "##r性転換#k#l\r\n";
				text += "#L" + 7 + "##r調髪2#k#l\r\n";
				return cm.sendSimple(text);
			}
		case 1:
			{
				old_selection = selection;
				switch (selection) {
					case 1:
						{
							return Surgery();
						}
					case 2:
						{
							return ChangeHair();
						}
					case 3:
						{
							return HairDyeing();
						}
					case 4:
						{
							return SkinCare();
						}
					case 5:
						{
							cm.sendSimple("作成中....");
							break;
						}
					case 6:
						{
							cm.sendSimple("作成中....");
							break;
						}
					// 髪型が0x7Fでオーバーフローしてそれ以降の選択肢がキャンセル扱い
					case 7:
						{
							return ChangeHair2();
						}
					default:
						break;
				}
				break;
			}
		case 2: {
			switch (old_selection) {
				case 1:
					{
						cm.setAvatar(2000005, face_list[selection]);
						break;
					}
				case 2:
					{
						
						cm.setAvatar(2000005, hair_list[selection]);
						break;
					}
				case 3:
					{
						cm.setAvatar(2000005, hair_color_list[selection]);
						break;
					}
				case 4:
					{
						cm.setAvatar(2000005, skin_list[selection]);
						break;
					}
				case 5:
					{
						// test
						break;
					}
				case 6:
					{
						// test
						break;
					}
				case 7:
					{
						cm.setAvatar(2000005, hair_list2[selection]);
						break;
					}
				default:
					break;
			}
			break;
		}
		default:
			break;
	}

	cm.dispose();
}