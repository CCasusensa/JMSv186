// �i�I�~
var status = -1;
var towns = Array(100000000, 261000000, 240000000);

function action(mode, type, selection) {
	var id = cm.getNpc();
	var mapid = cm.getMapId();
	var text = "�f�o�b�O���\r\n";
	text += "#p" + id + "#\r\n";
	text += "NPC ID = #b" + id + "#k\r\n";
	text += "#m" + mapid + "#\r\n";
	text += "Map ID = #b" + mapid + "#k\r\n";

	if (mode != 1) {
		cm.dispose();
		return;
	}

	status++;

	switch (status) {
		// �I�����
		case 0:
			{
				text = "";
				text += "�}�b�v�ړ����I�����Ă�������\r\n";

				for (var i = 0; i < towns.length; i++) {
					text += "#L" + i + "##b#m" + towns[i] + "##l#k\r\n";
				}

				cm.sendSimple(text);
			}
			return;
		case 1:
			cm.warp(towns[selection], 0);
			break;
		default:
			break;
	}
	return cm.dispose();
}