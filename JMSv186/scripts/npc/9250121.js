// �S�~����������
// !npc 9250121

function action(mode, type, selection) {
	//var jobid = cm.getJob();
	//var level = cm.getPlayer().getLevel();

	var text = "";
	//text += "Job ID = " + jobid + "\r\n";
	//text += "Lv = " + level + "\r\n";
	text += "�e�X�g���b�Z�[�W";

	//cm.changeMusic("Bgm09/TimeAttack");
	cm.changeMusic("Bgm03/Elfwood");
	cm.sendOk(text);
	cm.dispose();
	return;

	var p = cm.getOutPacket();

	p.writeShort(0x3F);
	p.write(3);
	p.writeMapleAsciiString("TSH");
	p.write(0);
	p.write(1);
	cm.SendPacket(p.getPacket());

	//cm.BroadcastPacket(cm.OutPacket().getPacket().getBytes());
	//DefeatedMessage(2);
	cm.dispose();
}

function DefeatedMessage(mobid) {
	switch (mobid) {
		case 0:
			{
				cm.WorldMessage("��ςȒ���̏I���Ƀz�[���e�C�������j������������I�M���B���{���̃��v���̉p�Y���I");
				break;
			}

		case 1:
			{
				cm.WorldMessage("�s���̓��u�Ńs���N�r�[����ނ����������̏��N�I�@�N�������^�̎��Ԃ̔e�҂��I");
				break;
			}

		case 2:
			{
				cm.WorldMessage("�т��񂿂��");
				break;
			}
		default:
			break;
	}
}